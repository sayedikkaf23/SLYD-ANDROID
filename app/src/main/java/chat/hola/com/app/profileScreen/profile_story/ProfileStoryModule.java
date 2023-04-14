package chat.hola.com.app.profileScreen.profile_story;


import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface ProfileStoryModule {
    @FragmentScoped
    @Binds
    ProfileStoryContract.Presenter presenter(ProfileStoryPresenter profileStoryPresenter);
}
