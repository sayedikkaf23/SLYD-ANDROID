package chat.hola.com.app.subscription;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.subscription.active.ActiveFragment;
import chat.hola.com.app.subscription.active.ActiveModule;
import chat.hola.com.app.subscription.cancelled.CancelledFragment;
import chat.hola.com.app.subscription.cancelled.CancelledModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface SubscriptionModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = ActiveModule.class)
    ActiveFragment activeFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = CancelledModule.class)
    CancelledFragment cancelledFragment();

}
