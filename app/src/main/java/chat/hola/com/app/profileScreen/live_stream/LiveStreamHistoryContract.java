package chat.hola.com.app.profileScreen.live_stream;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.stories.model.StoryPost;

public interface LiveStreamHistoryContract {

    interface View extends BaseView {

        void setupRecyclerView();

        void showData(List<StoryPost> storyPosts, boolean isFirst);

        void isLoading(boolean flag);

        void showEmptyUi(boolean show);

        void noData();

        void streamDeleteSuccess(boolean b);
    }

    interface Presenter extends BasePresenter<LiveStreamHistoryContract.View> {
        void init();

        void loadData(String userId, int skip, int limit);

        void callApiOnScroll(String userId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void deleteStream(String streamId);
    }
}
