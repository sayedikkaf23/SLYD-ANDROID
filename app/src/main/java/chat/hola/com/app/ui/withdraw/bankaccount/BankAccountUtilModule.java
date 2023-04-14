package chat.hola.com.app.ui.withdraw.bankaccount;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.ui.withdraw.adapter.BankAccountAdapter;
import chat.hola.com.app.ui.withdraw.model.bankaccount.Data;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>{@link BankAccountUtilModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */
@Module
public class BankAccountUtilModule {

    @ActivityScoped
    @Provides
    Loader loader(BankAccountActivity context) {
        return new Loader(context);
    }

    @ActivityScoped
    @Provides
    List<StripeResponse.Data.ExternalAccounts.Account> provideBankList() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    BankAccountAdapter provideAdapter(Context context, List<StripeResponse.Data.ExternalAccounts.Account> data, TypefaceManager typefaceManager) {
        return new BankAccountAdapter(context, data, typefaceManager);
    }
}
