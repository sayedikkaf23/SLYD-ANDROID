package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.AddProductToCartRepositoryImpl;
import com.kotlintestgradle.data.repository.ecom.UpdateCartRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.cart.AddProductToCartUseCase;
import com.kotlintestgradle.interactor.ecom.cart.UpdateCartUseCase;
import com.kotlintestgradle.repository.AddProductToCartRepository;
import com.kotlintestgradle.repository.UpdateCartRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class AddToCartUseCaseModule {

  @Provides
  AddProductToCartRepository provideRepository(AddProductToCartRepositoryImpl addAddressRepository) {
    return addAddressRepository;
  }

  @Provides
  public UseCase<AddProductToCartUseCase.RequestValues, AddProductToCartUseCase.ResponseValues> getLoginDetailsUseCase(
      AddProductToCartUseCase addAddressUseCase) {
    return addAddressUseCase;
  }

  @Provides
  UpdateCartRepository provideUpdateRepository(UpdateCartRepositoryImpl addAddressRepository) {
    return addAddressRepository;
  }

  @Provides
  public UseCase<UpdateCartUseCase.RequestValues, UpdateCartUseCase.ResponseValues> updateUseCase(
      UpdateCartUseCase addAddressUseCase) {
    return addAddressUseCase;
  }


}
