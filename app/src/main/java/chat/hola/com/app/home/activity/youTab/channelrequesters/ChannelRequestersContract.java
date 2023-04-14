package chat.hola.com.app.home.activity.youTab.channelrequesters;

import android.content.Intent;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.activity.youTab.model.RequestedChannels;
import chat.hola.com.app.home.activity.youTab.channelrequesters.model.ClickListner;

/**
 * <h1>ChannelRequestersContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public interface ChannelRequestersContract {

    interface View extends BaseView {
        void requestAccepted(boolean flag);

        void callChannel(String channelId);

        void callUser(String userId);
    }

    interface Presenter {

        void setData(List<RequestedChannels> data);

        void requestAction(String channelId);

        ClickListner getPresenter();

        void loadData(Intent intent);

        void getData();
    }
}
