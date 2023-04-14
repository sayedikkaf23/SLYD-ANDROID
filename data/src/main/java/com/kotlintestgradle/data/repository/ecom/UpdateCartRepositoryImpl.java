package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.data.utils.DataConstants.ADD;
import static com.kotlintestgradle.data.utils.DataConstants.DELETE;
import static com.kotlintestgradle.data.utils.DataConstants.MODIFY;
import static com.kotlintestgradle.data.utils.DataConstants.ZERO;
import static com.kotlintestgradle.remote.RemoteConstants.ECOM_CAT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.ECOM_FLOW;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.cache.entity.UserCart;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.mapper.UpdateCartRequestMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.data.repository.observable.CartDataObservable;
import com.kotlintestgradle.interactor.ecom.cart.UpdateCartUseCase;
import com.kotlintestgradle.model.ecom.getcart.CartData;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.remote.model.request.ecom.UpdateCartRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.UpdateCartRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class UpdateCartRepositoryImpl extends BaseRepository implements UpdateCartRepository {
  private DataSource mDataSource;
  private SuccessMapper mMapper = new SuccessMapper();
  private UpdateCartRequestMapper mCartRequestMapper = new UpdateCartRequestMapper();
  private PreferenceManager mPreferenceManager;
  private DatabaseManager mDatabaseManager;

  @Inject
  public UpdateCartRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    mDatabaseManager = dataSource.db();
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<UpdateCartUseCase.ResponseValues> updateCart(int userType, String centralProductId,
       final String productId, String unitId, final String storeId, int storeTypeId, final String productName,
       String estimatedProductPrice, String extraNotes, final int newQuantity, int oldQuantity,
       final int action, int cartType, String userIp, double latitude, double longitude,String currencySymbol,
      String currCode) {
    return mDataSource.api().handler().updateCart(mPreferenceManager.getAuthToken(),
            new UpdateCartRequest(userType, centralProductId, productId, unitId, storeId,
                ECOM_CAT_ID, storeTypeId, productName, ZERO, extraNotes,
                    newQuantity, oldQuantity, action, cartType, userIp, latitude, longitude),
        currencySymbol,currCode).flatMap(
            new Function<CommonModel, SingleSource<? extends UpdateCartUseCase.ResponseValues>>() {
              @Override
              public SingleSource<? extends UpdateCartUseCase.ResponseValues> apply(
                  CommonModel model)
                      throws Exception {
                Thread t = new Thread() {
                  public void run() {
                    if (action == ADD || action == MODIFY) {
                      UserCart cart = new UserCart(productId, storeId, productName, newQuantity, ECOM_FLOW);
                      mDatabaseManager.cart().insertItemToCart(cart);
                    } else if (action == DELETE) {
                      mDatabaseManager.cart().deleteCartItemById(productId);
                    }
                    CartDataObservable.getInstance().postData(
                            new CartData(action, productId, newQuantity));
                  }
                };
                t.start();
                return Single.just(new UpdateCartUseCase.ResponseValues(mMapper.mapper(model)));
              }
            });
  }

  @Override
  public Single<UpdateCartUseCase.ResponseValues> updateCart(int userType, String centralProductId,
     final String productId, String unitId, final String storeId, int storeTypeId,
     final String productName, String estimatedProductPrice, String extraNotes, final int newQuantity, int oldQuantity,
     final int action, int cartType, String userIp, double latitude, double longitude, PdpOfferData offer,
      String currencySymbol, String currCode) {
    return mDataSource.api().handler().updateCart(mPreferenceManager.getAuthToken(),
        new UpdateCartRequest(userType,
            centralProductId, productId, unitId, storeId, ECOM_CAT_ID, storeTypeId,
            productName, ZERO,
            extraNotes, newQuantity, oldQuantity, action, cartType, userIp, latitude,
            longitude, mCartRequestMapper.getOffer(offer)),currencySymbol,currCode).flatMap(
        new Function<CommonModel, SingleSource<? extends UpdateCartUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends UpdateCartUseCase.ResponseValues> apply(CommonModel model)
              throws Exception {
            Thread t = new Thread() {
              public void run() {
                if (action == ADD || action == MODIFY) {
                  UserCart cart = new UserCart(productId, storeId, productName, newQuantity, ECOM_FLOW);
                  mDatabaseManager.cart().insertItemToCart(cart);
                } else if (action == DELETE) {
                  mDatabaseManager.cart().deleteCartItemById(productId);
                }
                CartDataObservable.getInstance().postData(
                    new CartData(action, productId, newQuantity));
              }
            };
            t.start();
            return Single.just(new UpdateCartUseCase.ResponseValues(mMapper.mapper(model)));
          }
        });
  }
}
