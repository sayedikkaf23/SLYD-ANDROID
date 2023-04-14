package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;


import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;

import chat.hola.com.app.historyproductdetail.HistoryProductDetailViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HistoryProductDetailViewModelModule extends ViewModelFactory {

  @Binds
  @IntoMap
  @ViewModelKey(HistoryProductDetailViewModel.class)
  protected abstract ViewModel historyViewModel(HistoryProductDetailViewModel catViewMoreViewModel);
}
