package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.IpAddressToLocationRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.IpAddressToLocationUseCase;
import com.kotlintestgradle.repository.IpAddressToLocationRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class IpAddressToLocationUseCaseModule {
  @Provides
  IpAddressToLocationRepository provideRepository(
      IpAddressToLocationRepositoryImpl addAddressRepository) {
    return addAddressRepository;
  }

  @Provides
  public UseCase<IpAddressToLocationUseCase.RequestValues,
        IpAddressToLocationUseCase.ResponseValues> getLoginDetailsUseCase(
      IpAddressToLocationUseCase addAddressUseCase) {
    return addAddressUseCase;
  }
}
