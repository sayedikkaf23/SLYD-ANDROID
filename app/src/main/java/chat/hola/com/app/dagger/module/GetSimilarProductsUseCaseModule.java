package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.GetSimilarProductsRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.GetSimilarProductsUseCase;
import com.kotlintestgradle.repository.GetSimilarProductsRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class GetSimilarProductsUseCaseModule {

  @Provides
  GetSimilarProductsRepository provideRepository(
      GetSimilarProductsRepositoryImpl getSimilarProductsRepository) {
    return getSimilarProductsRepository;
  }

  @Provides
  public UseCase<GetSimilarProductsUseCase.RequestValues, GetSimilarProductsUseCase.ResponseValues> getSimilarProducts(
          GetSimilarProductsUseCase getSimilarProductsUseCase) {
    return getSimilarProductsUseCase;
  }

}
