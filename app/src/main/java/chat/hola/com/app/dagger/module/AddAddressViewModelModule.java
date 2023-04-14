package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
import chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressViewModel;
import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AddAddressViewModelModule extends ViewModelFactory {

  @Binds
  @IntoMap
  @ViewModelKey(AddAddressViewModel.class)
  protected abstract ViewModel homeViewModel(AddAddressViewModel catViewMoreViewModel);
}
