package chat.hola.com.app.profileScreen.business.configuration;


import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>BusinessConfigureModule</h1>
 *
 * @author Shaktisnh Jadeja
 * @version 1.0
 * @since 08 July 2019
 */
@Module
public interface BusinessConfigureModule {
    @ActivityScoped
    @Binds
    BusinessConfigurationContract.Presenter presenter(BusinessConfigurationPresenter presenter);

    @ActivityScoped
    @Binds
    BusinessConfigurationContract.View view(BusinessConfigurationActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(BusinessConfigurationActivity activity);
}
