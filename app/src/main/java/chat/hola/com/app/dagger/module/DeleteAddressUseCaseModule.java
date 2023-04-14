package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.DeleteAddressRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.address.DeleteAddressUseCase;
import com.kotlintestgradle.repository.DeleteAddressRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class DeleteAddressUseCaseModule {
  @Provides
  DeleteAddressRepository provideRepository(DeleteAddressRepositoryImpl deleteAddressRepository) {
    return deleteAddressRepository;
  }

  @Provides
  public UseCase<DeleteAddressUseCase.RequestValues, DeleteAddressUseCase.ResponseValues> getUseCase(
      DeleteAddressUseCase deleteAddressUseCase) {
    return deleteAddressUseCase;
  }
}
