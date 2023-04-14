package chat.hola.com.app.collections.create_collection;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.CreateCollectionResponse;

public interface CreateCollectionContract {

    interface View extends BaseView{

        /**
         * When collection created successfully it will call
         * @param body
         */
        void createdSuccess(CreateCollectionResponse body);

        /**
         * To show progress view
         * @param show
         */
        void showProgress(boolean show);
    }

    interface Presenter extends BasePresenter<View>{

        /**
         * Create collection to API.
         * @param trim
         */
        void createCollection(String trim);
    }
}
