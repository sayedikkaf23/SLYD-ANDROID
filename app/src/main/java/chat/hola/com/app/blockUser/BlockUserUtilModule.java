package chat.hola.com.app.blockUser;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.blockUser.model.User;
import chat.hola.com.app.blockUser.model.BlockUserAdapter;
import chat.hola.com.app.dagger.ActivityScoped;
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
public class BlockUserUtilModule {

    @ActivityScoped
    @Provides
    List<User> getBlockedUser() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    BlockUserAdapter blockUserAdapter(List<User> users, Activity mContext, TypefaceManager typefaceManager) {
        return new BlockUserAdapter(users, mContext, typefaceManager);
    }
}
