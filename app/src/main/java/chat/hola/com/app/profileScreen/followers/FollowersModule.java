package chat.hola.com.app.profileScreen.followers;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 19/3/18.
 */

//@ActivityScoped
@Module
public interface FollowersModule {

    @ActivityScoped
    @Binds
    FollowersContract.Presenter changeEmailPresenter(FollowersPresenter presenter);

    @ActivityScoped
    @Binds
    FollowersContract.View changeEmailView(FollowersActivity followersActivity);

}
