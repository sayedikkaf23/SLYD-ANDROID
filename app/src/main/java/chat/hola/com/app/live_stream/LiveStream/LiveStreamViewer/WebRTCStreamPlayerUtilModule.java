package chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.ViewersAdapter;
import chat.hola.com.app.live_stream.gift.GiftFragment;
import dagger.Module;
import dagger.Provides;

@Module
public class WebRTCStreamPlayerUtilModule {

  @ActivityScoped
  @Provides
  List<Viewer> followerModels() {
    return new ArrayList();
  }

  @ActivityScoped
  @Provides
  ViewersAdapter viewersAdapter(List<Viewer> viewers, Context mContext,
                                TypefaceManager typefaceManager) {
    return new ViewersAdapter(viewers, mContext, typefaceManager);
  }

  @ActivityScoped
  @Provides
  GiftFragment giftFragment() {
    return new GiftFragment();
  }
}
