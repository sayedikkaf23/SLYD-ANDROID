package chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer;

import dagger.Binds;
import dagger.Module;

@Module
public interface WebRTCStreamPlayerModule {

  @Binds
  LiveStreamPresenterContract.LiveStreamPresenter providePresenter(
          LiveStreamPlayerPresenterImpl liveVideoPlayerPresenter);

  @Binds
  LiveStreamPresenterContract.LiveStreamView provideView(
          WebRTCStreamPlayerActivity liveVideoPlayerPresenter);
}
