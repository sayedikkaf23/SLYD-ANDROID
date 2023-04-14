package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
import chat.hola.com.app.ecom.addresslist.SavedAddressListViewModel;
import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SavedAddressListViewModule extends ViewModelFactory {

  @Binds
  @IntoMap
  @ViewModelKey(SavedAddressListViewModel.class)
  protected abstract ViewModel homeViewModel(SavedAddressListViewModel productListViewModel);
}
