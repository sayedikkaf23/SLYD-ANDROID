package chat.hola.com.app.home.activity.youTab.channelrequesters;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 4/9/2018.
 */

//@ActivityScoped
@Module
public interface ChannelRequestersModule {
    @ActivityScoped
    @Binds
    ChannelRequestersContract.Presenter presenter(ChannelRequestersPresenter presenter);

    @ActivityScoped
    @Binds
    ChannelRequestersContract.View view(ChannelRequestersActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(ChannelRequestersActivity activity);

}
