package chat.hola.com.app.profileScreen.discover.contact;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h>ContactModule</h>
 * @author 3Embed.
 * @since 23/2/18.
 */

//@FragmentScoped
@Module
public interface ContactModule {

    @FragmentScoped
    @Binds
    ContactContract.Presenter presenter(ContactPresenter presenter);

}
