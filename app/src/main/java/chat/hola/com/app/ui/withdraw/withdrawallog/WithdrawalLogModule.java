package chat.hola.com.app.ui.withdraw.withdrawallog;

import com.appscrip.myapplication.injection.scope.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>{@link WithdrawalLogModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 march 2020
 */

@Module
public interface WithdrawalLogModule {

    @ActivityScoped
    @Binds
    WithdrawalLogContract.Presenter presenter(WithdrawalLogPresenter presenter);
}
