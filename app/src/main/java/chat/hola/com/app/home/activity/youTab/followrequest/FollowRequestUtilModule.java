package chat.hola.com.app.home.activity.youTab.followrequest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>SearchUserUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */
//@ActivityScoped
@Module
public class FollowRequestUtilModule {

    @ActivityScoped
    @Provides
    List<ReuestData> getFollowingRequested() {
        return new ArrayList<ReuestData>();
    }

    @ActivityScoped
    @Provides
    FollowRequestAdapter channelRequesterAdapter(Activity activity, List<ReuestData> list, TypefaceManager typefaceManager) {
        return new FollowRequestAdapter(activity, list, typefaceManager);
    }

}
