package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;

import dagger.Binds;
import dagger.Module;

/**
 * Created by moda on 12/3/2018.
 */
@Module
public interface RTMPStreamBroadCastModule
{

    @Binds
    LiveBroadCastPresenterContract.LiveBroadCastPresenter providePresenter(LiveBroadCastPresenterImpl liveBroadCastPresenter);

    @Binds
    LiveBroadCastPresenterContract.LiveBroadCastView provideLiveView(
            RTMPStreamBroadcasterActivity liveVideoBroadcasterActivity);
}
