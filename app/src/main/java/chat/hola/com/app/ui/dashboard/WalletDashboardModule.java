package chat.hola.com.app.ui.dashboard;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>WalletDashboardModule</h1>
 *
 * <p>It @{@link Binds} the Presenter</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
@Module
public interface WalletDashboardModule {
    @FragmentScoped
    @Binds
    WalletDashboardContract.Presenter presenter(WalletDashboardPresenter presenter);
}
