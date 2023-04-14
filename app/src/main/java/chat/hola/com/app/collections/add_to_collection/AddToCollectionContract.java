package chat.hola.com.app.collections.add_to_collection;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import chat.hola.com.app.collections.model.PostData;

public interface AddToCollectionContract {

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
         * When post added successfully to collection this method calls.
         */
        void successfullyAdded();

        /**
         * It will show empty data UI.
         */
        void showEmpty();

        /**
         * new collection created successfully.
         * @param body
         */
        void createdSuccess(CreateCollectionResponse body);
    }

    interface Presenter extends BasePresenter<View>{

        /**
         * For get the post from API which are not added to this collection.
         * @param collectionId
         * @param offset
         * @param limit
         */
        void getCollectionPost(String collectionId, int offset, int limit);

        /**
         * Add posts to Collection by ids.
         * @param collectionId
         * @param selectedPostIds
         */
        void addPostToCollection(String collectionId, List<String> selectedPostIds);

        /**
         * When user creating new collection need to show all saved post.
         * @param offset
         * @param limit
         */
        void getAllPost(int offset, int limit);

        /**
         * Used to create new collection with posts and name.
         * @param collectionName
         * @param coverImage
         * @param selectedPostIds
         */
        void createCollection(String collectionName, String coverImage, List<String> selectedPostIds);
    }
}
