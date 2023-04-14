package chat.hola.com.app.authentication.newpassword;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface NewPasswordModule {

    @ActivityScoped
    @Binds
    NewPasswordContract.Presenter presenter(NewPasswordPresenter presenter);

    @ActivityScoped
    @Binds
    NewPasswordContract.View view(NewPasswordActivity view);

    @ActivityScoped
    @Binds
    Activity activity(NewPasswordActivity activity);
}
