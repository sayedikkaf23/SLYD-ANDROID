package chat.hola.com.app.live_stream.Home.live_users;

import java.util.ArrayList;

import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;

/**
 * <h1>LiveUsersContract</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0.
 * @since 26/7/2019.
 */

public interface LiveUsersContract {

    interface Presenter {

        void callLiveBroadcaster(int offset, int limit);

        void allStreamDataRxJava();

        void follow(String userId);

        void unfollow(String userId);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void getWalletBalance();

        /**
         * Checks that is user following this streamer
         *
         * @param dataStream : a streamer's data
         */
        void following(AllStreamsData dataStream);
    }

    interface View {

        void liveBroadCasterData(ArrayList<AllStreamsData> streams);

        void onAllStreamDataReceived(AllStreamsData messages);

        /**
         * Add one data to the live users list
         *
         * @param dataStream : a streamer's data
         */
        void setData(AllStreamsData dataStream);
    }
}
