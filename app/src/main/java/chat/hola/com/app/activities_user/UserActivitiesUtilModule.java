package chat.hola.com.app.activities_user;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.activity.followingTab.FollowingAdapter;
import chat.hola.com.app.home.activity.followingTab.model.Following;
import chat.hola.com.app.home.activity.youTab.YouAdapter;
import chat.hola.com.app.home.activity.youTab.model.Data;
import dagger.Module;
import dagger.Provides;

@Module
public class UserActivitiesUtilModule {
    @ActivityScoped
    @Provides
    ArrayList<Data> youActivityList() {
        return new ArrayList<Data>();
    }

    @ActivityScoped
    @Provides
    YouAdapter youAdapter(UserActivitiesActivity context, TypefaceManager typefaceManager) {
        return new YouAdapter(context, typefaceManager);
    }

    @ActivityScoped
    @Provides
    ArrayList<Following> youFollowingList() {
        return new ArrayList<Following>();
    }

    @ActivityScoped
    @Provides
    FollowingAdapter followingAdapter(UserActivitiesActivity context, TypefaceManager typefaceManager) {
        return new FollowingAdapter(context, typefaceManager);
    }
}
