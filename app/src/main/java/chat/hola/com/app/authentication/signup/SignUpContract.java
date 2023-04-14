package chat.hola.com.app.authentication.signup;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.Register;
import retrofit2.Response;

public interface SignUpContract {

    interface View {
        void registered(Login.LoginResponse response);

        void message(String message);

        void completed();

        void setProfilePic(String url);

        void enableSave(boolean enable);

        void showProgress(boolean show);

        void verifyPhone();

        void showLoader();

        void hideLoader();

        void socialLoginSuccess(Login.LoginResponse response);

        void socialIdNotRegistered(FirebaseUser user, String loginType);

        void emailalreadyRegistered(String email);

        void emailNotRegistered(String email_number_is_not_registered);

        void userNamealreadyRegistered(String userName);

        void userNameNotRegistered(String userName_is_already_in_use);

        void numberAlreadyRegistered(String phoneNumber);

        void numberNotRegistered(String mobile_number_is_not_registered);
    }

    interface Presenter {
        void register(Context context, Register register, UploadFileAmazonS3 amazonS3, File mFileTemp);

        void amazonUpload(Context context, UploadFileAmazonS3 amazonS3, File mFileTemp, String phone, String countryCode, String userId, String userName, Response<Login> response);

        void validate(Register register);

        void signUp(Context context, Register register, UploadFileAmazonS3 uploadFileAmazonS3, String profilePic);

        void socialSignIn(String token, String language, FirebaseUser user, String facebook);

        void verifyIsEmailRegistered(String userName);

        void verifyIsUserNameRegistered(String email);

        void verifyIsMobileRegistered(String phoneNumber, String countryCode);
    }
}
