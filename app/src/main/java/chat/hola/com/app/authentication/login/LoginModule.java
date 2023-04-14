package chat.hola.com.app.authentication.login;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface LoginModule {

    @ActivityScoped
    @Binds
    LoginContract.Presenter presenter(LoginPresenter loginPresenter);

    @ActivityScoped
    @Binds
    LoginContract.View view(LoginActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(LoginActivity activity);
}
