package chat.hola.com.app.dagger.module;


import com.kotlintestgradle.data.repository.order.GetPackageDetailsRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.order.GetPackageDetailsUseCase;
import com.kotlintestgradle.repository.GetPackageDetailsRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class GetPackageDetUseCaseModule {
  @Provides
  GetPackageDetailsRepository provideRepository(
      GetPackageDetailsRepositoryImpl getAddressRepository) {
    return getAddressRepository;
  }

  @Provides
  public UseCase<GetPackageDetailsUseCase.RequestValues, GetPackageDetailsUseCase.ResponseValues> loginDetailsUseCase(
      GetPackageDetailsUseCase getAddressUseCase) {
    return getAddressUseCase;
  }
}
