package chat.hola.com.app.ui.recharge;


import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.ui.kyc.KycActivity;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>KycUtilModule<h1/>
 *
 * <p>This module provides all utilities which @{@link KycActivity} and child fragments can @Inject,
 * can add List, Adapters, Custom components, etc</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
@Module
public class RechargeUtilModule {

    @ActivityScoped
    @Provides
    Loader loader(RechargeActivity context) {
        return new Loader(context);
    }
}
