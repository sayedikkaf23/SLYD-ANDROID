package com.kotlintestgradle.repository;


import com.kotlintestgradle.interactor.order.GetOrderDetailsUseCase;

import io.reactivex.Single;

public interface GetOrderDetailsRepository {

  Single<GetOrderDetailsUseCase.ResponseValues> getOrderDetails(String type, String orderId);

  Single<GetOrderDetailsUseCase.ResponseValues> getMasterOrderDetail(String type, String orderId);

  Single<GetOrderDetailsUseCase.ResponseValues> getStoreOrderDetail(String type, String orderId);
}
