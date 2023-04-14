package com.kotlintestgradle.data.repository.order;


import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.OrderDetailsMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.order.GetOrderDetailsUseCase;
import com.kotlintestgradle.model.orderdetails.MasterOrderData;
import com.kotlintestgradle.remote.model.response.orderdetails.MasterOrderDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.MasterOrdersDetail;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.StoreOrdersItem;
import com.kotlintestgradle.repository.GetOrderDetailsRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class GetOrderDetailsRepositoryImpl extends BaseRepository implements
        GetOrderDetailsRepository {
  private DataSource mDataSource;
  private PreferenceManager mPreferenceManager;
    private OrderDetailsMapper mMapper = new OrderDetailsMapper();

  @Inject
  public GetOrderDetailsRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<GetOrderDetailsUseCase.ResponseValues> getOrderDetails(String type,
                                                                       String orderId) {
    return mDataSource.api().handler().getOrderDetails(1,mPreferenceManager.getAuthToken(),
            mPreferenceManager.getLanguageCode(),type,"$","USD", orderId).flatMap(
        new Function<MasterOrderDetails, SingleSource<?
            extends GetOrderDetailsUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends GetOrderDetailsUseCase.ResponseValues> apply(
              MasterOrderDetails masterOrderDetails) throws Exception {
              return Single.just(
                      new GetOrderDetailsUseCase.ResponseValues(mMapper.mapper(masterOrderDetails,
                              8)));
          }
        });

  }

    @Override
    public Single<GetOrderDetailsUseCase.ResponseValues> getMasterOrderDetail(String type, String orderId) {
        return mDataSource.api().handler().getMasterOrderDetail(1,mPreferenceManager.getAuthToken(),
                mPreferenceManager.getLanguageCode(),"USD","$", type, orderId).flatMap(
                new Function<MasterOrdersDetail, SingleSource<?
                        extends GetOrderDetailsUseCase.ResponseValues>>() {
                    @Override
                    public SingleSource<? extends GetOrderDetailsUseCase.ResponseValues> apply(
                            MasterOrdersDetail masterOrderDetails) throws Exception {
                        return Single.just(
                                new GetOrderDetailsUseCase.ResponseValues(mMapper.mapper(masterOrderDetails)));
                    }
                });
    }

  @Override
  public Single<GetOrderDetailsUseCase.ResponseValues> getStoreOrderDetail(String type, String orderId) {
    return mDataSource.api().handler().getStoreOrderDetail(1,mPreferenceManager.getAuthToken(),
            mPreferenceManager.getLanguageCode(),"USD","$", type, orderId).flatMap(
            new Function<StoreOrdersItem, SingleSource<?
                    extends GetOrderDetailsUseCase.ResponseValues>>() {
              @Override
              public SingleSource<? extends GetOrderDetailsUseCase.ResponseValues> apply(
                      StoreOrdersItem storeOrdersItem) throws Exception {
                return Single.just(
                        new GetOrderDetailsUseCase.ResponseValues(mMapper.mapper(storeOrdersItem)));
              }
            });
  }
}
