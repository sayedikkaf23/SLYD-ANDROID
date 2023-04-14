package chat.hola.com.app.dagger.module;


import com.kotlintestgradle.data.repository.order.GetOrderDetailsRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.order.GetOrderDetailsUseCase;
import com.kotlintestgradle.repository.GetOrderDetailsRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class HistoryOrderDetailUseCaseModule {
  @Provides
  public GetOrderDetailsRepository getRecentlyViewedProductsRepository(
      GetOrderDetailsRepositoryImpl repository) {
    return repository;
  }

  @Provides
  public UseCase<GetOrderDetailsUseCase.RequestValues,
        GetOrderDetailsUseCase.ResponseValues> getRecentlyViewedProducts(
      GetOrderDetailsUseCase prodCatUseCase) {
    return prodCatUseCase;
  }
}
