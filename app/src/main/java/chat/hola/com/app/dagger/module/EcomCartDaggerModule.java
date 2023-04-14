package chat.hola.com.app.dagger.module;

import android.app.Activity;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.ecom.cart.EcomCartActivity;
import chat.hola.com.app.ecom.cart.EcomCartFragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class EcomCartDaggerModule {
  @ActivityScoped
  @Binds
  abstract Activity getActivity(EcomCartActivity ecomCartActivity);

  @FragmentScoped
  @ContributesAndroidInjector
  abstract EcomCartFragment getCartScreenFragment();

}
