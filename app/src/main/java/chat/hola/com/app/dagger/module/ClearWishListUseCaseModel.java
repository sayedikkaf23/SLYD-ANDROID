package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.ClearWishListRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.ClearWishListUseCase;
import com.kotlintestgradle.repository.ClearWishListRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class ClearWishListUseCaseModel {
  @Provides
  ClearWishListRepository provideRepository(ClearWishListRepositoryImpl clearWishListRepository) {
    return clearWishListRepository;
  }

  @Provides
  public UseCase<ClearWishListUseCase.RequestValues, ClearWishListUseCase.ResponseValues> getLoginDetailsUseCase(
      ClearWishListUseCase clearWishListUseCase) {
    return clearWishListUseCase;
  }
}
