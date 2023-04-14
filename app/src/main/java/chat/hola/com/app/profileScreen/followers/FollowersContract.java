package chat.hola.com.app.profileScreen.followers;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.followers.Model.Data;

/**
 * Created by ankit on 19/3/18.
 */

public class FollowersContract {
    interface View extends BaseView{

        void applyFont();

        void recyclerViewSetup();

        void showFollowers(List<Data> followers);

        void showFollowees(List<Data> followees);

        void invalidateBtn(int index, boolean isFollowing);

        void isDataLoading(boolean show);

        void clearList(boolean b);

        void showEmpty();

        void showSubscribers(List<Data> data);
    }

    interface Presenter {

        void init();

        void loadFollowersData(int skip, int limit, String userId);

        void loadFolloweesData(int skip, int limit, String userId);

        void follow(String followingId);

        void unFollow(String followingId);

        void viewers(String postId, int i, int page_size);

        void searchViewers(String postId, int i, int page_size, String searchText);

        void loadSubscribersData(int skip, int limit, String userId);
    }
}
