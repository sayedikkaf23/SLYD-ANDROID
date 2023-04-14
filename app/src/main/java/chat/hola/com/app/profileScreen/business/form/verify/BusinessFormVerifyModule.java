package chat.hola.com.app.profileScreen.business.form.verify;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface BusinessFormVerifyModule {

    @ActivityScoped
    @Binds
    BusinessFormVerifyContract.Presenter presenter(BusinessFormVerifyPresenter presenter);

    @ActivityScoped
    @Binds
    BusinessFormVerifyContract.View view(BusinessFormVerifyActivity activity);

}
