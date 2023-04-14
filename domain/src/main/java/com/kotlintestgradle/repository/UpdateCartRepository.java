package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.cart.UpdateCartUseCase;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import io.reactivex.Single;

public interface UpdateCartRepository {

  Single<UpdateCartUseCase.ResponseValues> updateCart(int userType, String centralProductId,
      String productId, String unitId, String storeId, int storeTypeId, String productName,
      String estimatedProductPrice, String extraNotes, int newQuantity, int oldQuantity,
      int action, int cartType, String userIp, double latitude, double longitude,
      String currencySymbol, String currCode);

  Single<UpdateCartUseCase.ResponseValues> updateCart(int userType, String centralProductId,
      String productId, String unitId, String storeId, int storeTypeId, String productName,
      String estimatedProductPrice, String extraNotes, int newQuantity, int oldQuantity,
      int action, int cartType, String userIp, double latitude, double longitude,
      PdpOfferData offer,String currencySymbol, String currCode);
}
