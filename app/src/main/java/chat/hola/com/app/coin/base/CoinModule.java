package chat.hola.com.app.coin.base;

import chat.hola.com.app.coin.pack.CoinPackFragment;
import chat.hola.com.app.coin.pack.CoinPackModule;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface CoinModule {
    @FragmentScoped
    @ContributesAndroidInjector(modules = CoinPackModule.class)
    CoinPackFragment coinPackFragment();
}
