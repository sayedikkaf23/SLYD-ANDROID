package chat.hola.com.app.ui.withdraw.detail;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>{@link WithdrawDetailModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 march 2020
 */

@Module
public interface WithdrawDetailModule {

    @ActivityScoped
    @Binds
    WithdrawDetailContract.Presenter presenter(WithdrawDetailPresenter presenter);
}
