package chat.hola.com.app.authentication.signup;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface SignUp2Module {

    @ActivityScoped
    @Binds
    SignUpContract.Presenter presenter(SignUpPresenter presenter);

    @ActivityScoped
    @Binds
    SignUpContract.View view(SignUp2Activity activity);

    @ActivityScoped
    @Binds
    Activity activity(SignUp2Activity activity);
}
