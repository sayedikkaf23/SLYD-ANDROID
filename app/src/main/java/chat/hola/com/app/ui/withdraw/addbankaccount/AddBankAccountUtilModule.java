package chat.hola.com.app.ui.withdraw.addbankaccount;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>{@link AddBankAccountUtilModule}</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */
@Module
public class AddBankAccountUtilModule {

    @ActivityScoped
    @Provides
    Loader loader(AddBankAccountActivity context){return new Loader(context);}

}
