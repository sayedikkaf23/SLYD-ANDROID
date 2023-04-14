package chat.hola.com.app.post.location;


import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>LocationModule</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
@Module
public interface LocationModule {
    @ActivityScoped
    @Binds
    LocationContract.Presenter presenter(LocationPresenter presenter);

    @ActivityScoped
    @Binds
    LocationContract.View view(LocationActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(LocationActivity activity);
}
