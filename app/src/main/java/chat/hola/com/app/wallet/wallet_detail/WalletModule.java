package chat.hola.com.app.wallet.wallet_detail;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface WalletModule {
    @ActivityScoped
    @Binds
    WalletContract.Presenter presenter(WalletPresenter presenter);

    @ActivityScoped
    @Binds
    WalletContract.View view(WalletActivity walletActivity);

}
