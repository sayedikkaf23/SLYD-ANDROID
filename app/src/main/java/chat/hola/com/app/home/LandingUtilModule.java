package chat.hola.com.app.home;

import android.content.Context;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AlertDialog;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.Utilities.SocialShare;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.adapter.SuggestUserAdapter;
import chat.hola.com.app.home.comment.CommentFragmentAdapter;
import chat.hola.com.app.home.contact.ContactAdapter;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.contact.FriendRequestAdapter;
import chat.hola.com.app.home.model.ContentAdapter;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.User;
import chat.hola.com.app.home.social.model.SocialAdapter;
import chat.hola.com.app.home.stories.model.StoriesAdapter;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.home.trending.model.Header;
import chat.hola.com.app.home.trending.model.HeaderAdapter;
import chat.hola.com.app.home.trending.model.TrendingContentAdapter;
import chat.hola.com.app.home.trending.model.TrendingResponse;
import chat.hola.com.app.preview.PreviewActivity;
import chat.hola.com.app.profileScreen.channel.ChannelAdapter;
import chat.hola.com.app.profileScreen.collection.CollectionFragment;
import chat.hola.com.app.profileScreen.liked.LikedPostAdapter;
import chat.hola.com.app.profileScreen.liked.LikedPostFragment;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryFrag;
import chat.hola.com.app.profileScreen.profile_story.model.ProfileStoryAdap;
import chat.hola.com.app.profileScreen.purchase.PurchaseFragment;
import chat.hola.com.app.profileScreen.story.StoryFragment;
import chat.hola.com.app.profileScreen.story.model.TrendingDtlAdapter;
import chat.hola.com.app.profileScreen.tag.TagFragment;
import com.ezcall.android.R;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>LandingUtilModule</h1>
 *
 * @author 3Embed
 * @since 22/3/18.
 */

//@ActivityScoped
@Module
public class LandingUtilModule {

    @ActivityScoped
    @Provides
    AlertDialog.Builder builder(LandingActivity activity) {
        return new AlertDialog.Builder(activity);
    }

    @ActivityScoped
    @Provides
    ArrayAdapter<String> reportReasons(LandingActivity activity) {
        return new ArrayAdapter<String>(activity, R.layout.custom_select_dialog_singlechoice);
    }

    @ActivityScoped
    @Provides
    SocialShare instaShare(LandingActivity landingPage) {
        return new SocialShare(landingPage);
    }

    @ActivityScoped
    @Provides
    List<Data> socialLists() {
        return new ArrayList<Data>();
    }

    @ActivityScoped
    @Provides
    List<Friend> friendList() {
        return new ArrayList<Friend>();
    }

//    @ActivityScoped
//    @Provides
//    List<Friend> requestedFriend() {
//        return new ArrayList<Friend>();
//    }

    @ActivityScoped
    @Provides
    ContactAdapter getContactAdapter(List<Friend> arrayList, LandingActivity mContext, TypefaceManager typefaceManager) {
        return new ContactAdapter(arrayList, mContext, typefaceManager);
    }

    @ActivityScoped
    @Provides
    FriendRequestAdapter getFriendRequestAdapter(List<Friend> arrayList, LandingActivity mContext, TypefaceManager typefaceManager) {
        return new FriendRequestAdapter(arrayList, mContext, typefaceManager);
    }

//    @ActivityScoped
//    @Provides
//    SocialAdapter getSocialAdapter(List<Data> arrayList, LandingActivity mContext, TypefaceManager typefaceManager) {
//        return new SocialAdapter(arrayList, mContext, typefaceManager);
//    }

    @ActivityScoped
    @Provides
    List<StoryPost> storyPost() {
        return new ArrayList<StoryPost>();
    }

    @ActivityScoped
    @Provides
    List<StoryData> storyData() {
        return new ArrayList<StoryData>();
    }

    @ActivityScoped
    @Provides
    StoriesAdapter getStoriesAdapter(Context context, List<StoryData> data, TypefaceManager typefaceManager) {
        return new StoriesAdapter(context, data, typefaceManager);
    }

    @ActivityScoped
    @Provides
    ImageSourcePicker imageSourcePicker(LandingActivity activity) {
        return new ImageSourcePicker(activity, true, false);
    }

    @ActivityScoped
    @Provides
    ArrayList<Header> headers() {
        return new ArrayList<Header>();
    }

    @ActivityScoped
    @Provides
    HeaderAdapter headerAdapter(LandingActivity context, TypefaceManager typefaceManager, ArrayList<Header> headers) {
        return new HeaderAdapter(context, typefaceManager, headers);
    }


    @ActivityScoped
    @Provides
    ArrayList<Data> content() {
        return new ArrayList<Data>();
    }

    @ActivityScoped
    @Provides
    ArrayList<TrendingResponse> trendingContent() {
        return new ArrayList<TrendingResponse>();
    }

    @ActivityScoped
    @Provides
    TrendingContentAdapter contentAdapter(LandingActivity context, TypefaceManager typefaceManager, ArrayList<TrendingResponse> data) {
        return new TrendingContentAdapter(context, typefaceManager, data);
    }

    @ActivityScoped
    @Provides
    PreviewActivity previewActivity() {
        return new PreviewActivity();
    }

    /*@ActivityScoped
    @Provides
    StaggeredGridLayoutManager getStaggeredGridLayoutManager()
    {
        return new StaggeredGridLayoutManager(Constants.AddContactActivity.COLUMNS, LinearLayoutManager.VERTICAL);
    }*/

    @ActivityScoped
    @Provides
    SocialAdapter getSocialAdapter(LandingActivity mContext, List<Data> data, TypefaceManager typefaceManager) {
        return new SocialAdapter(mContext, data, typefaceManager);
    }

    /*---------------profile fragment----------------*/

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
    ChannelAdapter provideChannelAdapter(LandingActivity activity, TypefaceManager typefaceManager) {
        return new ChannelAdapter(activity, typefaceManager);
    }

    @ActivityScoped
    @Provides
    ContentAdapter provideContentAdapter(LandingActivity activity, TypefaceManager typefaceManager) {
        return new ContentAdapter(activity, typefaceManager);
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

    @ActivityScoped
    @Provides
    CollectionFragment collectionFragment() {
        return new CollectionFragment();
    }

    @ActivityScoped
    @Provides
    PurchaseFragment purchaseFragment() {
        return new PurchaseFragment();
    }

    /*---------------profile fragment end----------------*/

    @ActivityScoped
    @Provides
    List<Comment> getComments() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    CommentFragmentAdapter commentAdapter(List<Comment> comments, LandingActivity mContext, TypefaceManager typefaceManager) {
        return new CommentFragmentAdapter(comments, mContext, typefaceManager);
    }

    @ActivityScoped
    @Provides
    ArrayList<User> users() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    SuggestUserAdapter suggestUserAdapter(LandingActivity context, ArrayList<User> users, TypefaceManager typefaceManager) {
        return new SuggestUserAdapter(context, users, typefaceManager);
    }

}
