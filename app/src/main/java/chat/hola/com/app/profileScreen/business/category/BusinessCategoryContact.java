package chat.hola.com.app.profileScreen.business.category;


import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.model.BusinessCategory;

/**
 * <h1>BusinessCategoryContact</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 16 August 2019
 */
public interface BusinessCategoryContact {

    interface View extends BaseView {

        /**
         * Data loaded successfully
         */
        void success();

        /**
         * Failed to load data
         */
        void failed();

        /**
         * Pass the selected category data to {@link chat.hola.com.app.profileScreen.business.form.BusinessProfileFormActivity}
         *
         * @param category : business category details
         */

        void selectCategory(BusinessCategory.Data category);
    }

    interface Presenter {

        /**
         * Get the list of business categories data
         */
        void businessCategories();

        /**
         * @return : instance of Clicklistner
         */
        BusinessCategoryAdapter.ClickListner getPresenter();
    }
}
