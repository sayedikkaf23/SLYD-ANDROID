package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.remote.RemoteConstants.ECOM_FLOW;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.CommonMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.GetSimilarProductsUseCase;
import com.kotlintestgradle.remote.model.response.ecom.similarproduct.GetSimilarProducts;
import com.kotlintestgradle.repository.GetSimilarProductsRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class GetSimilarProductsRepositoryImpl extends BaseRepository implements
    GetSimilarProductsRepository {

  private DataSource mDataSource;
  private PreferenceManager mPreference;
  private CommonMapper mMapper = new CommonMapper();

  @Inject
  public GetSimilarProductsRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    this.mPreference = dataSource.preference();
  }

  @Override
  public Single<GetSimilarProductsUseCase.ResponseValues> getSimilarProducts(String itemId, int limit) {
        int loginType = 2;
        String zoneId = "";
        String userId = mPreference.getUserId();
        return mDataSource.api().handler().getSimilarProducts(mPreference.getAuthToken(), itemId, limit,
                loginType, zoneId, userId).flatMap(
                new Function<GetSimilarProducts, SingleSource<? extends GetSimilarProductsUseCase.ResponseValues>>() {
                  @Override
                  public SingleSource<? extends GetSimilarProductsUseCase.ResponseValues> apply(
                          GetSimilarProducts similarProducts) throws Exception {
                    return Single.just(
                            new GetSimilarProductsUseCase.ResponseValues(mMapper.map(similarProducts,
                                    ECOM_FLOW)));
                  }
                });
  }
}

