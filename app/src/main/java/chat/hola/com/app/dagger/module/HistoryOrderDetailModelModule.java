package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
;

import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;

import chat.hola.com.app.orderdetails.HistoryOrderDetailViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HistoryOrderDetailModelModule extends ViewModelFactory {
  @Binds
  @IntoMap
  @ViewModelKey(HistoryOrderDetailViewModel.class)
  protected abstract ViewModel historyViewModel(HistoryOrderDetailViewModel ecomLoginViewModel);
}
