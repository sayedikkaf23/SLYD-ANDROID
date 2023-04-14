package chat.hola.com.app.settings;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>SettingsModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 24/2/18.
 */

//@ActivityScoped
@Module
public interface SettingsModule {

    @ActivityScoped
    @Binds
    SettingsContract.Presenter settingsPresenter(SettingsPresenter presenter);

    @ActivityScoped
    @Binds
    SettingsContract.View view(SettingsActivity activity);

}
