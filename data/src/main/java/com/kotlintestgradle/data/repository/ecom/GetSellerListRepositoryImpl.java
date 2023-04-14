package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SellerListMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.GetSellerListUseCase;
import com.kotlintestgradle.remote.model.request.ecom.GetSellerListRequest;
import com.kotlintestgradle.remote.model.response.ecom.sellerlist.SellerDetails;
import com.kotlintestgradle.repository.GetSellerListRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class GetSellerListRepositoryImpl extends BaseRepository implements GetSellerListRepository {

  private DataSource mDataSource;
  private PreferenceManager mPreferenceManager;
  private SellerListMapper mMapper = new SellerListMapper();

  @Inject
  public GetSellerListRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<GetSellerListUseCase.ResponseValues> getSellerList(String productId,
      String parentProductId, String loginType) {
    return mDataSource.api().handler().getSellerList(mPreferenceManager.getAuthToken(),
        new GetSellerListRequest(loginType, productId, parentProductId)).flatMap(
        new Function<SellerDetails, SingleSource<? extends GetSellerListUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends GetSellerListUseCase.ResponseValues> apply(
              SellerDetails sellerDetails) throws Exception {
            return Single.just(
                new GetSellerListUseCase.ResponseValues(mMapper.mapper(sellerDetails)));
          }
        });

  }
}
