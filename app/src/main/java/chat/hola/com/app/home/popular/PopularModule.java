package chat.hola.com.app.home.popular;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>SocialDetailModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 23/3/18.
 */

@Module
public interface PopularModule {
    @FragmentScoped
    @Binds
    PopularContract.Presenter presenter(PopularPresenter presenter);

//    @FragmentScoped
//    @Binds
//    HomeContract.View view(HomeFragment detail);
}
