package chat.hola.com.app.preview;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>PreviewModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 4/24/2018.
 */

//@ActivityScoped
@Module
public interface PreviewModule {

    @ActivityScoped
    @Binds
    PreviewContract.Presenter presenter(PreviewPresenter presenter);

    @ActivityScoped
    @Binds
    PreviewContract.View view(PreviewActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(PreviewActivity activity);


}
