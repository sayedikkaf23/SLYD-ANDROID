package chat.hola.com.app.authentication.verifyEmail;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface VerifyEmailModule {

    @Binds
    @ActivityScoped
    VerifyEmailContract.View view(VerifyEmailActivity view);

    @Binds
    @ActivityScoped
    VerifyEmailContract.Presenter presenter(VerifyEmailPresenter activity);

    @Binds
    @ActivityScoped
    Activity activity(VerifyEmailActivity activity);
}
