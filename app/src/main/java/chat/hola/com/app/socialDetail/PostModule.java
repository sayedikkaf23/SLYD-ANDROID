package chat.hola.com.app.socialDetail;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>PostModule</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

//@FragmentScoped
@Module
public interface PostModule {

    @FragmentScoped
    @Binds
    PostContract.Presenter postPresenter(PostPresenter presenter);
}
