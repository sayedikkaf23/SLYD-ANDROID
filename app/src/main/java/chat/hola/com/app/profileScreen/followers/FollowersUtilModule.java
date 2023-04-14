package chat.hola.com.app.profileScreen.followers;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * Created by ankit on 20/3/18.
 */

//@ActivityScoped
@Module
public class FollowersUtilModule {

    @ActivityScoped
    @Provides
    FollowerAdapter followerAdapter(FollowersActivity activity, TypefaceManager typefaceManager){
        return new FollowerAdapter(activity,typefaceManager);
    }
}
