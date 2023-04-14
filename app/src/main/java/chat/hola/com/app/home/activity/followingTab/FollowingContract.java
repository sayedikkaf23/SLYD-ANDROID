package chat.hola.com.app.home.activity.followingTab;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.activity.followingTab.model.Following;

/**
 * @author 3Embed.
 * @since 21/2/18.
 */

public interface FollowingContract {

    interface View extends BaseView {

        void initPostRecycler();

        void showFollowings(ArrayList<Following> followings, boolean clear);

        void onUserClicked(String userId);

        void onMediaClick(int position, android.view.View view);

        void loading(boolean isLoading);
        void noData(boolean isFirstPage);
    }

    interface Presenter extends BasePresenter<View> {

        void init();

        void loadFollowing(int skip, int limit);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);
    }
}
