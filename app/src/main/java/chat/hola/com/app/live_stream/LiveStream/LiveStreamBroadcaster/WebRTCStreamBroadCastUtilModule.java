package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.live_stream.gift.GiftFragment;
import dagger.Module;
import dagger.Provides;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 07 August 2019
 */
@Module
public class WebRTCStreamBroadCastUtilModule {

    @ActivityScoped
    @Provides
    List<Viewer> followerModels() {
        return new ArrayList();
    }

    @ActivityScoped
    @Provides
    ViewersAdapter viewersAdapter(List<Viewer> viewers, Context mContext, TypefaceManager typefaceManager) {
        return new ViewersAdapter(viewers, mContext, typefaceManager);
    }
    @ActivityScoped
    @Provides
    GiftFragment giftFragment() {
        return new GiftFragment();
    }
}
