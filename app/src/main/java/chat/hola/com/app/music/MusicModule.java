package chat.hola.com.app.music;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>HashtagModule</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

//@ActivityScoped
@Module
public interface MusicModule {
    @ActivityScoped
    @Binds
    MusicContract.Presenter presenter(MusicPresenter presenter);

    @ActivityScoped
    @Binds
    MusicContract.View view(MusicActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(MusicActivity activity);

}
