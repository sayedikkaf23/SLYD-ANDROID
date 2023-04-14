package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.GetRatableAttributesUseCase;
import io.reactivex.Single;

public interface GetRatableAttributesRepository {
  Single<GetRatableAttributesUseCase.ResponseValues> getRatableAttributes(String storeCatId,
      String productId, String driverId, String orderId);
}
