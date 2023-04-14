package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.IpAddressToLocationMapper;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.IpAddressToLocationUseCase;
import com.kotlintestgradle.remote.model.response.ecom.location.IpAddressToLocationDetails;
import com.kotlintestgradle.repository.IpAddressToLocationRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class IpAddressToLocationRepositoryImpl extends BaseRepository implements
    IpAddressToLocationRepository {

  private DataSource mDataSource;
  private IpAddressToLocationMapper mMapper = new IpAddressToLocationMapper();

  @Inject
  public IpAddressToLocationRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mDataSource = dataSource;
  }

  @Override
  public Single<IpAddressToLocationUseCase.ResponseValues> ipAddressToLocation(String ipAddress,String currencySymbol,
      String currencyCode) {
    return mDataSource.api().handler().ipAddressToLocation(ipAddress,currencySymbol,currencyCode).flatMap(
        new Function<IpAddressToLocationDetails, SingleSource<?
            extends IpAddressToLocationUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends IpAddressToLocationUseCase.ResponseValues> apply(
              IpAddressToLocationDetails ipAddressToLocationDetails) throws Exception {
            return Single.just(new IpAddressToLocationUseCase.ResponseValues(
                mMapper.mapper(ipAddressToLocationDetails)));
          }
        });
  }
}
