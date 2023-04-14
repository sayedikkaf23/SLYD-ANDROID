package chat.hola.com.app.authentication.signup;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface SignUp1Module {

    @ActivityScoped
    @Binds
    SignUpContract.Presenter presenter(SignUpPresenter presenter);

    @ActivityScoped
    @Binds
    SignUpContract.View view(SignUp1Activity activity);

    @ActivityScoped
    @Binds
    Activity activity(SignUp1Activity activity);
}