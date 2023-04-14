package chat.hola.com.app.blockUser;

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
public interface BlockUserModule {
    @ActivityScoped
    @Binds
    BlockUserContract.Presenter presenter(BlockUserPresenter presenter);

    @ActivityScoped
    @Binds
    BlockUserContract.View view(BlockUserActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(BlockUserActivity activity);

}
