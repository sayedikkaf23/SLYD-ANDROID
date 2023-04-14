package chat.hola.com.app.ui.withdraw.addbankaccount;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>{@link AddBankAccountModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 march 2020
 */

@Module
public interface AddBankAccountModule {

    @ActivityScoped
    @Binds
    AddBankAccountContract.Presenter presenter(AddBankAccountPresenter presenter);
}
