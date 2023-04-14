package chat.hola.com.app.live_stream.Home.live_users;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>LiveUsersModule</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0.
 * @since 26/7/2019.
 */

@Module
public interface LiveUsersModule {
    @ActivityScoped
    @Binds
    LiveUsersContract.Presenter presenter(LiveUsersPresenter presenter);

    @ActivityScoped
    @Binds
    LiveUsersContract.View view(LiveUsersActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(LiveUsersActivity activity);

}
