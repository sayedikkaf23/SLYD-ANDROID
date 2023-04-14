package chat.hola.com.app.live_stream.Home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Created by moda on 11/20/2018.
 */
public interface StreamingMainActivityPresenterContract {
    interface presenterMain {

//        void checkForNetwork(boolean isConnected);

        void onFragmentTransition(String TAG, FragmentManager fragmentManager, Fragment fragmentService
                , Fragment fragmentProject, int frameId);

        void startLiveBroadcastApi(String streamId, String streamType, String thumbnail, String streamName);
    }

    interface ViewMain {

        void onError(String message);

        void showAlert(String message);

        void onSuccess(String streamId);
    }
}
