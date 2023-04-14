package chat.hola.com.app.poststatus;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by embed on 15/12/18.
 */

@Module
public interface PostStatusDaggerModule {

    @ActivityScoped
    @Binds
    PostStatusPresenterImpl.PostStatusPresenterView providePostStatusView(PostStatusActivity postStatusActivity);

    @ActivityScoped
    @Binds
    PostStatusPresenterImpl.PostStatusPresent providePostStatusPresent(PostStatusPresenter postStatusPresenter);
}
