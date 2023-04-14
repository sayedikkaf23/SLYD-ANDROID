package chat.hola.com.app.DublyCamera.deepar.ar;

import ai.deepar.ar.ARErrorType;
import ai.deepar.ar.AREventListener;

import android.graphics.Bitmap;
import android.media.Image;

/**
 * The listener to listen for AR engine events when AR engine is initialized, face visibility change
 * ,error in apply effect etc.
 */
public class ARInitializeListener implements AREventListener {

    private DeeparCallbacks deeparCallbacks;

    public ARInitializeListener(DeeparCallbacks deeparCallbacks) {

        this.deeparCallbacks = deeparCallbacks;
    }

    @Override
    public void screenshotTaken(Bitmap bitmap) {
        if (deeparCallbacks != null) {
            deeparCallbacks.arImageCaptured(bitmap);
        }
    }

    @Override
    public void videoRecordingStarted() {
        if (deeparCallbacks != null) {
            deeparCallbacks.arVideoRecordingStarted();
        }
    }

    @Override
    public void videoRecordingFinished() {
        if (deeparCallbacks != null) {
            deeparCallbacks.arVideoRecordingFinished();
        }
    }

    @Override
    public void videoRecordingFailed() {
        if (deeparCallbacks != null) {
            deeparCallbacks.arVideoRecordingFailed();
        }
    }

    @Override
    public void videoRecordingPrepared() {
        if (deeparCallbacks != null) {
            deeparCallbacks.arVideoRecordingPrepared();
        }
    }

    @Override
    public void shutdownFinished() {

    }

    @Override
    public void initialized() {

    }

    @Override
    public void faceVisibilityChanged(boolean b) {

    }

    @Override
    public void imageVisibilityChanged(String s, boolean b) {

    }


    @Override
    public void effectSwitched(String s) {

    }

    @Override
    public void error(ARErrorType arErrorType, String s) {
    }

    @Override
    public void frameAvailable(Image image) {

    }
}
