package chat.hola.com.app.profileScreen.profile_story;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.stories.model.StoryPost;

public interface ProfileStoryContract {

    interface View extends BaseView{

        void setupRecyclerView();

        void showData(List<StoryPost> storyPosts, boolean isFirst);

        void isLoading(boolean flag);

        void showEmptyUi(boolean show);

        void noData();

    }

    interface Presenter extends BasePresenter<ProfileStoryContract.View>{
        void init();

        void loadData(int skip, int limit, String userId);

        void callApiOnScroll(String userId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        //void loadMemberData(String userId, int skip, int limit,  boolean isLiked);

        //void like(String postId);

        //void unlike(String postId);
    }
}
