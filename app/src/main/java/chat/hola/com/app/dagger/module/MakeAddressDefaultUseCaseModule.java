package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.MakeAddressDefaultRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.address.MakeAddressDefaultUseCase;
import com.kotlintestgradle.repository.MakeAddressDefaultRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class MakeAddressDefaultUseCaseModule {

  @Provides
  MakeAddressDefaultRepository provideRepository(
      MakeAddressDefaultRepositoryImpl makeAddressDefaultRepository) {
    return makeAddressDefaultRepository;
  }

  @Provides
  public UseCase<MakeAddressDefaultUseCase.RequestValues,
        MakeAddressDefaultUseCase.ResponseValues> getLoginDetailsUseCase(
      MakeAddressDefaultUseCase makeAddressDefaultUseCase) {
    return makeAddressDefaultUseCase;
  }
}
