package chat.hola.com.app.home.stories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryPost;

/**
 * <h1>StoriesContract<h1/>
 *
 * @author 3Embed
 * @version 1.0
 * @since 4/24/18
 */

public interface StoriesContract {

    interface View extends BaseView {
        void myStories(String url, String timeStamp);

        void launchImagePicker(Intent data);

        void launchCropImage(Uri uri);

        void launchActivity(CameraOutputModel model);

        void preview(int position);

        void onComplete(Bundle bundle);

        void isDataAvailable(boolean empty);
    }

    interface Presenter extends BasePresenter<View> {
        void myStories();

        void stories();

        void parseMedia(int resultCode, Intent data);

        void parseSelectedImage(Uri uri, String picturePath);

        void parseCropedImage(int resultCode, Intent data);

        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

        void storyObserver();

        StoryData getStoryData(int position);

        List<StoryPost> getStoryPosts();

        List<StoryPost>  getStoryPosts(int position);

        List<StoryData> getAllStoryData();
    }
}
