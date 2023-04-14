package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.AddProductToWishListUseCase;
import io.reactivex.Single;

public interface AddProductToWishListRepository {

  Single<AddProductToWishListUseCase.ResponseValues> addProductWishList(String productId,
      String ipAddress, double latitude, double longitude, String cityId, String countryId,String userId);
}
