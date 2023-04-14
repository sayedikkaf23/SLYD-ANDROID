package chat.hola.com.app.DublyCamera.deepar.ar;

import android.graphics.Bitmap;

public interface DeeparCallbacks {

  void arVideoRecordingStarted();

  void arVideoRecordingFinished();

  void arVideoRecordingFailed();

  void arVideoRecordingPrepared();

  void arImageCaptured(Bitmap bitmap);
}
