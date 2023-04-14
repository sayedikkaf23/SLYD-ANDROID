package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.PlaceOrderUseCase;
import com.kotlintestgradle.remote.model.request.ecom.PlaceOrderRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.PlaceOrderRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import javax.inject.Inject;

public class PlaceOrderRepositoryImpl extends BaseRepository implements PlaceOrderRepository {
  private DataSource mDataSource;
  private SuccessMapper mMapper = new SuccessMapper();
  private DatabaseManager mDatabaseManager;
  private PreferenceManager mPreferenceManager;

  @Inject
  public PlaceOrderRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mDataSource = dataSource;
    mDatabaseManager = dataSource.db();
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<PlaceOrderUseCase.ResponseValues> placeOrder(String cartId, String addressId,
      String billingAddressId,
      int paymentType, String cardId, boolean payByWallet, String coupon, String couponId,
      double discount,
      double latitude, double longitude, String ipAddress, String extraNote, int storeType,
      int orderType,String userId,String currencySymb,String currencyCode) {
    return mDataSource.api().handler().placeOrder(mPreferenceManager.getAuthToken(), new PlaceOrderRequest(
        cartId, addressId, billingAddressId, paymentType, cardId, payByWallet, coupon, couponId,
        discount,
        latitude, longitude,
        ipAddress, extraNote, userId, storeType, orderType),currencySymb,currencyCode).flatMap(
        new Function<CommonModel, SingleSource<? extends PlaceOrderUseCase.ResponseValues>>() {
            @Override
          public SingleSource<? extends PlaceOrderUseCase.ResponseValues> apply(CommonModel model)
              throws Exception {
              mDatabaseManager.cart().clearCart();
              return Single.just(new PlaceOrderUseCase.ResponseValues(mMapper.mapper(model)));
            }
          });
  }
}
