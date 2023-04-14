package chat.hola.com.app.home.activity.youTab.followrequest;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 4/9/2018.
 */

//@ActivityScoped
@Module
public interface FollowRequestModule {
    @ActivityScoped
    @Binds
    FollowRequestContract.Presenter presenter(FollowRequestPresenter presenter);

    @ActivityScoped
    @Binds
    FollowRequestContract.View view(FollowRequestActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(FollowRequestActivity activity);

}
