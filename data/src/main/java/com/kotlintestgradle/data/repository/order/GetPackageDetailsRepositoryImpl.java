package com.kotlintestgradle.data.repository.order;


import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.OrderDetailsMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.order.GetPackageDetailsUseCase;
import com.kotlintestgradle.remote.model.response.orderdetails.OrderDetails;
import com.kotlintestgradle.repository.GetPackageDetailsRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class GetPackageDetailsRepositoryImpl extends BaseRepository implements
        GetPackageDetailsRepository {
  private DataSource mDataSource;
  private OrderDetailsMapper mMapper = new OrderDetailsMapper();
    private PreferenceManager mPreferenceManager;
  @Inject
  public GetPackageDetailsRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mDataSource = dataSource;
      mPreferenceManager = dataSource.preference();

  }

  @Override
  public Single<GetPackageDetailsUseCase.ResponseValues> getPackageDetails(String productOrderId,
                                                                           String packageID) {
    return mDataSource.api().handler().getPackageDetails(2,mPreferenceManager.getAuthToken(),
            mPreferenceManager.getLanguageCode(),"USD","$", packageID,
        productOrderId).flatMap(
        new Function<OrderDetails,
            SingleSource<? extends GetPackageDetailsUseCase.ResponseValues>>() {
            @Override
          public SingleSource<? extends GetPackageDetailsUseCase.ResponseValues> apply(
                OrderDetails orderDetails) throws Exception {
              return Single.just(new GetPackageDetailsUseCase.ResponseValues(
                mMapper.convertToOrderData(orderDetails)));
            }
          });
  }
}
