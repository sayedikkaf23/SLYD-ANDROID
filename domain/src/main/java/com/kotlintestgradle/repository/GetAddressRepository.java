package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.address.GetAddressUseCase;
import io.reactivex.Single;

public interface GetAddressRepository {
  Single<GetAddressUseCase.ResponseValues> getFilteredProductList();
}
