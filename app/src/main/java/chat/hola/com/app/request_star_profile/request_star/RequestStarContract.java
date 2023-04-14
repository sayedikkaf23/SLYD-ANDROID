package chat.hola.com.app.request_star_profile.request_star;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.request_star_profile.model.Data;

public interface RequestStarContract {

    interface View extends BaseView{

        void launchCamera(Intent intent);

        void showSnackMsg(int string_61);

        void launchImagePicker(Intent intent);

        void launchCropImage(Uri data);

        void showProgress(boolean b);

        void imageUploadSuccess(String imgUrl);

        void requestDone();

        void showStarData(Profile body);

        void updateStatus(Data data);
    }

    interface Presenter extends BasePresenter<RequestStarContract.View>{

        void loadProfileData();

        void launchCamera(PackageManager packageManager);

        void launchImagePicker();

        void parseSelectedImage(int requestCode, int resultCode, Intent data);

        void parseCapturedImage(int requestCode, int resultCode, Intent data);

        void parseCropedImage(int requestCode, int resultCode, Intent data);

        void makeStarProfileRequest(String categorieId, String starUserEmail, String starUserPhoneNumber, String starUserIdProof, String starUserKnownBy, String description);

        void getStarStatus();
    }
}
