package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.PlaceOrderRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.PlaceOrderUseCase;
import com.kotlintestgradle.repository.PlaceOrderRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class PlaceOrderUseCaseModule {
  @Provides
  PlaceOrderRepository provideRepository(
      PlaceOrderRepositoryImpl placeOrderRepository) {
    return placeOrderRepository;
  }

  @Provides
  public UseCase<PlaceOrderUseCase.RequestValues, PlaceOrderUseCase.ResponseValues> getUseCase(
      PlaceOrderUseCase placeOrderUseCase) {
    return placeOrderUseCase;
  }
}
