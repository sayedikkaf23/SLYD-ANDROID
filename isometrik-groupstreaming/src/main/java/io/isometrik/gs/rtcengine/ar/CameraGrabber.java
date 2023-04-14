package io.isometrik.gs.rtcengine.ar;

import ai.deepar.ar.CameraResolutionPreset;
import ai.deepar.ar.DeepAR;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * The class to grab camera frames and pass them on to AR engine.
 */
public class CameraGrabber {
  private static final String TAG = CameraGrabber.class.getSimpleName();

  private static final int NUMBER_OF_BUFFERS = 2;

  private static int currentCameraDevice = Camera.CameraInfo.CAMERA_FACING_FRONT;

  //private int width = 1280;
  //private int height = 720;
  private int width = 640;
  private int height = 480;
  private CameraResolutionPreset resolutionPreset = CameraResolutionPreset.P640x480;

  private int screenOrientation = 0;

  /**
   * Instantiates a new Camera grabber.
   */
  public CameraGrabber() {
  }

  /**
   * Instantiates a new Camera grabber.
   *
   * @param cameraDevice the camera device
   */
  public CameraGrabber(int cameraDevice) {
    CameraGrabber.currentCameraDevice = cameraDevice;
  }

  /**
   * Sets frame receiver.
   *
   * @param receiver the receiver
   */
  public void setFrameReceiver(DeepAR receiver) {
    if (mThread != null) {
      mThread.setFrameReceiver(receiver, currentCameraDevice);
    }
  }

  /**
   * Initialize camera.
   *
   * @param listener the CameraGrabberListener listener
   * @see io.isometrik.gs.rtcengine.ar.CameraGrabberListener
   */
  public void initCamera(CameraGrabberListener listener) {
    if (mThread == null) {
      mThread = new CameraHandlerThread(listener, width, height, screenOrientation);
    }

    synchronized (mThread) {
      mThread.openCamera();
    }
  }

  /**
   * Start camera preview.
   */
  public void startPreview() {
    if (mThread != null && mThread.camera != null) {
      mThread.camera.startPreview();
    }
  }

  /**
   * Stop camera preview.
   */
  public void stopPreview() {
    if (mThread != null && mThread.camera != null) {
      mThread.camera.stopPreview();
    }
  }

  /**
   * Change camera device.
   *
   * @param cameraDevice the camera device
   */
  public void changeCameraDevice(int cameraDevice) {
    currentCameraDevice = cameraDevice;
    initCamera(new CameraGrabberListener() {
      @Override
      public void onCameraInitialized() {
        startPreview();
      }

      @Override
      public void onCameraError(String errorMsg) {
        Log.e(TAG, errorMsg);
      }
    });
  }

  /**
   * Gets current camera device.
   *
   * @return the current camera device
   */
  public int getCurrCameraDevice() {
    return currentCameraDevice;
  }

  /**
   * Release camera.
   */
  public void releaseCamera() {
    if (mThread != null) {
      mThread.releaseCamera();
      mThread = null;
    }
  }

  private CameraHandlerThread mThread = null;

  /**
   * Gets resolution preset.
   *
   * @return the resolution preset
   */
  public CameraResolutionPreset getResolutionPreset() {
    return resolutionPreset;
  }

  /**
   * Sets resolution preset.
   *
   * @param resolutionPreset the resolution preset
   */
  public void setResolutionPreset(CameraResolutionPreset resolutionPreset) {

    this.resolutionPreset = resolutionPreset;

    if (this.resolutionPreset == CameraResolutionPreset.P640x480) {
      width = 640;
      height = 480;
    } else if (this.resolutionPreset == CameraResolutionPreset.P1280x720) {
      width = 1280;
      height = 720;
    } else if (this.resolutionPreset == CameraResolutionPreset.P640x360) {
      width = 640;
      height = 360;
    }

    if (mThread != null) {
      mThread.reinitCamera(width, height);
    }
  }

  /**
   * Gets screen orientation.
   *
   * @return the screen orientation
   */
  public int getScreenOrientation() {
    return screenOrientation;
  }

  /**
   * Sets screen orientation.
   *
   * @param screenOrientation the screen orientation
   */
  public void setScreenOrientation(int screenOrientation) {
    this.screenOrientation = screenOrientation;
  }

  /**
   * Gets camera.
   *
   * @return the camera
   * @see android.hardware.Camera
   */
  public Camera getCamera() {
    if (mThread == null) {
      return null;
    }
    return mThread.camera;
  }

  private static class CameraHandlerThread extends HandlerThread {

    Handler mHandler = null;
    public Camera camera;
    public SurfaceTexture surface;
    private DeepAR frameReceiver;
    private ByteBuffer[] buffers;
    private int currentBuffer = 0;
    private CameraGrabberListener listener;
    private int cameraOrientation;
    private int width;
    private int height;
    private int screenOrientation;

    /**
     * Instantiates a new Camera handler thread.
     *
     * @param listener the CameraGrabberListener listener
     * @param width the width
     * @param height the height
     * @param screenOrientation the screen orientation
     * @see io.isometrik.gs.rtcengine.ar.CameraGrabberListener
     */
    CameraHandlerThread(CameraGrabberListener listener, int width, int height,
        int screenOrientation) {
      super("CameraHandlerThread");

      this.listener = listener;
      this.width = width;
      this.height = height;
      this.screenOrientation = screenOrientation;
      start();
      mHandler = new Handler(getLooper());
    }

