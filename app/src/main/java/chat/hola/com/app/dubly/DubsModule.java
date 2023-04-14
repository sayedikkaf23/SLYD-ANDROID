package chat.hola.com.app.dubly;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>DubCategoryModule</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

//@ActivityScoped
@Module
public interface DubsModule {
    @ActivityScoped
    @Binds
    DubsContract.Presenter presenter(DubsPresenter presenter);

    @ActivityScoped
    @Binds
    DubsContract.View view(DubsActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(DubsActivity activity);
}
