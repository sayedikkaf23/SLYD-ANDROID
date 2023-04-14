package chat.hola.com.app.live_stream.Home;

import java.util.ArrayList;

import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;

/**
 * Created by moda on 2/18/2019.
 */
public interface BroadcastersPresenterContract {
    interface BroadcastPresenter extends BasePresenter {
        void callLiveBroadcaster();

        void allStreamDataRxJava();
    }

    interface BroadcastView {
        void liveBroadCasterData(ArrayList<AllStreamsData> streams);

        void showAlert(String message);

        void onAllStreamDataReceived(AllStreamsData messages);
    }


}
