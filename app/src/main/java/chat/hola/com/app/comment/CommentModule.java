package chat.hola.com.app.comment;

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
public interface CommentModule {
    @ActivityScoped
    @Binds
    CommentContract.Presenter presenter(CommentPresenter presenter);

    @ActivityScoped
    @Binds
    CommentContract.View view(CommentActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(CommentActivity activity);

}
