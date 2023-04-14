package chat.hola.com.app.collections.edit_collection;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

public interface EditCollectionContract  {

    interface View extends BaseView{

        /**
         * Used to show progress view.
         * @param show
         */
        void showProgress(boolean show);

        /**
         * Successfully edited colletion
         */
        void collectionEdited();

        /**
         * Successfully deleted Collection.
         */
        void deletedCollection();
    }

    interface Presenter extends BasePresenter<View>{

        /**
         * Used To edit colletion through API.
         * @param collectionId
         * @param collectionName
         * @param coverImage
         */
        void editCollection(String collectionId, String collectionName, String coverImage);

        /**
         * used To delete collection
         * @param collectionId
         */
        void deleteCollection(String collectionId);
    }
}
