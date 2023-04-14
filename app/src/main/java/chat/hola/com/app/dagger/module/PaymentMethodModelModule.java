package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class PaymentMethodModelModule extends ViewModelFactory {
  @Binds
  @IntoMap
  @ViewModelKey(chat.hola.com.app.ecom.payment.PaymentMethodViewModel.class)
  protected abstract ViewModel reviewViewModel(chat.hola.com.app.ecom.payment.PaymentMethodViewModel paymentMethodModel);
}
