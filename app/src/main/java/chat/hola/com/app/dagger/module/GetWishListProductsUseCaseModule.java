package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.GetWishListRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.GetWishListUseCase;
import com.kotlintestgradle.repository.GetWishListRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class GetWishListProductsUseCaseModule {

  @Provides
  GetWishListRepository provideRepository(GetWishListRepositoryImpl getWishListRepository) {
    return getWishListRepository;
  }

  @Provides
  public UseCase<GetWishListUseCase.RequestValues, GetWishListUseCase.ResponseValues> getLoginDetailsUseCase(
      GetWishListUseCase getWishListUseCase) {
    return getWishListUseCase;
  }
}
