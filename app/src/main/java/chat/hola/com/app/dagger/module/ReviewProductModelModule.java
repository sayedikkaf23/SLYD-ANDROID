package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
import chat.hola.com.app.ecom.review.ReviewProductViewModel;
import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ReviewProductModelModule extends ViewModelFactory {
  @Binds
  @IntoMap
  @ViewModelKey(ReviewProductViewModel.class)
  protected abstract ViewModel reviewViewModel(ReviewProductViewModel reviewProductViewModel);
}
