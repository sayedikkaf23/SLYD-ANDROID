package io.isometrik.gs.rtcengine.ar.capture;

import android.graphics.Bitmap;

/**
 * The interface Image capture callbacks to inform on successful image capture.
 */
public interface ImageCaptureCallbacks {

  /**
   * Image captured.
   *
   * @param bitmap the bitmap captured
   */
  void imageCaptured(Bitmap bitmap);
}
