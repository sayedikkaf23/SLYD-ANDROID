package chat.hola.com.app.profileScreen.addChannel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.post.model.CategoryData;

/**
 * Created by ankit on 22/2/18.
 */

public interface AddChannelContract {

    interface View extends BaseView {

        void applyFont();

        void showCategories(List<CategoryData> categories);

        void showProgress(boolean show);

        void finishActivity();

        void launchCamera(Intent intent);

        void showSnackMsg(int msgId);

        void launchImagePicker(Intent intent);

        void launchCropImage(Uri data);

        void setProfileImage(Bitmap bitmap);
    }

    interface Presenter {

        void init();

        void addChannel(@NonNull String channelName, String channelDesc, Boolean isPrivate, String categoryId);

        void loadCategories();

        void launchCamera(PackageManager packageManager);

        void launchImagePicker();

        void parseSelectedImage(int requestCode, int resultCode, Intent data);

        void parseCapturedImage(int requestCode, int resultCode, Intent data);

        void parseCropedImage(int requestCode, int resultCode, Intent data);

    }
}
