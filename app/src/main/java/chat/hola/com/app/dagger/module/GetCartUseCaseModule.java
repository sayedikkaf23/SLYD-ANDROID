package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.GetCartProductsRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.GetCartProductsUseCase;
import com.kotlintestgradle.repository.GetCartProductsRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class GetCartUseCaseModule {
  @Provides
  GetCartProductsRepository provideRepository(
      GetCartProductsRepositoryImpl getCartProductsRepository) {
    return getCartProductsRepository;
  }

  @Provides
  public UseCase<GetCartProductsUseCase.RequestValues, GetCartProductsUseCase.ResponseValues> getUseCase(
      GetCartProductsUseCase getCartProductsUseCase) {
    return getCartProductsUseCase;
  }
}
