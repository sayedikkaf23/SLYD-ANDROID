package chat.hola.com.app.post;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 21/2/18.
 */

//@ActivityScoped
@Module
public interface PostModule {

    @ActivityScoped
    @Binds
    PostContract.Presenter presenter(PostPresenter presenter);

    @ActivityScoped
    @Binds
    PostContract.View view(PostActivity activity);

}
