package chat.hola.com.app.ui.password;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>RechargePasswordModule</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
@Module
public interface RechargePasswordModule {
    @FragmentScoped
    @Binds
    RechargePasswordContract.Presenter rechargePresenter(RechargePasswordPresenter presenter);
}
