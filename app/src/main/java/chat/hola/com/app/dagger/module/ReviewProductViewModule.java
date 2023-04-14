package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.GetRatableAttributesRepositoryImpl;
import com.kotlintestgradle.data.repository.ecom.ProductRateAndReviewRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.GetRatableAttributesUseCase;
import com.kotlintestgradle.interactor.ecom.ProductRateAndReviewUseCase;
import com.kotlintestgradle.repository.GetRatableAttributesRepository;
import com.kotlintestgradle.repository.ProductRateAndReviewRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class ReviewProductViewModule {
  @Provides
  GetRatableAttributesRepository provideRepository(
      GetRatableAttributesRepositoryImpl getRatableAttributesRepository) {
    return getRatableAttributesRepository;
  }

  @Provides
  public UseCase<GetRatableAttributesUseCase.RequestValues,
        GetRatableAttributesUseCase.ResponseValues> getAttributesData(
      GetRatableAttributesUseCase pdpUseCase) {
    return pdpUseCase;
  }

  @Provides
  ProductRateAndReviewRepository provideReviewRepository(
      ProductRateAndReviewRepositoryImpl getRatableAttributesRepository) {
    return getRatableAttributesRepository;
  }

  @Provides
  public UseCase<ProductRateAndReviewUseCase.RequestValues,
      ProductRateAndReviewUseCase.ResponseValues> postReviewData(
      ProductRateAndReviewUseCase pdpUseCase) {
    return pdpUseCase;
  }
}
