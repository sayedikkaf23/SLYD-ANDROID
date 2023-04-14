package chat.hola.com.app.home.callhistory;


import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryContract;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryPresenter;
import dagger.Binds;
import dagger.Module;

@Module
public interface CallHistoryModule {
    @FragmentScoped
    @Binds
    CallHistoryContract.Presenter presenter(CallHistoryPresenter callHistoryPresenter);
}
