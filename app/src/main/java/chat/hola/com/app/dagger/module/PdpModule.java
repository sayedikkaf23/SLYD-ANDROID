package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.AddProductToWishListRepositoryImpl;
import com.kotlintestgradle.data.repository.ecom.LikeDisLikeReviewRepositoryImpl;
import com.kotlintestgradle.data.repository.ecom.ProductDetailsRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.AddProductToWishListUseCase;
import com.kotlintestgradle.interactor.ecom.LikeDisLikeReviewUseCase;
import com.kotlintestgradle.interactor.ecom.cart.PdpUseCase;
import com.kotlintestgradle.repository.AddProductToWishListRepository;
import com.kotlintestgradle.repository.ProductDetailsRepository;
import dagger.Module;
import dagger.Provides;
import com.kotlintestgradle.repository.LikeDisLikeReviewRepository;

@Module
public class PdpModule {
  @Provides
  ProductDetailsRepository provideRepository(
      ProductDetailsRepositoryImpl productDetailsRepository) {
    return productDetailsRepository;
  }

  @Provides
  public UseCase<PdpUseCase.RequestValues, PdpUseCase.ResponseValues> getLoginDetailsUseCase(
      PdpUseCase pdpUseCase) {
    return pdpUseCase;
  }

  @Provides
  LikeDisLikeReviewRepository provideLikeDislikeRepository(
      LikeDisLikeReviewRepositoryImpl reportReviewRepository) {
    return reportReviewRepository;
  }

  @Provides
  UseCase<LikeDisLikeReviewUseCase.RequestValues, LikeDisLikeReviewUseCase.ResponseValues> getLikeDisLikeUseCase(
      LikeDisLikeReviewUseCase likeDisLikeReviewUseCase) {
    return likeDisLikeReviewUseCase;
  }

  @Provides
  AddProductToWishListRepository provideLAddWishListRepository(
      AddProductToWishListRepositoryImpl addProductToWishListRepository) {
    return addProductToWishListRepository;
  }

  @Provides
  UseCase<AddProductToWishListUseCase.RequestValues, AddProductToWishListUseCase.ResponseValues> getAddWishListUseCase(
      AddProductToWishListUseCase addProductToWishListUseCase) {
    return addProductToWishListUseCase;
  }
 /* @Provides
  ReportReviewRepository provideReportReviewRepository(
      ReportReviewRepositoryImpl reportReviewRepository) {
    return reportReviewRepository;
  }

  @Provides
  UseCase<ReportReviewUseCase.RequestValues, ReportReviewUseCase.ResponseValues> getReportReviewUseCase(
      ReportReviewUseCase reportReviewUseCase) {
    return reportReviewUseCase;
  }


 */
}
