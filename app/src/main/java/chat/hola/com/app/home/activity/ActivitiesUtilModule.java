package chat.hola.com.app.home.activity;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.activity.followingTab.FollowingAdapter;
import chat.hola.com.app.home.activity.followingTab.model.Following;
import chat.hola.com.app.home.activity.youTab.YouAdapter;
import chat.hola.com.app.home.activity.youTab.model.Data;
import dagger.Module;
import dagger.Provides;

/**
 * Created by ankit on 22/2/18.
 */

//@FragmentScoped
@Module
public class ActivitiesUtilModule {

    @FragmentScoped
    @Provides
    ArrayList<Data> youActivityList() {
        return new ArrayList<Data>();
    }

    @FragmentScoped
    @Provides
    YouAdapter youAdapter(LandingActivity context, TypefaceManager typefaceManager) {
        return new YouAdapter(context, typefaceManager);
    }

    @FragmentScoped
    @Provides
    ArrayList<Following> youFollowingList() {
        return new ArrayList<Following>();
    }

    @FragmentScoped
    @Provides
    FollowingAdapter followingAdapter(LandingActivity context, TypefaceManager typefaceManager) {
        return new FollowingAdapter(context, typefaceManager);
    }
}
