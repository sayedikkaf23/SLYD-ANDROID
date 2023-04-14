package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.data.utils.DataConstants.DEFAULT_LAT_LONG;
import static com.kotlintestgradle.data.utils.DataConstants.DEFAULT_STORE_TYPE;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;
import static com.kotlintestgradle.data.utils.DataConstants.ZERO;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.cache.entity.UserCart;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.cart.AddProductToCartUseCase;
import com.kotlintestgradle.model.ecom.AddOnsObject;
import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.remote.model.request.ecom.AddProductToCartRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.OfferName;
import com.kotlintestgradle.remote.model.response.ecom.pdp.PdpOfferDetails;
import com.kotlintestgradle.repository.AddProductToCartRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import javax.inject.Inject;

public class AddProductToCartRepositoryImpl extends BaseRepository implements
    AddProductToCartRepository {
  private DataSource mDataSource;
  private SuccessMapper mMapper = new SuccessMapper();
  private DatabaseManager mDatabaseManager;
  private PreferenceManager mPreferenceManager;

  @Inject
  public AddProductToCartRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mDataSource = dataSource;
    mDatabaseManager = dataSource.db();
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<AddProductToCartUseCase.ResponseValues> addProductToCart(int userType,
      final String centralProductId, final String productId, String unitId, final String storeId,
      final int quantity,
      int cartType, String notes, String userIp, String userPostCode, double latitude,
      double longitude, String city, int storeTypeId, final String productName,
      String estimatedProductPrice, String extraNotes, PdpOfferData offerData, final int action,
      final String addProductToCart,
      final ArrayList<AddOnsObject> addsOnDataRecords, String oldProductId) {
    AddProductToCartRequest request = null;
    if (offerData != null) {
      PdpOfferDetails offerDetails = new PdpOfferDetails(offerData.getApplicableOnStatus(),
          convertToImageDetails(offerData.getImages()), new OfferName(offerData.getOfferName()),
          offerData.getEndDateTimeISO(), offerData.getEndDateTime(),
          offerData.getGlobalClaimCount(), offerData.getStartDateTimeISO(),
          convertToImageDetails(offerData.getWebimages()), offerData.getStartDateTime(),
          offerData.getPerUserLimit(), offerData.getMinimumPurchaseQty(),
          offerData.getStatusString(), offerData.getOfferId(), offerData.getDiscountType(),
          offerData.getOfferFor(), offerData.getDiscountValue(), offerData.getApplicableOn(),
          offerData.getStatus());
      request = new AddProductToCartRequest(userType,
          centralProductId, productId, unitId, storeId, quantity,
          cartType, notes, userIp, mPreferenceManager.getStoreCatId(), userPostCode, latitude,
          longitude, city, storeTypeId, productName,
          estimatedProductPrice, extraNotes, offerDetails);
    } else {
      request = new AddProductToCartRequest(userType,
          centralProductId, productId, unitId, storeId, quantity,
          cartType, notes, userIp, mPreferenceManager.getStoreCatId(), userPostCode, latitude,
          longitude, city, storeTypeId, productName,
          estimatedProductPrice, extraNotes, action);
    }

    return mDataSource.api().handler().addProductToCart(mPreferenceManager.getAuthToken(), request).flatMap(
        (Function<CommonModel, SingleSource<? extends AddProductToCartUseCase.ResponseValues>>) model -> {
          if (action != 7) {
            Thread t = new Thread() {
              public void run() {
                mDatabaseManager.cart().insertItemToCart(
                    new UserCart(productId, storeId, productName, quantity, DEFAULT_STORE_TYPE));
              }
            };
            t.start();
          }
          return Single.just(new AddProductToCartUseCase.ResponseValues(mMapper.mapper(model)));
        });

  }

  @Override
  public Single<AddProductToCartUseCase.ResponseValues> addProductToCart(int action,
      String addressId,
      String storeCategoryId) {
    return mDataSource.api().handler().addProductToCart(mPreferenceManager.getAuthToken(), new AddProductToCartRequest(
        ZERO, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, ZERO, ZERO, EMPTY_STRING,
        null, EMPTY_STRING, EMPTY_STRING, DEFAULT_LAT_LONG, DEFAULT_LAT_LONG, EMPTY_STRING, ZERO,
        EMPTY_STRING, EMPTY_STRING, storeCategoryId, addressId, EMPTY_STRING, action))
        .flatMap(
            new Function<CommonModel, SingleSource<?
                extends AddProductToCartUseCase.ResponseValues>>() {
              @Override
              public SingleSource<? extends AddProductToCartUseCase.ResponseValues> apply(
                  CommonModel model) throws Exception {
                return Single.just(
                    new AddProductToCartUseCase.ResponseValues(mMapper.mapper(model)));
              }
            });
  }

  private ImagesDetails convertToImageDetails(ImageData imageData) {
    if (imageData != null) {
      return new ImagesDetails(imageData.getImageText(), imageData.getLarge(),
          imageData.getExtraLarge(), imageData.getSmall(), imageData.getAltText(),
          imageData.getMedium());
    }
    return null;
  }
}

