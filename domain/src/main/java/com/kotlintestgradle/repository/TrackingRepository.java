package com.kotlintestgradle.repository;


import com.kotlintestgradle.interactor.order.TrackingOrderUseCase;

import io.reactivex.Single;

public interface TrackingRepository {
  Single<TrackingOrderUseCase.ResponseValues> trackOrder(String productOrderId);
}
