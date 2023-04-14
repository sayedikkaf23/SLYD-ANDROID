package chat.hola.com.app.home.activity.youTab;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.activity.youTab.followrequest.ReuestData;
import chat.hola.com.app.home.activity.youTab.model.Data;
import chat.hola.com.app.home.activity.youTab.model.RequestedChannels;
import chat.hola.com.app.home.activity.youTab.model.YouModel;

/**
 * @author 3Embed.
 * @since 21/2/18.
 */

public interface YouContract {

    interface View extends BaseView {

        void showYouActivity(ArrayList<Data> dataList, boolean b);

        void isDataLoading(boolean show);

        void applyFont();

        void showErrorLayout(boolean show);

        void channelRequest(List<RequestedChannels> response);

        void followRequest(List<ReuestData> response, Integer requests);

        void showEmptyRequest(boolean isEmpty);
    }

    interface Presenter extends BasePresenter<View> {

        void loadYouData(int offset, int limit);

        void setModel(YouModel model);

        void follow(String userId);

        void unFollow(String userId);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);
    }

}
