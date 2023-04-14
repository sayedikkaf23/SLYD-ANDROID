package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.EditAddressRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.address.EditAddressUseCase;
import com.kotlintestgradle.repository.EditAddressRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class EditAddressUseCaseModule {

  @Provides
  EditAddressRepository provideRepository(EditAddressRepositoryImpl editAddressRepository) {
    return editAddressRepository;
  }

  @Provides
  public UseCase<EditAddressUseCase.RequestValues, EditAddressUseCase.ResponseValues> getLoginDetailsUseCase(
      EditAddressUseCase editAddressUseCase) {
    return editAddressUseCase;
  }
}
