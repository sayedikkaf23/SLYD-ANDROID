package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.IpAddressToLocationUseCase;
import io.reactivex.Single;

public interface IpAddressToLocationRepository {

  Single<IpAddressToLocationUseCase.ResponseValues> ipAddressToLocation(String ipAddress,String currencySymbol,
      String currencyCode);

}
