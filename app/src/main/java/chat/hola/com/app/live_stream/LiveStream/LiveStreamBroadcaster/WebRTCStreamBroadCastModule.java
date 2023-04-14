package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;

import dagger.Binds;
import dagger.Module;

@Module
public interface WebRTCStreamBroadCastModule {
    @Binds
    LiveBroadCastPresenterContract.LiveBroadCastPresenter providePresenter(LiveBroadCastPresenterImpl liveBroadCastPresenter);

    @Binds
    LiveBroadCastPresenterContract.LiveBroadCastView provideLiveView(WebRTCStreamBroadcasterActivity webRTCStreamBroadcasterActivity);
}