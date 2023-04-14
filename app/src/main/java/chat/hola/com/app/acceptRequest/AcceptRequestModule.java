package chat.hola.com.app.acceptRequest;

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
public interface AcceptRequestModule {

    @ActivityScoped
    @Binds
    AcceptRequestContract.Presenter acceptRequestPresenter(AcceptRequestPresenter presenter);

}
