package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
import chat.hola.com.app.ecom.cart.CartViewModel;
import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class EcomCartViewModel extends ViewModelFactory {
  @Binds
  @IntoMap
  @ViewModelKey(CartViewModel.class)
  protected abstract ViewModel cartViewModel(CartViewModel cartViewModel);
}
