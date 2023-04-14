package chat.hola.com.app.category;

import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>CategoryContract</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 28/3/18
 */

public interface CategoryContract {

    interface View extends BaseView {

        /**
         * applies fonts
         */
        void applyFont();

        /**
         * setups recycler view to display list of categories
         */
        void recyclerSetup();

        /**
         * show loader according to flag
         *
         * @param show : returns true or false
         */
        void isLoadingData(boolean show);
    }

    interface Presenter {

        /**
         * initialized the ui setup
         */
        void init();

        /**
         * gets the list of category data
         */
        void getCategory(String id);
    }
}
