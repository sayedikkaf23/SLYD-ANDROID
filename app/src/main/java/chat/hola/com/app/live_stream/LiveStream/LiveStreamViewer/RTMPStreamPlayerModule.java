package chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer;

import dagger.Binds;
import dagger.Module;

/**
 * Created by moda on 12/19/2018.
 */
@Module
public interface RTMPStreamPlayerModule
{


    @Binds
    LiveStreamPresenterContract.LiveStreamPresenter providePresenter(LiveStreamPlayerPresenterImpl liveVideoPlayerPresenter);

    @Binds
    LiveStreamPresenterContract.LiveStreamView provideView(
            RTMPStreamPlayerActivity liveVideoPlayerPresenter);


}
