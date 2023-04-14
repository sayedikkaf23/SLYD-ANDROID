package chat.hola.com.app.authentication.forgotpassword;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface ForgotPasswordModule {

    @Binds
    @ActivityScoped
    ForgotPasswordContract.View view(ForgotPasswordActivity view);

    @Binds
    @ActivityScoped
    ForgotPasswordContract.Presenter presenter(ForgotPasswordPresenter activity);

    @Binds
    @ActivityScoped
    Activity activity(ForgotPasswordActivity activity);
}
