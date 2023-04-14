package chat.hola.com.app.profileScreen.discover.facebook;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.discover.facebook.apiModel.Data;
import chat.hola.com.app.profileScreen.discover.follow.Follow;

/**
 * <h>FacebookFragment</h>
 * @author 3Embed.
 * @since 02/03/18.
 */

public interface FacebookContract {

    interface View extends BaseView {

        void applyFont();

        void showFollows(Follow follow);

        void initPostRecycler();

        void setFbVisibility(boolean show);

        void setContactVisibility(boolean show);

        void showFbContacts(ArrayList<Data> dataList);

        void followedAll(boolean flag);

        void showFbContactUi(boolean show);

        void isDataLoading(boolean isLoading);

        void showEmptyUi(boolean show);
    }

    interface Presenter extends BasePresenter<FacebookContract.View> {
        void init();

        void loadFollows();

        void setFbVisibility(boolean show);

        void setContactVisibility(boolean show);

        void fbLogin();

        void returnFbResult(int RequestCode, int ResultCode, Intent data);

        void follow(String followerId);

        void unFollow(String followerId);

        void followAll(List<String> strings);

        void fetchFriendList();

        void checkForFbLogin();
    }
}
