package chat.hola.com.app.search.channel;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.search.channel.module.Channels;

/**
 * Created by ankit on 24/2/18.
 */

public interface ChannelContract {

    interface View extends BaseView {

        void showData(List<Channels> data);

        void noData();
    }

    interface Presenter extends BasePresenter<ChannelContract.View> {
        void search(CharSequence charSequence);

        void subscribeChannel(String channelId);

        void unSubscribeChannel(String channelId);
    }
}
