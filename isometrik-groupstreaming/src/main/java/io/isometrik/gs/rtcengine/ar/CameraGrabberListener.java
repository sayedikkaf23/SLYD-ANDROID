package io.isometrik.gs.rtcengine.ar;

/**
 * The interface camera grabber listener for callbacks on camera initialized or on error.
 */
public interface CameraGrabberListener {
  /**
   * On camera initialized.
   */
  void onCameraInitialized();

  /**
   * On camera error.
   *
   * @param errorMsg the error msg
   */
  void onCameraError(String errorMsg);
}
