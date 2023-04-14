package chat.hola.com.app.profileScreen.editProfile.editDetail;

import java.util.Map;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;


public interface EditDetailContract {

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

        /**
         * This performs the action once successfully update the detail
         */
        void success();

        /**
         * Show message after sending verification mail
         *
         * @param message : message text
         */
        void businessEmailVerificationSent(String message);

        /**
         * Show message after sending verification code and redirect to verification code screen
         *
         * @param otpId : verification code id
         */
        void businessPhoneVerificationSent(String otpId);

        void emailAvailable(boolean b);

        void emailVerificationSent(String message);

        void phoneVerificationSent(String otpId);

        void phoneAvailable(boolean b);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * To update the user details
         *
         * @param map : user details
         */
        void updateDetail(Map<String, Object> map);

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
         * @param isBusiness
         */
        void verifyIsEmailRegistered(String email, boolean isBusiness);

        /**
         * To check given phone is available or not
         *
         * @param phone       : new phone number
         * @param countryCode : county code
         */
        void verifyIsPhoneRegistered(String phone, String countryCode, boolean isBusiness);

        /**
         * To send verification link to email to verify business email
         *
         * @param email : email to verify
         */
        void verifyBusinessEmail(String email);

        /**
         * To send verification code to phone to verify business phone
         *
         * @param countryCode : country code
         * @param mobile      : mobile number without country code
         */
        void verifyBusinessPhone(String countryCode, String mobile);

        void verifyEmail(String email);

        void verifyPhone(String countryCode, String mobile);
    }
}
