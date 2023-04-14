package chat.hola.com.app.dubly;

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
public class DubsUtilModule {

    @ActivityScoped
    @Provides
    List<Dub> getDubs() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    DubsAdapter dubsAdapter(List<Dub> dubs, Activity mContext, TypefaceManager typefaceManager) {
        return new DubsAdapter(dubs, mContext, typefaceManager);
    }
}
