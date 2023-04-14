package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.NotifyProductAvailabilityRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.cart.NotifyProductAvailabilityUseCase;
import com.kotlintestgradle.repository.NotifyProductAvailabilityRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class NotifyProductUseCaseModule {
  @Provides
  NotifyProductAvailabilityRepository provideRepository(
      NotifyProductAvailabilityRepositoryImpl addAddressRepository) {
    return addAddressRepository;
  }

  @Provides
  public UseCase<NotifyProductAvailabilityUseCase.RequestValues,
        NotifyProductAvailabilityUseCase.ResponseValues> getLoginDetailsUseCase(
      NotifyProductAvailabilityUseCase addAddressUseCase) {
    return addAddressUseCase;
  }
}
