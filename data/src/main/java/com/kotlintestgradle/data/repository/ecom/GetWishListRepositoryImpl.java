package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.remote.RemoteConstants.ECOM_FLOW;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.WishListMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.GetWishListUseCase;
import com.kotlintestgradle.remote.model.response.ecom.wishlist.WishListDetails;
import com.kotlintestgradle.repository.GetWishListRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class GetWishListRepositoryImpl extends BaseRepository implements GetWishListRepository {

  private DataSource dataSource;
  private PreferenceManager mPreferenceManager;
  private WishListMapper mMapper = new WishListMapper();

  @Inject
  public GetWishListRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<GetWishListUseCase.ResponseValues> getWishListProducts(int sortType,
      String searchQuery) {
    return dataSource.api().handler().getWishList(mPreferenceManager.getAuthToken(), sortType,
        searchQuery).flatMap(
        new Function<WishListDetails, SingleSource<? extends GetWishListUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends GetWishListUseCase.ResponseValues> apply(
              WishListDetails wishListDetails) throws Exception {
            return Single.just(
                new GetWishListUseCase.ResponseValues(mMapper.mapper(wishListDetails,
                    ECOM_FLOW)));
          }
        });
  }
}
