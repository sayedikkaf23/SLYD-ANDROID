package chat.hola.com.app.search_user;

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
public class SearchUserUtilModule {

    @ActivityScoped
    @Provides
    List<Friend> getData() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    SearchUserAdapter searchUserAdapter(List<Friend> data, Activity mContext, TypefaceManager typefaceManager) {
        return new SearchUserAdapter(mContext, data, typefaceManager);
    }
}
