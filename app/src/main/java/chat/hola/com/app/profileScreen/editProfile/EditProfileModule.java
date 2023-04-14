package chat.hola.com.app.profileScreen.editProfile;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h>EditProfileModule</h>
 * @author 3Embed.
 * @since 22/2/18.
 */

//@ActivityScoped
@Module
public interface EditProfileModule {

    @ActivityScoped
    @Binds
    EditProfileContract.Presenter presenter(EditProfilePresenter presenter);

    @ActivityScoped
    @Binds
    EditProfileContract.View view(EditProfileActivity editProfileActivity);

}
