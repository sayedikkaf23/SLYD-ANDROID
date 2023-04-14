package chat.hola.com.app.user_verification.phone;


import java.io.File;
import java.util.Map;

import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.Register;

/**
 * <h1>VerifyNumberContract</h1>
 * <p>Contains PricePresenter and View methods</p>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 22 August 2019
 */
public interface VerifyNumberContract {

    interface View {
        void showMessage(String message);

        void showProgress(boolean show);

        void numberNotRegistered(String message);

        void openSuccessDialog();

        void newPassword();

        void registered(Login.LoginResponse response, boolean isSignUp);

        void startTimer();

        void businessPhoneVerified();

        void showNext();
    }

    interface Presenter {

        void makeReqToNumber(Map<String,Object> params);

        void signUp(Register register, UploadFileAmazonS3 uploadFileAmazonS3, File mFileTemp);

        void amazonUpload(UploadFileAmazonS3 amazonS3, File mFileTemp, String userId, Login response);

        void verifyPhoneNumber(Login login, int move, Register register, UploadFileAmazonS3 uploadFileAmazonS3, File mFile);

        void verifyBusinessPhoneNumber(String toString, String countryCode, String phoneNumber);
    }
}
