package io.isometrik.gs.rtcengine.ar.capture;

import ai.deepar.ar.CameraResolutionPreset;
import ai.deepar.ar.DeepAR;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.rtcengine.ar.ARHelper;
import io.isometrik.gs.rtcengine.ar.CameraGrabber;
import io.isometrik.gs.rtcengine.ar.CameraGrabberListener;

/**
 * The Capture operations utility to allow capturing of the cover image for the live stream with AR
 * effects.
 */
public class CaptureOperations {

  private CameraGrabber cameraGrabber;
  private DeepAR deepAR;

  /**
   * Instantiates a new Capture operations.
   *
   * @param isometrik the isometrik instance
   * @param arView the AR view
   */
  public CaptureOperations(Isometrik isometrik, SurfaceView arView) {
    this.deepAR = isometrik.getAREngine();
    initializeAREngine(arView, isometrik.getAREngine());
  }

  /**
   * Request image capture.
   */
  public void requestImageCapture() {
    deepAR.takeScreenshot();
  }

  /**
   * Open camera.
   *
   * @param context the context
   * @param windowManager the window manager
   */
  public void openCamera(Context context, WindowManager windowManager) {
    cameraGrabber = new CameraGrabber(Camera.CameraInfo.CAMERA_FACING_FRONT);

    switch (ARHelper.getScreenOrientation(windowManager)) {
      case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
        cameraGrabber.setScreenOrientation(90);
        break;
      case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
        cameraGrabber.setScreenOrientation(270);
        break;
      default:
        cameraGrabber.setScreenOrientation(0);
        break;
    }

    cameraGrabber.setResolutionPreset(CameraResolutionPreset.P1280x720);

    cameraGrabber.initCamera(new CameraGrabberListener() {
      @Override
      public void onCameraInitialized() {
        cameraGrabber.setFrameReceiver(deepAR);
        cameraGrabber.startPreview();
      }

      @Override
      public void onCameraError(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Camera error");
        builder.setMessage(errorMsg);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
      }
    });
  }

  /**
   * Initializes surface for the AR engine
   *
   * @param arView the AR view
   * @param deepAR the AR engine instance
   */
  private void initializeAREngine(SurfaceView arView, DeepAR deepAR) {

    try {

      arView.setOnClickListener(view -> deepAR.onClick());

      arView.getHolder().addCallback(new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

          if (deepAR != null) {
            deepAR.setRenderSurface(holder.getSurface(), width, height);
          }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

          if (deepAR != null) {
            deepAR.setRenderSurface(null, 0, 0);
          }
        }
      });
      // Surface might already be initialized, so we force the call to onSurfaceChanged
      arView.setVisibility(View.GONE);
      arView.setVisibility(View.VISIBLE);
    } catch (Exception e) {

      e.printStackTrace();
    }
  }

  /**
   * Cleanup the surface from the ar engine.
   */
  public void cleanup() {
    if (cameraGrabber != null) {
      cameraGrabber.setFrameReceiver(null);
      cameraGrabber.stopPreview();
      cameraGrabber.releaseCamera();
      cameraGrabber = null;
      deepAR.setRenderSurface(null, 0, 0);
    }
  }
}
