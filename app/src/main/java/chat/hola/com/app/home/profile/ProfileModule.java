package chat.hola.com.app.home.profile;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryModule;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryFrag;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryModule;
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
    @Binds
    ProfileContract.Presenter presenter(ProfilePresenter presenter);

//    @ActivityScoped
//    @Binds
//    ProfileContract.View view(ProfileActivity profileActivity);

}
