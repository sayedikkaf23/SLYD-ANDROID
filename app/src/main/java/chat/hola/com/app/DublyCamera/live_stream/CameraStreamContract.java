package chat.hola.com.app.DublyCamera.live_stream;

/**
 * Created by moda on 11/20/2018.
 */
public interface CameraStreamContract {
    interface Presenter {
        void startLiveBroadcastApi(String streamId, String streamType, String thumbnail, String streamName);
    }

    interface View {

        void onError(String message);

        void showAlert(String message);

        void onSuccess(String streamId);
    }
}
