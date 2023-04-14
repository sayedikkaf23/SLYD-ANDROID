package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.cart.AddProductToCartUseCase;
import com.kotlintestgradle.model.ecom.AddOnsObject;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import io.reactivex.Single;
import java.util.ArrayList;

public interface AddProductToCartRepository {
  Single<AddProductToCartUseCase.ResponseValues> addProductToCart(int userType,
      String centralProductId, String productId, String unitId, String storeId, int quantity,
      int cartType, String notes, String userIp, String userPostCode, double latitude,
      double longitude, String city, int storeTypeId, String productName,
      String estimatedProductPrice, String extraNotes, PdpOfferData offerData, int action,
      String addtoCartId,
      ArrayList<AddOnsObject> addsOnDataRecords, String oldProductId);

  Single<AddProductToCartUseCase.ResponseValues> addProductToCart(int action, String addressId,
      String storeCategoryId);
}

