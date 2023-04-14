package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.GetRatableProductMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.GetRatableAttributesUseCase;
import com.kotlintestgradle.remote.model.response.ecom.getratable.RatableDetails;
import com.kotlintestgradle.repository.GetRatableAttributesRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class GetRatableAttributesRepositoryImpl extends BaseRepository implements
    GetRatableAttributesRepository {

  private DataSource dataSource;
  private PreferenceManager mPreferenceManager;
  private GetRatableProductMapper mMapper = new GetRatableProductMapper();

  @Inject
  public GetRatableAttributesRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<GetRatableAttributesUseCase.ResponseValues> getRatableAttributes(String storeCatId,String productId,String driverId,String orderId) {
    return dataSource.api().handler().getRatableAttributes(mPreferenceManager.getAuthToken(),storeCatId,driverId,orderId, productId).flatMap(
        new Function<RatableDetails, SingleSource<?
            extends GetRatableAttributesUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends GetRatableAttributesUseCase.ResponseValues> apply(
              RatableDetails details) throws Exception {
            return Single.just(
                new GetRatableAttributesUseCase.ResponseValues(mMapper.mapper(details)));
          }
        });
  }
}
