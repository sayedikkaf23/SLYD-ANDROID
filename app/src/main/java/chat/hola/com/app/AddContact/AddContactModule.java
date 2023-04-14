package chat.hola.com.app.AddContact;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>AddContactModule</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

//@FragmentScoped
@Module
public interface AddContactModule {

    @ActivityScoped
    @Binds
    AddContactContract.Presenter acceptRequestPresenter(AddContactPresenter presenter);

}
