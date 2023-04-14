package chat.hola.com.app.home;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.home.activity.ActivitiesFragment;
import chat.hola.com.app.home.activity.ActivitiesModule;
import chat.hola.com.app.home.callhistory.CallHistoryModule;
import chat.hola.com.app.home.callhistory.CallsFragment;
import chat.hola.com.app.home.comment.CommentFragment;
import chat.hola.com.app.home.comment.CommentFragmentModule;
import chat.hola.com.app.home.contact.ContactFragment;
import chat.hola.com.app.home.contact.ContactModule;
import chat.hola.com.app.home.home.HomeFragment;
import chat.hola.com.app.home.home.HomeModule;
import chat.hola.com.app.home.live.LiveUsersFragment;
import chat.hola.com.app.home.live.LiveUsersModule;
import chat.hola.com.app.home.popular.PopularFragment;
import chat.hola.com.app.home.popular.PopularModule;
import chat.hola.com.app.home.profile.ProfileFragment;
import chat.hola.com.app.home.profile.ProfileFragmentNew;
import chat.hola.com.app.home.profile.ProfileModule;
import chat.hola.com.app.home.stories.StoriesFrag;
import chat.hola.com.app.home.stories.StoriesModule;
import chat.hola.com.app.home.trending.ContentFragment;
import chat.hola.com.app.home.trending.TrendingFragment;
import chat.hola.com.app.home.trending.TrendingModule;
import chat.hola.com.app.home.xclusive.XclusiveFragment;
import chat.hola.com.app.home.xclusive.XclusiveModule;
import chat.hola.com.app.profileScreen.channel.ChannelFragment;
import chat.hola.com.app.profileScreen.channel.ChannelModule;
import chat.hola.com.app.profileScreen.collection.CollectionFragment;
import chat.hola.com.app.profileScreen.collection.CollectionModule;
import chat.hola.com.app.profileScreen.liked.LikedPostFragment;
import chat.hola.com.app.profileScreen.liked.LikedPostModule;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryModule;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryFrag;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryModule;
import chat.hola.com.app.profileScreen.purchase.PurchaseFragment;
import chat.hola.com.app.profileScreen.purchase.PurchaseModule;
import chat.hola.com.app.profileScreen.story.StoryFragment;
import chat.hola.com.app.profileScreen.story.StoryModule;
import chat.hola.com.app.profileScreen.tag.TagFragment;
import chat.hola.com.app.profileScreen.tag.TagModule;
import chat.hola.com.app.socialDetail.PostFragment;
import chat.hola.com.app.socialDetail.PostModule;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * <h1>LandingModule</h1>
 *
 * @author 3Embed
 * @since 21/2/18.
 */

//@ActivityScoped
@Module
public interface LandingModule {

  @FragmentScoped
  @ContributesAndroidInjector(modules = HomeModule.class)
  HomeFragment homeFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = PopularModule.class)
  PopularFragment popularFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = XclusiveModule.class)
  XclusiveFragment xclusiveFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = LiveUsersModule.class)
  LiveUsersFragment liveUsersFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = PostModule.class)
  PostFragment postFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = ContactModule.class)
  ContactFragment contactFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = StoriesModule.class)
  StoriesFrag storiesFragment();

  //    @FragmentScoped
  //    @ContributesAndroidInjector(modules = {SocialModule.class})
  //    SocialFragment socialFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = ActivitiesModule.class)
  ActivitiesFragment activitiesFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = TrendingModule.class)
  TrendingFragment trendingFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = TrendingModule.class)
  ContentFragment contentFragment();

  @ActivityScoped
  @Binds
  LandingContract.Presenter presenter(LandingPresenter presenter);

  @ActivityScoped
  @Binds
  LandingContract.View view(LandingActivity landingPage);

  /*-------------- Profile Fragment--------------*/

  @FragmentScoped
  @ContributesAndroidInjector(modules = { ProfileModule.class })
  ProfileFragment profileFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = TagModule.class)
  TagFragment tagFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = { StoryModule.class })
  StoryFragment storyFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = { LikedPostModule.class })
  LikedPostFragment likedPostFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = { ChannelModule.class })
  ChannelFragment channelFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = { ProfileStoryModule.class })
  ProfileStoryFrag profileStoryFrag();

  @FragmentScoped
  @ContributesAndroidInjector(modules = { LiveStreamHistoryModule.class })
  LiveStreamHistoryFragment liveStreamHistoryFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = {CollectionModule.class})
  CollectionFragment collectionFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = {PurchaseModule.class})
  PurchaseFragment purchaseFragment();

  /*-------------- Profile Fragment End--------------*/

  @FragmentScoped
  @ContributesAndroidInjector(modules = { CommentFragmentModule.class })
  CommentFragment commentFragment();

  //    Isometrik groupstreaming
//  @FragmentScoped
//  @ContributesAndroidInjector()
//  StreamsFragment streamsFragment();
//
//  @FragmentScoped
//  @ContributesAndroidInjector(modules = GroupStreamsModule.class)
//  GroupStreamsFragment groupStreamsFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = CallHistoryModule.class)
  CallsFragment callsFragment();

  @FragmentScoped
  @ContributesAndroidInjector(modules = ProfileModule.class)
  ProfileFragmentNew profileFragmentNew();
}
