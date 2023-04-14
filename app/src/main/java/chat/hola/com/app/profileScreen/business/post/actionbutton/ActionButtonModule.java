package chat.hola.com.app.profileScreen.business.post.actionbutton;


import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>ActionButtonModule</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
@Module
public interface ActionButtonModule {
    @FragmentScoped
    @Binds
    ActionButtonContract.Presenter presenter(ActionButtonPresenter presenter);
}
