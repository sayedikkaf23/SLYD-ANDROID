package com.kotlintestgradle.data.repository.order;

import android.util.Log;


import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.OrderHistoryMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.order.GetOrderHistoryUseCase;
import com.kotlintestgradle.remote.model.request.ecom.GetOrderHistoryRequest;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryDetails;
import com.kotlintestgradle.repository.GetOrderHistoryRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class GetOrderHistoryRepositoryImpl extends BaseRepository implements
        GetOrderHistoryRepository {
  private DataSource mDataSource;
    private PreferenceManager mPreferenceManager;
  private OrderHistoryMapper mMapper = new OrderHistoryMapper();

  @Inject
  public GetOrderHistoryRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mDataSource = dataSource;
      mPreferenceManager = dataSource.preference();

  }

  @Override
  public Single<GetOrderHistoryUseCase.ResponseValues> getOrderHistory(int limit, int skip,
                                                                       int status, int orderType, int storeType, int orderBy, String search, String orderTime) {
    Log.d("exe", "orderTime" + orderTime);
    return mDataSource.api().handler().getOrderHistory(2,mPreferenceManager.getAuthToken(),
        mPreferenceManager.getLanguageCode(),"USD","$",
        new GetOrderHistoryRequest(limit, skip, status, orderType, storeType, orderBy,
            search, orderTime)).flatMap(
        new Function<OrderHistoryDetails, SingleSource<?
            extends GetOrderHistoryUseCase.ResponseValues>>() {
                @Override
          public SingleSource<? extends GetOrderHistoryUseCase.ResponseValues> apply(
                    OrderHistoryDetails orderHistoryDetails) throws Exception {
                  return Single.just(
                new GetOrderHistoryUseCase.ResponseValues(mMapper.mapper(orderHistoryDetails)));
                }
              });
  }
}
