package chat.hola.com.app.search_user;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>BlockUserModule</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

//@ActivityScoped
@Module
public interface SearchUserModule {
    @ActivityScoped
    @Binds
    SearchUserContract.Presenter presenter(SearchUserPresenter presenter);

    @ActivityScoped
    @Binds
    SearchUserContract.View view(SearchUserActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(SearchUserActivity activity);

}
