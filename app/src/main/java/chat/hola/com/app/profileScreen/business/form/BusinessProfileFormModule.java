package chat.hola.com.app.profileScreen.business.form;


import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>BusinessProfileFormModule</h1>
 *
 * @author Shaktisnh Jadeja
 * @version 1.0
 * @since 08 July 2019
 */
@Module
public interface BusinessProfileFormModule {
    @ActivityScoped
    @Binds
    BusinessProfileFormContract.Presenter presenter(BusinessProfileFormPresenter presenter);

    @ActivityScoped
    @Binds
    BusinessProfileFormContract.View view(BusinessProfileFormActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(BusinessProfileFormActivity activity);
}
