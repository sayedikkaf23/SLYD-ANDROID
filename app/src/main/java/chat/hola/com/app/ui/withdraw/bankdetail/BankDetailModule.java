package chat.hola.com.app.ui.withdraw.bankdetail;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface BankDetailModule {
    @ActivityScoped
    @Binds
    BankDetailContract.Presenter presenter(BankDetailPresenter presenter);
}
