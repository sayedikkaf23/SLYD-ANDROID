package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.address.DeleteAddressUseCase;
import io.reactivex.Single;

public interface DeleteAddressRepository {

  Single<DeleteAddressUseCase.ResponseValues> deleteAddress(String addressId);

}
