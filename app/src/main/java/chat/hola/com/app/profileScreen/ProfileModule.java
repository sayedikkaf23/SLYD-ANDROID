package chat.hola.com.app.profileScreen;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.profileScreen.bottomProfileMenu.ProfileMenuFrag;
import chat.hola.com.app.profileScreen.channel.ChannelFragment;
import chat.hola.com.app.profileScreen.channel.ChannelModule;
import chat.hola.com.app.profileScreen.collection.CollectionFragment;
import chat.hola.com.app.profileScreen.collection.CollectionModule;
import chat.hola.com.app.profileScreen.liked.LikedPostFragment;
import chat.hola.com.app.profileScreen.liked.LikedPostModule;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryModule;
import chat.hola.com.app.profileScreen.paid.PaidFragment;
import chat.hola.com.app.profileScreen.paid.PaidModule;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryFrag;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryModule;
import chat.hola.com.app.profileScreen.purchase.PurchaseFragment;
import chat.hola.com.app.profileScreen.purchase.PurchaseModule;
import chat.hola.com.app.profileScreen.story.StoryFragment;
import chat.hola.com.app.profileScreen.story.StoryModule;
import chat.hola.com.app.profileScreen.tag.TagFragment;
import chat.hola.com.app.profileScreen.tag.TagModule;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by ankit on 22/2/18.
 */

//@ActivityScoped
@Module
public interface ProfileModule {

    @FragmentScoped
    @ContributesAndroidInjector()
    ProfileMenuFrag profileMenuFrag();

    @FragmentScoped
    @ContributesAndroidInjector(modules = TagModule.class)
    TagFragment tagFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {StoryModule.class})
    StoryFragment storyFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {LikedPostModule.class})
    LikedPostFragment likedPostFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {ChannelModule.class})
    ChannelFragment channelFragment();

    @ActivityScoped
    @Binds
    ProfileContract.Presenter presenter(ProfilePresenter presenter);

    @ActivityScoped
    @Binds
    ProfileContract.View view(ProfileActivity profileActivity);

    @FragmentScoped
    @ContributesAndroidInjector(modules = {ProfileStoryModule.class})
    ProfileStoryFrag profileStoryFrag();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {LiveStreamHistoryModule.class})
    LiveStreamHistoryFragment liveStreamHistoryFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {CollectionModule.class})
    CollectionFragment collectionFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {PurchaseModule.class})
    PurchaseFragment purchaseFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {PaidModule.class})
    PaidFragment paidFragment();
}
