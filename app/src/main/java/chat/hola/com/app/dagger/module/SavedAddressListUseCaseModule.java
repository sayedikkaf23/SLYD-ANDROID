package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.GetAddressRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.address.GetAddressUseCase;
import com.kotlintestgradle.repository.GetAddressRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class SavedAddressListUseCaseModule {
  @Provides
  GetAddressRepository provideRepository(GetAddressRepositoryImpl getAddressRepository) {
    return getAddressRepository;
  }

  @Provides
  public UseCase<GetAddressUseCase.RequestValues, GetAddressUseCase.ResponseValues> getLoginDetailsUseCase(
      GetAddressUseCase getAddressUseCase) {
    return getAddressUseCase;
  }
}
