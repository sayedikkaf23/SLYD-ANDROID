package chat.hola.com.app.friends;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.contact.Friend;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>BlockUserUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */
////@ActivityScoped
@Module
public class FriendsUtilModule {

    @ActivityScoped
    @Provides
    List<Friend> getFrinds() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    FriendsAdapter FriendsAdapter(List<Friend> friends, Activity mContext, TypefaceManager typefaceManager) {
        return new FriendsAdapter(friends, mContext, typefaceManager);
    }
}
