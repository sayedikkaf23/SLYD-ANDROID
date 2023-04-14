package chat.hola.com.app.authentication.signup;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface SignUpModule {

    @ActivityScoped
    @Binds
    SignUpContract.Presenter presenter(SignUpPresenter presenter);

    @ActivityScoped
    @Binds
    SignUpContract.View view(SignUpActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(SignUpActivity activity);
}
