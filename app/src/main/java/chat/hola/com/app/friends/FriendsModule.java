package chat.hola.com.app.friends;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>BlockUserModule</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

//@ActivityScoped
@Module
public interface FriendsModule {
    @ActivityScoped
    @Binds
    FriendsContract.Presenter presenter(FriendsPresenter presenter);

    @ActivityScoped
    @Binds
    FriendsContract.View view(FriendsActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(FriendsActivity activity);

}
