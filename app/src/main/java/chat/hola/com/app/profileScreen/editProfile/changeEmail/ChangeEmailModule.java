package chat.hola.com.app.profileScreen.editProfile.changeEmail;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h>ChangeEmailModule</h>
 * @author 3Embed.
 * @since 19/3/18.
 */

//@ActivityScoped
@Module
public interface ChangeEmailModule {

    @ActivityScoped
    @Binds
    ChangeEmailContract.Presenter changeEmailPresenter(ChangeEmailPresenter presenter);

    @ActivityScoped
    @Binds
    ChangeEmailContract.View changeEmailView(ChangeEmail changeEmail);

}
