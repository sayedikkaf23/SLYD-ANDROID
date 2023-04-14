package chat.hola.com.app.ui.withdraw.withdrawallog;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WithdrawLog;
import chat.hola.com.app.ui.adapter.WithdrawLogsAdapter;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>{@link WithdrawalLogUtilModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */

@Module
public class WithdrawalLogUtilModule {

    @ActivityScoped
    @Provides
    List<WithdrawLog.DataResponse.Data> dataList() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    WithdrawLogsAdapter withdrawLogsAdapter(Context context, TypefaceManager typefaceManager, List<WithdrawLog.DataResponse.Data> transactionDataList, SessionManager sessionManager) {
        return new WithdrawLogsAdapter(context, typefaceManager, transactionDataList,sessionManager);
    }

    @ActivityScoped
    @Provides
    Loader loader(WithdrawalLogActivity context) {
        return new Loader(context);
    }
}
