package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;


import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;

import chat.hola.com.app.tracking.EcomTrackingViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class EcomTrackViewModelModule extends ViewModelFactory {

  @Binds
  @IntoMap
  @ViewModelKey(EcomTrackingViewModel.class)
  protected abstract ViewModel cartViewModel(EcomTrackingViewModel cartViewModel);
}
