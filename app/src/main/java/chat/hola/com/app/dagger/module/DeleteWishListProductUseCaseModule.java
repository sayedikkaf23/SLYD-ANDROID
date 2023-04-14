package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.DeleteWishListProductRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.DeleteWishListProductUseCase;
import com.kotlintestgradle.repository.DeleteWishListProductRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class DeleteWishListProductUseCaseModule {

  @Provides
  DeleteWishListProductRepository provideRepository(
      DeleteWishListProductRepositoryImpl deleteWishListProductRepository) {
    return deleteWishListProductRepository;
  }

  @Provides
  public UseCase<DeleteWishListProductUseCase.RequestValues,
        DeleteWishListProductUseCase.ResponseValues> getUseCase(
      DeleteWishListProductUseCase deleteWishListProductUseCase) {
    return deleteWishListProductUseCase;
  }
}
