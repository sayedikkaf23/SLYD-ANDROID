package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.ApplyPromoCodesRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.ApplyPromoCodesUseCase;
import com.kotlintestgradle.repository.ApplyPromoCodeRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplyPromoCodesUseCaseModule {
  @Provides
  ApplyPromoCodeRepository provideRepository(ApplyPromoCodesRepositoryImpl applyPromoCodesRepository) {
    return applyPromoCodesRepository;
  }
  @Provides
  public UseCase<ApplyPromoCodesUseCase.RequestValues, ApplyPromoCodesUseCase.ResponseValues> provideUseCase(
      ApplyPromoCodesUseCase promoCodesUseCase) {
    return promoCodesUseCase;
  }
}
