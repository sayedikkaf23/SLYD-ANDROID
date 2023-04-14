package chat.hola.com.app.collections.collection;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.PostData;

public interface CollectionContract {

    interface View extends BaseView{

        /**
         * It will show progress view
         * @param show
         */
        void showProgress(boolean show);

        /**
         * Update view with collection post.
         * @param data
         */
        void collectionPostResponse(List<PostData> data);

        /**
         * It will shows empty UI.
         */
        void showEmpty();
    }

    interface Presenter extends BasePresenter<View>{

        /**
         * Get All Bookmarked/Saved post of user.
         * @param offset
         * @param limit
         */
        void getAllPost(int offset, int limit);

        /**
         * Get particular collection post.
         * @param collectionId
         * @param offset
         * @param limit
         */
        void getCollectionPost(String collectionId, int offset, int limit);
    }
}
