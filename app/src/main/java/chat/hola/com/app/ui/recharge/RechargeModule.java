package chat.hola.com.app.ui.recharge;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>RechargeModule</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
@Module
public interface RechargeModule {
    @FragmentScoped
    @Binds
    RechargeContract.Presenter rechargePresenter(RechargePresenter presenter);
}
