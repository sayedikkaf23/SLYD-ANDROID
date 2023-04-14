package chat.hola.com.app.activities_user;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.activity.followingTab.FollowingContract;
import chat.hola.com.app.home.activity.followingTab.FollowingFrag;
import chat.hola.com.app.home.activity.followingTab.FollowingPresenter;
import chat.hola.com.app.home.activity.youTab.YouContract;
import chat.hola.com.app.home.activity.youTab.YouFrag;
import chat.hola.com.app.home.activity.youTab.YouPresenter;
import chat.hola.com.app.home.activity.youTab.model.YouModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface UserActivitiesModule {
    @ContributesAndroidInjector()
    YouFrag youTabFrag();

    @ContributesAndroidInjector()
    FollowingFrag followingTabFrag();

    @ActivityScoped
    @Binds
    FollowingContract.Presenter followingPresenter(FollowingPresenter presenter);

    @ActivityScoped
    @Binds
    YouContract.Presenter youPresenter(YouPresenter presenter);

    @ActivityScoped
    @Binds
    YouModel provideYouModel(YouModel model);
}
