package chat.hola.com.app.authentication.login;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import chat.hola.com.app.models.Login;

public interface LoginContract {

    interface View {
        void message(String message);

        void loginSuccessfully();

        void numberNotRegistered(String error);

        void emailNotRegistered(String error);

        void blocked(String errorMessage);

        void verifyOtp(String phoneNumber, String countryCode);

        void enterEmailPswd(String emailId);

        void showLoader();

        void hideLoader();

        void forgotPassword();

        void socialLoginSuccess(Login.LoginResponse response);

        void socialIdNotRegistered(FirebaseUser user, String loginType);
    }

    interface Presenter {
        void loginWithEmailId(String userName, String password, String appVersion, String country);

        void loginByPassword(String countryCode, String phoneNumber, String password, String appVersion, String country);

        void loginByOtp(Login login);

        void requestOtp(String phoneNumber, String countryCode);

        void verifyIsMobileRegistered(String phone, String countryCode, boolean isForgotPassword);

        void socialSignIn(String token, String language, FirebaseUser user, String google);

        void verifyIsEmailRegistered(String email, boolean isForgotPassword);

    }
}
