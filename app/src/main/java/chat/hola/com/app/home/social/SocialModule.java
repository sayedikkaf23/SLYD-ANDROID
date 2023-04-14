package chat.hola.com.app.home.social;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.home.social.model.SocialModel;
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
public interface SocialModule {

    @FragmentScoped
    @Binds
    SocialContract.Presenter socialPresenter(SocialPresenter presenter);

    @FragmentScoped
    @Binds
    SocialModel socialModel(SocialModel model);
}
