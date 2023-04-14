package chat.hola.com.app.settings;

import android.content.Context;

import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>SettingsContract</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 24/2/18.
 */

public interface SettingsContract {

    interface View extends BaseView {

        /**
         * <p>to apply fonts</p>
         */
        void applyFont();

        void logout();

        void delete();

        void gotoProfile(boolean isBusiness);

        void showLoader();

        void hideLoader();

        void onSuccessSubscriptionAdded();
    }

    interface Presenter {
        /**
         * <p>to initialize</p>
         */
        void init();

        void logout();

        void deleteAccount(Context context);

        void switchBusiness(boolean isBusiness, String businessCategoryId);

        void postSubscription(double amount, double youAmount, double appAmount);
    }

}
