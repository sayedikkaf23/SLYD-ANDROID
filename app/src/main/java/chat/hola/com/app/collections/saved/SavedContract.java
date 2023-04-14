package chat.hola.com.app.collections.saved;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.CollectionData;

public interface SavedContract {

    interface View extends BaseView{
        /**
         * Show progress in view
         * @param b
         */
        void showProgress(boolean b);

        /**
         * On successfully get list of collections from API.
         * @param data
         */
        void onSuccess(List<CollectionData> data);

        /**
         * It will show empty UI.
         */
        void showEmpty();

    }

    interface Presenter extends BasePresenter<View>{

        /**
         * Get All collection From API
         * @param offSet
         * @param limit
         */
        void getCollections(int offSet, int limit);
    }

}
