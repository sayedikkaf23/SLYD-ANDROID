package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.address.GetDeliveryFeeUseCase;
import io.reactivex.Single;

public interface GetDeliveryFeeRepository {
  Single<GetDeliveryFeeUseCase.ResponseValues> calculateDeliveryFee(String cartId,
      String deliveryAddressId);
}
