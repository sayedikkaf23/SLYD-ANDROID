package chat.hola.com.app.DublyCamera.gpuv.camerarecorder;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sudamasayuki on 2018/03/13.
 */

public class CameraThread extends Thread {

  private static final String TAG = "CameraThread";

  private final Object readyFence = new Object();
  private CameraHandler handler;
  volatile boolean isRunning = false;

  private CameraDevice cameraDevice;
  private CaptureRequest.Builder requestBuilder;
  private CameraCaptureSession cameraCaptureSession;
  private Rect sensorArraySize;

  private SurfaceTexture surfaceTexture;

  private final OnStartPreviewListener listener;
  private final CameraRecordListener cameraRecordListener;
  private final CameraManager cameraManager;

  private Size cameraSize;
  private boolean isFlashTorch = false;
  private final LensFacing lensFacing;

  private boolean flashSupport = false;

  CameraThread(final CameraRecordListener cameraRecordListener,
      final OnStartPreviewListener listener, final SurfaceTexture surfaceTexture,
      final CameraManager cameraManager, final LensFacing lensFacing) {
    super("Camera thread");
    this.listener = listener;
    this.cameraRecordListener = cameraRecordListener;
    this.surfaceTexture = surfaceTexture;
    this.cameraManager = cameraManager;
    this.lensFacing = lensFacing;
  }

  public CameraHandler getHandler() {
    synchronized (readyFence) {
      try {
        readyFence.wait();
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }
    }
    return handler;
  }

  private CameraDevice.StateCallback cameraDeviceCallback = new CameraDevice.StateCallback() {
    @Override
    public void onOpened(CameraDevice camera) {
      //            Log.d(TAG, "cameraDeviceCallback onOpened");
      CameraThread.this.cameraDevice = camera;
      createCaptureSession();
    }

    @Override
    public void onDisconnected(CameraDevice camera) {
      //            Log.d(TAG, "cameraDeviceCallback onDisconnected");
      camera.close();
      CameraThread.this.cameraDevice = null;
    }

    @Override
    public void onError(CameraDevice camera, int error) {
      //            Log.d(TAG, "cameraDeviceCallback onError");
      camera.close();
      CameraThread.this.cameraDevice = null;
    }
  };

