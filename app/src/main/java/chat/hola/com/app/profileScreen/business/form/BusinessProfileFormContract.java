package chat.hola.com.app.profileScreen.business.form;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Map;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;

/**
 * <h1>BusinessProfileFormContract</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 08 July 2019
 */
public interface BusinessProfileFormContract {

    interface View extends BaseView {
        /**
         * Show or hides the progress
         *
         * @param show : true : show progress, false : hides progress
         */
        void showProgress(boolean show);

        /**
         * redirects to the profile
         */
        void profile();

        /**
         * redirect to verify phone number
         */
        void verifyMobile();

        /**
         * redirect to verify email
         */
        void verifyEmailAddress();

        void launchCamera(boolean isProfile);

        void launchImagePicker(boolean isProfile);

        void setProfilePic(String imageUrl);

        void finishActivity(boolean b);

        void setProfileImage(Bitmap bitmap);

        void setCover(Bitmap bitmapToUpload);

        void launchCropImage(Uri imageUri);

        void userNameAvailable(boolean b);

        void emailAvailable(boolean b);

        void phoneAvailable(boolean b);
    }

    interface Presenter {

        /**
         * register and apply for business profile
         *
         * @param params : required data
         */
        void applyBusinessProfile(Map<String, Object> params);

        /**
         * Sends verification code to given email address
         *
         * @param email : email address
         */
        void businessEmailVerificationCode(String email);

        /**
         * Sends verification code to given phone number
         *
         * @param countryCode : country code
         * @param phone       : phone number
         */
        void businessPhoneVerificationCode(String countryCode, String phone);

        void launchCamera(PackageManager packageManager, boolean isProfile);

        void launchImagePicker(boolean isProfile);

        void parseSelectedImage(int requestCode, int resultCode, Intent data);

        void parseCapturedImage(int requestCode, int resultCode, Intent data);

        void parseCropedImage(int requestCode, int resultCode, Intent data, boolean isProfile);

        void verifyIsEmailRegistered(String email);

        void verifyIsUserNameRegistered(String userName);

        void verifyIsPhoneRegistered(String phone, String countryCode);
    }
}
