package chat.hola.com.app.ui.dashboard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WalletTransactionData;
import chat.hola.com.app.ui.adapter.WalletTransactionAdapter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>WalletDashboardModule</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
@Module
public class WalletDashboardUtilModule {
    @ActivityScoped
    @Provides
    List<WalletTransactionData> transactionData() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    WalletTransactionAdapter walletTransactionAdapter(Context context, TypefaceManager typefaceManager, List<WalletTransactionData> list, SessionManager sessionManager) {
        return new WalletTransactionAdapter(context, typefaceManager, list, sessionManager);
    }

    @ActivityScoped
    @Provides
    Loader loader(WalletDashboardActivity context) {
        return new Loader(context);
    }
}
