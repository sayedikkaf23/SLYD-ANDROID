package chat.hola.com.app.profileScreen.editProfile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileBody;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;

/**
 * <h>EditProfileContract</h>
 * <p>
 *
 * @author 3Embed.
 * @since 22/2/18.
 */

public interface EditProfileContract {

    interface View extends BaseView {
        void applyFont();

        void finishActivity(boolean success,boolean imageUpdated);

        void showProgress(boolean b);

        void setEmail(String email);

        void launchCamera(Intent intent, boolean b);

        void showSnackMsg(int msgId);

        void launchImagePicker(Intent intent, boolean b);

        void launchCropImage(Uri data);

        void setProfileImage(Bitmap bitmap);

        void setCover(Bitmap bitmap);

        void saveEnable(boolean enable);

        void setProfilePic(String profilePath);

        void verifyEmailAddress();

        void verifyMobile();

        void showProfileData(Profile body);
    }

    interface Presenter {

        void init();

        void initUpdateProfile(EditProfileBody editProfileBody, Data profileData, UploadFileAmazonS3 amazonS3, boolean isPicChange, boolean isBusiness);

        void launchCamera(PackageManager packageManager, boolean isProfile);

        void launchImagePicker(boolean isProfile);

        void parseSelectedImage(int requestCode, int resultCode, Intent data);

        void parseCapturedImage(int requestCode, int resultCode, Intent data);

        void parseCropedImage(int requestCode, int resultCode, Intent data, boolean b);

        void businessEmailVerificationCode(String email);

        void businessPhoneVerificationCode(String businessCountryCode, String phone);
    }
}
