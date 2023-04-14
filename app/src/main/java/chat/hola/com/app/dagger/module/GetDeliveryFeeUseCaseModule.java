package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.GetDeliveryFeeRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.address.GetDeliveryFeeUseCase;
import com.kotlintestgradle.repository.GetDeliveryFeeRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class GetDeliveryFeeUseCaseModule {
  @Provides
  GetDeliveryFeeRepository provideRepository(GetDeliveryFeeRepositoryImpl getDeliveryFeeRepository) {
    return getDeliveryFeeRepository;
  }

  @Provides
  public UseCase<GetDeliveryFeeUseCase.RequestValues, GetDeliveryFeeUseCase.ResponseValues> getUseCase(
          GetDeliveryFeeUseCase getDeliveryFeeUseCase) {
    return getDeliveryFeeUseCase;
  }
}
