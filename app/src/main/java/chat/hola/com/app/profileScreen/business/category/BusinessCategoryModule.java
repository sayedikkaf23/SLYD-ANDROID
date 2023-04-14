package chat.hola.com.app.profileScreen.business.category;


import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>BusinessCategoryModule</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 16 August 2019
 */
@Module
public interface BusinessCategoryModule {
    @ActivityScoped
    @Binds
    BusinessCategoryContact.Presenter presenter(BusinessCategoryPresenter presenter);

    @ActivityScoped
    @Binds
    BusinessCategoryContact.View view(BusinessCategoryActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(BusinessCategoryActivity activity);
}
