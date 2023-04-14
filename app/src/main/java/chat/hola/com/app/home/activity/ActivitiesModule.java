package chat.hola.com.app.home.activity;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.home.activity.followingTab.FollowingContract;
import chat.hola.com.app.home.activity.followingTab.FollowingFrag;
import chat.hola.com.app.home.activity.followingTab.FollowingPresenter;
import chat.hola.com.app.home.activity.youTab.model.YouModel;
import chat.hola.com.app.home.activity.youTab.YouContract;
import chat.hola.com.app.home.activity.youTab.YouFrag;
import chat.hola.com.app.home.activity.youTab.YouPresenter;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by ankit on 21/2/18.
 */

//not being used yet.
////@FragmentScoped
@Module
public interface  ActivitiesModule {

    @ContributesAndroidInjector()
    YouFrag youTabFrag();

    @ContributesAndroidInjector()
    FollowingFrag followingTabFrag();

    @FragmentScoped
    @Binds
    FollowingContract.Presenter followingPresenter(FollowingPresenter presenter);

    @FragmentScoped
    @Binds
    YouContract.Presenter youPresenter(YouPresenter presenter);

    @FragmentScoped
    @Binds
    YouModel provideYouModel(YouModel model);

}