  private CameraCaptureSession.StateCallback cameraCaptureSessionCallback =
      new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
          cameraCaptureSession = session;
          updatePreview();
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
          // Toast.makeText(activity, "onConfigureFailed", Toast.LENGTH_LONG).show();
        }
      };

  private void updatePreview() {

    requestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);

    HandlerThread thread = new HandlerThread("CameraPreview");
    thread.start();
    Handler backgroundHandler = new Handler(thread.getLooper());

    try {
      cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, backgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * message loop
   * prepare Looper and create Handler for this thread
   */
  @Override
  public void run() {
    //        Log.d(TAG, "Camera thread start");
    Looper.prepare();
    synchronized (readyFence) {
      handler = new CameraHandler(this);
      isRunning = true;
      readyFence.notify();
    }
    Looper.loop();
    //        Log.d(TAG, "Camera thread finish");
    if (cameraRecordListener != null) {
      cameraRecordListener.onCameraThreadFinish();
    }
    synchronized (readyFence) {
      handler = null;
      isRunning = false;
    }
  }

  /**
   * start camera preview
   */

  @SuppressLint("MissingPermission")
  final void startPreview(final int width, final int height) {
    //        Log.d(TAG, "startPreview:");

    try {

      if (cameraManager == null) return;
      for (String cameraId : cameraManager.getCameraIdList()) {
        CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

        MAX_ZOOM_LEVEL =
            characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);

        cameraCharacteristics = characteristics;
        MAX_ZOOM_LEVEL_ALLOWED = MAX_ZOOM_LEVEL * 0.60f;
        //Allowing only 50 % zoom
        //Assuming maximum 10 levels of zoom allowed
        ZOOM_STEP = (MAX_ZOOM_LEVEL_ALLOWED) / 10;
        if (characteristics.get(CameraCharacteristics.LENS_FACING) == null
            || characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) == null) {
          continue;
        }
        if (characteristics.get(CameraCharacteristics.LENS_FACING) == lensFacing.getFacing()) {
          sensorArraySize =
              characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

          flashSupport = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
          StreamConfigurationMap map =
              characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

          if (width < 0 || height < 0) {
            cameraSize = map.getOutputSizes(SurfaceTexture.class)[0];
          } else {
            cameraSize =
                getClosestSupportedSize(Arrays.asList(map.getOutputSizes(SurfaceTexture.class)),
                    width, height);
          }
          //                    Log.d(TAG, "cameraSize =" + cameraSize);

          HandlerThread thread = new HandlerThread("OpenCamera");
          thread.start();
          Handler backgroundHandler = new Handler(thread.getLooper());

          cameraManager.openCamera(cameraId, cameraDeviceCallback, backgroundHandler);

          return;
        }
      }
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void createCaptureSession() {
    surfaceTexture.setDefaultBufferSize(cameraSize.getWidth(), cameraSize.getHeight());
    Surface surface = new Surface(surfaceTexture);

    try {
      if(android.os.Build.MANUFACTURER.equalsIgnoreCase("Google"))
        requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
      else
        requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

    } catch (CameraAccessException e) {
      e.printStackTrace();
    }

    requestBuilder.addTarget(surface);
    try {
      cameraDevice.createCaptureSession(Collections.singletonList(surface),
          cameraCaptureSessionCallback, null);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }

    listener.onStart(cameraSize, flashSupport);
  }

  private static Size getClosestSupportedSize(List<Size> supportedSizes, final int requestedWidth,
      final int requestedHeight) {
    return Collections.min(supportedSizes, new Comparator<Size>() {

      private int diff(final Size size) {
        return Math.abs(requestedWidth - size.getWidth()) + Math.abs(
            requestedHeight - size.getHeight());
      }

      @Override
      public int compare(final Size lhs, final Size rhs) {
        return diff(lhs) - diff(rhs);
      }
    });
  }

  /**
   * stop camera preview
   */
  void stopPreview() {
    //        Log.d(TAG, "stopPreview:");
    isFlashTorch = false;
    if (requestBuilder != null) {
      requestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);

      try {
        cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, null);
        cameraDevice.close();
        //                Log.d(TAG, "stopPreview: cameraDevice.close()");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * change focus
   */
  void changeManualFocusPoint(float eventX, float eventY, int viewWidth, int viewHeight) {

    final int y = (int) ((eventX / (float) viewWidth) * (float) sensorArraySize.height());
    final int x = (int) ((eventY / (float) viewHeight) * (float) sensorArraySize.width());
    final int halfTouchWidth = 400;
    final int halfTouchHeight = 400;
    MeteringRectangle focusAreaTouch =
        new MeteringRectangle(Math.max(x - halfTouchWidth, 0), Math.max(y - halfTouchHeight, 0),
            halfTouchWidth * 2, halfTouchHeight * 2, MeteringRectangle.METERING_WEIGHT_MAX - 1);

    if (requestBuilder != null) {
      requestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS,
          new MeteringRectangle[] { focusAreaTouch });
    }
    try {
      cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, null);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (requestBuilder != null) {
      requestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
      requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
      requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
          CameraMetadata.CONTROL_AF_TRIGGER_START);
    }
    //then we ask for a single request (not repeating!)
    try {
      cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, null);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // フラッシュ切り替え
  void switchFlashMode() {
    if (!flashSupport) return;

    try {
      if (isFlashTorch) {
        isFlashTorch = false;
        requestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);
      } else {
        isFlashTorch = true;
        requestBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
      }

      cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void changeAutoFocus() {
    requestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
    requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
    //then we ask for a single request (not repeating!)
    try {
      cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, null);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  interface OnStartPreviewListener {
    void onStart(Size previewSize, boolean flashSupport);
  }

  private CameraCharacteristics cameraCharacteristics;

  private float zoom = 0;

  private float ZOOM_STEP;

  void updateZoomLevel(boolean zoomOut, boolean reset) {

    if (cameraCharacteristics != null && requestBuilder != null && cameraCaptureSession != null) {

      if (reset) {

        resetCameraZoom();
      } else {
        if (zoomOut) {

          if (zoom > 0) {
            zoom = zoom - ZOOM_STEP;

            if (zoom < 0) {

              zoom = 0;
            }
            zoomOut(zoom);
          }
        } else {

          if (zoom < MAX_ZOOM_LEVEL_ALLOWED) {
            zoom = zoom + ZOOM_STEP;

            if (zoom > MAX_ZOOM_LEVEL_ALLOWED) {

              zoom = MAX_ZOOM_LEVEL_ALLOWED;
            }

            zoomIn(zoom);
          }
        }
      }
    }
  }

  private float MAX_ZOOM_LEVEL;

  private float MAX_ZOOM_LEVEL_ALLOWED;

  private void zoomIn(float zoom) {


    Rect rect =
        (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

    if (rect != null) {
      int width =
          (int) ((((float) (rect.width() - ((int) (((float) rect.width()) / MAX_ZOOM_LEVEL))))
              / MAX_ZOOM_LEVEL) * zoom);
      int height =
          (int) ((((float) (rect.height() - ((int) (((float) rect.height()) / MAX_ZOOM_LEVEL))))
              / MAX_ZOOM_LEVEL) * zoom);
      width -= width & 3;
      height -= height & 3;
      try {
        requestBuilder.set(CaptureRequest.SCALER_CROP_REGION,
            new Rect(width, height, rect.width() - width, rect.height() - height));
        cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void zoomOut(float zoom) {

    Rect rect =
        (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

    if (rect != null) {
      int width =
          (int) ((((float) (rect.width() - ((int) (((float) rect.width()) / MAX_ZOOM_LEVEL))))
              / MAX_ZOOM_LEVEL) * zoom);
      int height =
          (int) ((((float) (rect.height() - ((int) (((float) rect.height()) / MAX_ZOOM_LEVEL))))
              / MAX_ZOOM_LEVEL) * zoom);
      width += width & 3;
      height += height & 3;
      try {
        requestBuilder.set(CaptureRequest.SCALER_CROP_REGION,
            new Rect(width, height, rect.width() - width, rect.height() - height));
        cameraCaptureSession.setRepeatingRequest(requestBuilder.build(), null, null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void resetCameraZoom() {

    zoomOut(0.0001f);
    zoom = 0;
  }
}

