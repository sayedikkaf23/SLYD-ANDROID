package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.address.MakeAddressDefaultUseCase;
import io.reactivex.Single;

public interface MakeAddressDefaultRepository {

  Single<MakeAddressDefaultUseCase.ResponseValues> makeAddressDefault(String addressId);
}
