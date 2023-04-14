package chat.hola.com.app.ui.withdraw.bankaccount;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>{@link BankAccountModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 march 2020
 */

@Module
public interface BankAccountModule {

    @ActivityScoped
    @Binds
    BankAccountContract.Presenter presenter(BankAccountPresenter presenter);
}
