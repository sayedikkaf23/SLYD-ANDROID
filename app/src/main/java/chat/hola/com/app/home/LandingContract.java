package chat.hola.com.app.home;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.calling.model.Data;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.models.WalletResponse;

/**
 * <h1>LandingContract</h1>
 *
 * @author 3Embed
 * @since 21/2/18.
 */

public interface LandingContract {

    interface View extends BaseView {
        void removeShift(BottomNavigationView view, int position);

        boolean selectFragment(MenuItem item);

        void pushFragment(Fragment fragment);

        void hideActionBar();

        void visibleActionBar();

        void launchImagePicker(Intent data);

        void launchCropImage(Uri data);

        void showSnackMsg(int msgId);

        void launchPostActivity(CameraOutputModel model);

        void intenetStatusChanged(boolean isConnected);

        void profilepic(chat.hola.com.app.profileScreen.model.Data data, String profilePic, boolean isStar, String userName, boolean isBusinessProfileActive, boolean businessProfile);

        void showBalance(WalletResponse.Data.Wallet data);

        void showCoinBalance(WalletResponse.Data.Wallet coinWallet);

        void showLoader();

        void hideLoader();

        void gotoWalletDashboard(Integer verificationStatus);

        void showPendingCalls(Data response);

        void guestTokenFetched();
    }

    interface Presenter {
        void instagramShare(String type, String path);

        LandingActivity getActivity();

        void parseMedia(int requestCode, int resultCode, Intent data);

        void parseSelectedImage(Uri uri, String picturePath);

        void parseCropedImage(int requestCode, int resultCode, Intent data);

        void launchImagePicker();

        void friends();

        void getUserProfile();

        void kycVerification();

        void getWalletBalance();

        void getPendingCalls();

        void patchLatLong(String apiToken, double latitude, double longitude);

        void getGuestToken();
    }
}
