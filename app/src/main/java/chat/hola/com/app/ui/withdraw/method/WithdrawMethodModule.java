package chat.hola.com.app.ui.withdraw.method;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface WithdrawMethodModule {
    @ActivityScoped
    @Binds
    WithdrawMethodContract.Presenter presenter(WithdrawMethodPresenter presenter);
}
