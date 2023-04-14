package chat.hola.com.app.profileScreen.business.configuration;


import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>BusinessConfigurationContract</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 08 July 2019
 */
public interface BusinessConfigurationContract {

    interface View {
        /**
         * Ue to display message
         *
         * @param message : message
         */
        void showMessage(String message);
    }

    interface Presenter {

        /**
         * Allow user to call or send email
         *
         * @param enableEmail : true : allow to send email,  false: not allow
         * @param enableCall: true : allow to call on business number, false : not allow
         */
        void configureBusiness(boolean enableEmail, boolean enableCall, String businessCategoryId);
    }
}
