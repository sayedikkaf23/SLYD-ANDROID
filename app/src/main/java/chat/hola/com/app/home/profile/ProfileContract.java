package chat.hola.com.app.home.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceData;

/**
 * Created by ankit on 22/2/18.
 */

public interface ProfileContract {

    interface View extends BaseView {
        void applyFont();

        void isLoading(boolean flag);

        void showProfileData(Profile profile);

        void isFollowing(boolean flag);

        void launchCustomCamera();

        void checkReadImage();

        void launchImagePicker(Intent intent);

        void launchCropImage(Uri uri);

        void showSnackMsg(int msgId);

        void launchPostActivity(CameraOutputModel model);

        void addToReportList(ArrayList<String> data);

        void addToBlockList(ArrayList<String> data);

        void block(boolean block);

        void unfriend();

        void showBalance(WalletResponse.Data.Wallet data);

        void noProfile(String message);

        void moveNext(Integer verificationStatus);

        void showCoinBalance(WalletResponse.Data.Wallet wallet);

        void logout();

        void showLoader();

        void hideLoader();
    }

    interface Presenter extends BasePresenter<View> {

        void init();

        void loadProfileData();

        void loadMemberData(String userId);

        void follow(String followingId);

        void unfollow(String followingId);

        void launchCustomCamera();

        void launchGallery();

        void launchImagePicker();

        void parseMedia(int requestCode, int resultCode, Intent data);

        void parseSelectedImage(Uri uri, String picturePath);

        void parseCropedImage(int requestCode, int resultCode, Intent data);

        void getWalletBalance();

        void logout(Context context);

    }
}
