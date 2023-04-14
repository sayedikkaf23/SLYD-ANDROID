package chat.hola.com.app.wallet.transaction.credit;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface CreditModule {

    @FragmentScoped
    @Binds
    CreditContract.Presenter presenter(CreditPresenter creditPresenter);

//    @FragmentScoped
//    @Binds
//    CreditContract.View view(CreditFragment creditFragment);

}
