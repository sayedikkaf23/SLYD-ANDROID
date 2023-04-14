package com.kotlintestgradle.repository;


import com.kotlintestgradle.interactor.order.GetOrderHistoryUseCase;

import io.reactivex.Single;

public interface GetOrderHistoryRepository {

  Single<GetOrderHistoryUseCase.ResponseValues> getOrderHistory(int limit, int skip, int status,
                                                                int orderType, int storeType, int orderBy, String search, String orderTime);
}
