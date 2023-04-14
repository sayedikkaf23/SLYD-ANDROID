package chat.hola.com.app.ui.withdraw.detail;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.WithdawStatus;
import chat.hola.com.app.ui.adapter.WithdrawStatusAdapter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>{@link WithdrawDetailUtilModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */
@Module
public class WithdrawDetailUtilModule {

    @ActivityScoped
    @Provides
    Loader loader(WithdawDetailActivity context) {
        return new Loader(context);
    }

    @ActivityScoped
    @Provides
    List<WithdawStatus> provideBankList() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    WithdrawStatusAdapter adapter(Context context, List<WithdawStatus> data, TypefaceManager typefaceManager) {
        return new WithdrawStatusAdapter(context, typefaceManager, data);
    }
}
