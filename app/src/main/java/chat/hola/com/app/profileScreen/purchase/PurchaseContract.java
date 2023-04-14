package chat.hola.com.app.profileScreen.purchase;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostUpdateData;

/**
 * Created by ankit on 23/2/18.
 */

public interface PurchaseContract {

    interface View extends BaseView {
        void setupRecyclerView();

        void showData(List<Data> profilePostRequests, boolean isFirst);

        void isLoading(boolean flag);

        void showEmptyUi(boolean show);

        void noData();

        void loadData();

        /**
         * <p>Used to set updated saved status of post</p>
         *
         * @param saveData
         * */
        void updateSavedOnObserve(PostUpdateData saveData);
    }

    interface Presenter extends BasePresenter<View> {
        void init();

        void loadData(int skip, int limit, boolean isLiked);

        void like(String postId, SessionManager sessionManager);

        void unlike(String postId);
    }
}
