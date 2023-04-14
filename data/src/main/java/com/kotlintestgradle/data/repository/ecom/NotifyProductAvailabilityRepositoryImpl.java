package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.cart.NotifyProductAvailabilityUseCase;
import com.kotlintestgradle.remote.model.request.ecom.NotifyProductAvailabilityRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.NotifyProductAvailabilityRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class NotifyProductAvailabilityRepositoryImpl extends BaseRepository implements
    NotifyProductAvailabilityRepository {

  private DataSource mDataSource;
  private PreferenceManager mPreferenceManager;
  private SuccessMapper mMapper = new SuccessMapper();

  @Inject
  public NotifyProductAvailabilityRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<NotifyProductAvailabilityUseCase.ResponseValues> notifyProductAvailabilityRepository(
      String childProductId, String email, String parentProductId) {
    return mDataSource.api().handler().notifyProductAvailability(mPreferenceManager.getAuthToken(),
        new NotifyProductAvailabilityRequest(childProductId, email, parentProductId)).flatMap(
        new Function<CommonModel, SingleSource<?
            extends NotifyProductAvailabilityUseCase.ResponseValues>>() {
            @Override
            public SingleSource<? extends NotifyProductAvailabilityUseCase.ResponseValues> apply(
                CommonModel model) throws Exception {
              return Single.just(
                  new NotifyProductAvailabilityUseCase.ResponseValues(mMapper.mapper(model)));
            }
          });
  }
}