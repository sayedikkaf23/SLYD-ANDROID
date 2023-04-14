package chat.hola.com.app.home.contact;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>ContactModule</h1>
 *
 * @author 3Embed
 * @since 13/2/2019.
 */

@Module
public interface ContactModule {

    @FragmentScoped
    @Binds
    ContactContract.Presenter socialPresenter(ContactPresenter presenter);

    @FragmentScoped
    @Binds
    ContactFriendModel contactModel(ContactFriendModel model);
}
