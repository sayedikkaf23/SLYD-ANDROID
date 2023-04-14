package chat.hola.com.app.profileScreen.channel;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;

/**
 * <h>ChannelContract</h>
 *
 * @author 3Embed.
 * @since 23/2/18.
 */

public interface ChannelContract {

    interface View extends BaseView {
        void setupRecyclerView();

        void showChannelData(ArrayList<ChannelData> channelData);

        void isLoading(boolean flag);

        void showEmptyUi(boolean show);

        void noData();

        void channelDeleted();
    }

    interface Presenter extends BasePresenter<ChannelContract.View> {
        void init();

        void getChannelData(int skip, int limit, String userId);

        void deleteChannel(String channelId);

        void subscribeObservers();
    }

}
