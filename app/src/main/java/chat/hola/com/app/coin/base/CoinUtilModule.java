package chat.hola.com.app.coin.base;

import android.content.Context;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.coin.adapter.CoinPackageAdapter;
import chat.hola.com.app.coin.model.CoinPackage;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

@Module
public class CoinUtilModule {
    @ActivityScoped
    @Provides
    ArrayList<CoinPackage> coinPackages() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    CoinPackageAdapter coinPackageAdapter(Context context, ArrayList<CoinPackage> packageList) {
        return new CoinPackageAdapter(context, packageList);
    }

    @ActivityScoped
    @Provides
    Loader loader(Context context) {
        return new Loader(context);
    }
}
