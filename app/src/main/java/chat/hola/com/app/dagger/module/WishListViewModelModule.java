package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
import chat.hola.com.app.ecom.wishlist.WishListViewModel;
import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class WishListViewModelModule extends ViewModelFactory {
  @Binds
  @IntoMap
  @ViewModelKey(WishListViewModel.class)
  protected abstract ViewModel productFilterViewModel(
      WishListViewModel updateProfileViewModel);

}
