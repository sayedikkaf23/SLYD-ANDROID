package chat.hola.com.app.profileScreen.business.form.verify;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;


public interface BusinessFormVerifyContract {

    interface View extends BaseView {

        /**
         * Perform action based on user name availability
         *
         * @param b : true= available, false= not available
         */
        void userNameAvailable(boolean b);

        /**
         * Perform action based on phone umber availability
         *
         * @param b : true= available, false= not available
         */
        void businessPhoneAvailable(boolean b);

        /**
         * Perform action based on email availability
         *
         * @param b : true= available, false= not available
         */
        void businessEmailAvailable(boolean b);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * To check given username is available or not
         *
         * @param userName : new user name
         */
        void verifyIsUserNameRegistered(String userName);

        /**
         * To check given email is available or not
         *
         * @param email : new email address
         */
        void verifyIsEmailRegistered(String email);

        /**
         * To check given phone is available or not
         *
         * @param phone       : new phone number
         * @param countryCode : county code
         */
        void verifyIsPhoneRegistered(String phone, String countryCode);
    }
}
