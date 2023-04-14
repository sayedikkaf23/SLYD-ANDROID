package chat.hola.com.app.home.profile;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.model.ContentAdapter;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.channel.ChannelAdapter;
import chat.hola.com.app.profileScreen.liked.LikedPostAdapter;
import chat.hola.com.app.profileScreen.liked.LikedPostFragment;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryFrag;
import chat.hola.com.app.profileScreen.profile_story.model.ProfileStoryAdap;
import chat.hola.com.app.profileScreen.story.StoryFragment;
import chat.hola.com.app.profileScreen.story.model.TrendingDtlAdapter;
import chat.hola.com.app.profileScreen.tag.TagFragment;
import dagger.Module;
import dagger.Provides;

/**
 * Created by ankit on 31/3/18.
 */

//@ActivityScoped
@Module
public class ProfileUtilModule {

    @ActivityScoped
    @Provides
    List<Data> storyData() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    ProfileStoryAdap profileStoryAdap(Context activity, List<StoryPost> storyPosts, ProfileStoryAdap.ClickListner frag) {
        return new ProfileStoryAdap(activity, storyPosts, frag);
    }

    @ActivityScoped
    @Provides
    TrendingDtlAdapter trendingDtlAdapter(Context activity, List<Data> storyData) {
        return new TrendingDtlAdapter(activity, storyData);
    }

    @ActivityScoped
    @Provides
    LikedPostAdapter likedPostAdapter(Context activity, List<Data> storyData) {
        return new LikedPostAdapter(activity, storyData);
    }


    @ActivityScoped
    @Provides
    ChannelAdapter provideChannelAdapter(ProfileActivity activity, TypefaceManager typefaceManager) {
        return new ChannelAdapter(activity, typefaceManager);
    }

    @ActivityScoped
    @Provides
    ContentAdapter provideContentAdapter(ProfileActivity activity, TypefaceManager typefaceManager) {
        return new ContentAdapter(activity, typefaceManager);
    }

    @ActivityScoped
    @Provides
    ImageSourcePicker imageSourcePicker(ProfileActivity activity) {
        return new ImageSourcePicker(activity, true, true);
    }

    @ActivityScoped
    @Provides
    AlertDialog.Builder builder(ProfileActivity activity) {
        return new AlertDialog.Builder(activity);
    }

    @ActivityScoped
    @Provides
    ArrayAdapter<String> reportReasons(ProfileActivity activity) {
        return new ArrayAdapter<String>(activity, R.layout.custom_select_dialog_singlechoice);
    }

    @ActivityScoped
    @Provides
    StoryFragment storyFragment() {
        return new StoryFragment();
    }

    @ActivityScoped
    @Provides
    LikedPostFragment likedPostFragment() {
        return new LikedPostFragment();
    }

    @ActivityScoped
    @Provides
    TagFragment tagFragment() {
        return new TagFragment();
    }

    @ActivityScoped
    @Provides
    ProfileStoryFrag profileStoryFrag() {
        return new ProfileStoryFrag();
    }

    @ActivityScoped
    @Provides
    LiveStreamHistoryFragment liveStreamHistoryFragment() {
        return new LiveStreamHistoryFragment();
    }
}
