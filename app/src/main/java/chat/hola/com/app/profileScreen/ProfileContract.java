package chat.hola.com.app.profileScreen;

import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

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

        void setupViewPager();

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

        void showLoader();

        void hideLoader();

        void moveNext(Integer verificationStatus);
        void onSuccessSubscribe(boolean isChecked);
        void insufficientBalance();

        void showCoinBalance(WalletResponse.Data.Wallet wallet);
    }

    interface Presenter {
        void init();

        void loadProfileData();

        void loadMemberData(String userId);

        void follow(String followingId);

        void unfollow(String followingId);

        void launchCustomCamera();

        void launchGallery();

        void launchImagePicker();

        void parseMedia(int requestCode,int resultCode,Intent data);

        void parseSelectedImage(Uri uri, String picturePath);

        void parseCropedImage(int requestCode, int resultCode, Intent data);

        void getWalletBalance();

        void kycVerification();
        void subscribeStarUser(boolean isChecked, String id);
    }
}
