package chat.hola.com.app.dagger.module;


import com.kotlintestgradle.data.repository.order.GetOrderHistoryRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.order.GetOrderHistoryUseCase;
import com.kotlintestgradle.repository.GetOrderHistoryRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class GetOrderHistoryUseCaseModule {
  @Provides
  GetOrderHistoryRepository provideRepository(GetOrderHistoryRepositoryImpl addAddressRepository) {
    return addAddressRepository;
  }

  @Provides
  public UseCase<GetOrderHistoryUseCase.RequestValues, GetOrderHistoryUseCase.ResponseValues> getLoginDetailsUseCase(
      GetOrderHistoryUseCase addAddressUseCase) {
    return addAddressUseCase;
  }
}
