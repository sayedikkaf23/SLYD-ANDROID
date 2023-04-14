package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.cart.NotifyProductAvailabilityUseCase;
import io.reactivex.Single;

public interface NotifyProductAvailabilityRepository {

  Single<NotifyProductAvailabilityUseCase.ResponseValues> notifyProductAvailabilityRepository(
      String childProductId, String email, String parentProductId);
}
