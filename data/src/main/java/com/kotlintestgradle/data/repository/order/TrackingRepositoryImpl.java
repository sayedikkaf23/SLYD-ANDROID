package com.kotlintestgradle.data.repository.order;



import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.TrackingMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.order.TrackingOrderUseCase;
import com.kotlintestgradle.remote.model.response.tracking.TrackingListOrderStatusData;
import com.kotlintestgradle.repository.TrackingRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class TrackingRepositoryImpl extends BaseRepository implements TrackingRepository {
  private DataSource mDataSource;
  private TrackingMapper mMapper = new TrackingMapper();
    private PreferenceManager mPreferenceManager;
  @Inject
  public TrackingRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
      mPreferenceManager = dataSource.preference();

  }

  @Override
  public Single<TrackingOrderUseCase.ResponseValues> trackOrder(
      String productOrderId) {
    return mDataSource.api().handler().getDeliveryStatus(2,mPreferenceManager.getAuthToken(),
            mPreferenceManager.getLanguageCode(),"USD","$",
        productOrderId).flatMap(
        new Function<TrackingListOrderStatusData, SingleSource<?
            extends TrackingOrderUseCase.ResponseValues>>() {
            @Override
          public SingleSource<? extends TrackingOrderUseCase.ResponseValues> apply(
                TrackingListOrderStatusData details) {
              return Single.just(
                new TrackingOrderUseCase.ResponseValues(mMapper.mapper(details)));
            }
          });
  }
}
