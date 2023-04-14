package chat.hola.com.app.profileScreen.tag;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostUpdateData;

/**
 * Created by ankit on 23/2/18.
 */

public interface TagContract {

    interface View extends BaseView {
        void setupRecyclerView();

        void showData(List<Data> profilePostRequests, boolean isFirst);

        void isLoading(boolean flag);

        void showEmptyUi(boolean show);

        void noData();

        void updateSavedOnObserve(PostUpdateData postUpdateData);

        void viewAppear();
    }

    interface Presenter extends BasePresenter<TagContract.View> {
        void init();

        void loadData(int skip, int limit, boolean isLiked);

        void loadMemberData(String userId, int skip, int limit, boolean isLiked);

        void like(String postId, SessionManager sessionManager);

        void unlike(String postId);
    }
}
