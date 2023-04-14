package chat.hola.com.app.wallet.transaction.debit;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface DebitModule {

    @FragmentScoped
    @Binds
    DebitContract.Presenter presenter(DebitPresenter presenter);

//    @FragmentScoped
//    @Binds
//    DebitContract.View view(DebitFragment fragment);
}