    /**
     * Notify camera opened.
     */
    synchronized void notifyCameraOpened() {
      notify();
    }

    /**
     * Release camera.
     */
    synchronized void releaseCamera() {

      if (camera == null) {
        return;
      }

      mHandler.post(() -> {

        camera.stopPreview();

        camera.setPreviewCallbackWithBuffer(null);

        camera.release();

        camera = null;
        mHandler = null;
        listener = null;
        frameReceiver = null;
        surface = null;
        buffers = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
          quitSafely();
        } else {
          quit();
        }
      });
    }

    /**
     * Sets frame receiver.
     *
     * @param receiver the receiver
     * @param cameraDevice the camera device
     */
    synchronized void setFrameReceiver(DeepAR receiver, final int cameraDevice) {
      frameReceiver = receiver;
      mHandler.post(() -> {
        if (camera == null) {
          return;
        }

        camera.setPreviewCallbackWithBuffer((data, arg1) -> {
          if (frameReceiver != null) {
            buffers[currentBuffer].put(data);
            buffers[currentBuffer].position(0);
            if (frameReceiver != null) {

              frameReceiver.receiveFrame(buffers[currentBuffer], width, height, cameraOrientation,
                  cameraDevice == Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            currentBuffer = (currentBuffer + 1) % NUMBER_OF_BUFFERS;
          }
          if (camera != null) {
            camera.addCallbackBuffer(data);
          }
        });
      });
    }

    private void init() {

      if (camera != null) {
        camera.setPreviewCallbackWithBuffer(null);
        camera.stopPreview();
        camera.release();
        camera = null;
      }

      if (surface == null) {
        surface = new SurfaceTexture(0);
      }

      Camera.CameraInfo info = new Camera.CameraInfo();
      int cameraId = -1;
      int numberOfCameras = Camera.getNumberOfCameras();
      for (int i = 0; i < numberOfCameras; i++) {
        Camera.getCameraInfo(i, info);
        if (info.facing == currentCameraDevice) {
          cameraOrientation = info.orientation;

          if (currentCameraDevice == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraOrientation = (info.orientation + screenOrientation) % 360;
            //cameraOrientation = (360 - cameraOrientation) % 360;
          } else {
            cameraOrientation = (info.orientation - screenOrientation + 360) % 360;
          }
          cameraId = i;
          break;
        }
      }

      if (cameraId == -1) {
        if (listener != null) {
          listener.onCameraError("Camera not found error.");
        }
        return;
      }

      try {
        camera = Camera.open(cameraId);
      } catch (Exception e) {
        // event error
        if (listener != null) {
          listener.onCameraError("Could not open camera device. Could be used by another process.");
        }
        return;
      }

      Camera.Parameters params = camera.getParameters();

      boolean presetExists = false;
      List<Camera.Size> availableSizes = camera.getParameters().getSupportedPictureSizes();
      for (Camera.Size size : availableSizes) {
        if (size.width == width && size.height == height) {
          presetExists = true;
          break;
        }
      }

      if (!presetExists) {
        Log.e(TAG, "Selected resolution preset is not available on this device");
        listener.onCameraError("Selected preset resolution of "
            + width
            + "x"
            + height
            + " is not supported for this device.");
        return;
      }

      params.setPreviewSize(width, height);
      params.setPictureSize(width, height);
      params.setPictureFormat(PixelFormat.JPEG);
      params.setJpegQuality(90);
      params.setPreviewFormat(ImageFormat.NV21);

            /*
            List<int[]> ranges = params.getSupportedPreviewFpsRange();
            int[] bestRange = {0,0};
            for (int[] range : ranges) {
                if (range[0] > bestRange[0]) {
                    bestRange[0] = range[0];
                    bestRange[1] = range[1];
                }
            }
            params.setPreviewFpsRange(bestRange[0], bestRange[1]);
            */

      camera.setParameters(params);

      buffers = new ByteBuffer[NUMBER_OF_BUFFERS];
      for (int i = 0; i < NUMBER_OF_BUFFERS; i++) {
        buffers[i] = ByteBuffer.allocateDirect(width * height * 3 / 2);
        buffers[i].order(ByteOrder.nativeOrder());
        buffers[i].position(0);
        byte[] buffer = new byte[width * height * 3 / 2];
        camera.addCallbackBuffer(buffer);
      }

      try {
        camera.setPreviewTexture(surface);
      } catch (IOException ioe) {
        if (listener != null) {
          listener.onCameraError("Error setting preview texture.");
        }
        return;
      }

      if (frameReceiver != null) {
        setFrameReceiver(frameReceiver, currentCameraDevice);
      }
      if (listener != null) {
        listener.onCameraInitialized();
      }
    }

    /**
     * Re-initialize camera.
     *
     * @param newWidth the new width
     * @param newHeight the new height
     */
    void reinitCamera(final int newWidth, final int newHeight) {
      mHandler.post(() -> {
        camera.stopPreview();
        camera.setPreviewCallbackWithBuffer(null);
        camera.release();
        camera = null;
        width = newWidth;
        height = newHeight;
        init();
      });
    }

    /**
     * Open camera.
     */
    void openCamera() {

      mHandler.post(() -> {
        init();
        notifyCameraOpened();
      });
      try {
        wait();
      } catch (InterruptedException e) {
        Log.w(TAG, "wait was interrupted");
      }
    }
  }
};