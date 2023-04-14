package chat.hola.com.app.home.activity.youTab.followrequest;

import android.content.Intent;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>ChannelRequestersContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public interface FollowRequestContract {

    interface View extends BaseView {
        void callUser(String userId);
    }

    interface Presenter {

        void setData(List<ReuestData> data);

        void requestAction(String channelId);

        ItemClickListner getPresenter();

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void loadData(Intent intent);

        void getData(int offset, int limit);
    }
}
