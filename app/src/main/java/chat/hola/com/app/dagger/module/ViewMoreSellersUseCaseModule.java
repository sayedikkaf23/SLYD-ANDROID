package chat.hola.com.app.dagger.module;

import com.kotlintestgradle.data.repository.ecom.GetSellerListRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.GetSellerListUseCase;
import com.kotlintestgradle.repository.GetSellerListRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class ViewMoreSellersUseCaseModule {
  @Provides
  public GetSellerListRepository provideRepository(
      GetSellerListRepositoryImpl viewMoreRepository) {
    return viewMoreRepository;
  }

  @Provides
  public UseCase<GetSellerListUseCase.RequestValues, GetSellerListUseCase.ResponseValues> getCase(
      GetSellerListUseCase getProfileDetailsUseCase) {
    return getProfileDetailsUseCase;
  }
}
