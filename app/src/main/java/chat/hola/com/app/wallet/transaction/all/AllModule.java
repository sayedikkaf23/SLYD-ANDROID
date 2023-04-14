package chat.hola.com.app.wallet.transaction.all;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface AllModule {

//    @FragmentScoped
//    @Binds
//    AllContract.View view(AllFragment allFragment);

    @FragmentScoped
    @Binds
    AllContract.Presenter allPresenter(AllPresenter presenter);

}
