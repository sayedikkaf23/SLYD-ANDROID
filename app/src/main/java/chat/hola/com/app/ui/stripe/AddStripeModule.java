package chat.hola.com.app.ui.stripe;


import com.appscrip.myapplication.injection.scope.ActivityScoped;

import dagger.Binds;
import dagger.Module;


/**
 * <h1>AddStripeModule<h1/>
 *
 * <p>contains all fragments defines {@link chat.hola.com.app.dagger.FragmentScoped} and
 * binds the related modules using {@link dagger.android.ContributesAndroidInjector}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
@Module
public interface AddStripeModule {
    @ActivityScoped
    @Binds
    AddStripeContract.Presenter presenter(AddStripePresenter presenter);
}
