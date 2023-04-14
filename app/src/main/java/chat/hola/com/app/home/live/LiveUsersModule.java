package chat.hola.com.app.home.live;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
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
    @FragmentScoped
    @Binds
    LiveUsersContract.Presenter presenter(LiveUsersPresenter presenter);
}
