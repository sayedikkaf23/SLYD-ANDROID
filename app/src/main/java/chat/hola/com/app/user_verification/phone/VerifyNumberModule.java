package chat.hola.com.app.user_verification.phone;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface VerifyNumberModule {

    @Binds
    @ActivityScoped
    VerifyNumberContract.View view(VerifyNumberOTPActivity view);

    @Binds
    @ActivityScoped
    VerifyNumberContract.Presenter presenter(VerifyNumberPresenter activity);

    @Binds
    @ActivityScoped
    Activity activity(VerifyNumberOTPActivity activity);
}
