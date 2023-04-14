package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.AddAddressRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.address.AddAddressUseCase;
import com.kotlintestgradle.repository.AddAddressRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class AddAddressUseCaseModule {

  @Provides
  AddAddressRepository provideRepository(AddAddressRepositoryImpl addAddressRepository) {
    return addAddressRepository;
  }

  @Provides
  public UseCase<AddAddressUseCase.RequestValues, AddAddressUseCase.ResponseValues> getLoginDetailsUseCase(
      AddAddressUseCase addAddressUseCase) {
    return addAddressUseCase;
  }
}
