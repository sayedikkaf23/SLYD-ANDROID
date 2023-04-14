package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.HelpRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.HelpUseCase;
import com.kotlintestgradle.repository.HelpRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class HelpUseCaseModule {
  @Provides
  HelpRepository provideRepository(HelpRepositoryImpl helpRepository) {
    return helpRepository;
  }

  @Provides
  public UseCase<HelpUseCase.RequestValues, HelpUseCase.ResponseValues> provideUseCase(
      HelpUseCase filterUseCase) {
    return filterUseCase;
  }
}
