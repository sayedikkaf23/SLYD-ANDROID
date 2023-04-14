package chat.hola.com.app.home.stories;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>StoriesModule</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */

//@FragmentScoped
@Module
public interface StoriesModule {

    @FragmentScoped
    @Binds
    StoriesContract.View storiesView(StoriesFrag fragment);

    @FragmentScoped
    @Binds
    StoriesContract.Presenter storiesPresenter(StoriesPresenter presenter);

}
