package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.DeleteWishListProductUseCase;
import io.reactivex.Single;

public interface DeleteWishListProductRepository {

  Single<DeleteWishListProductUseCase.ResponseValues> deleteWishListProduct(String productId,
      String ipAddress, double latitude, double longitude, String cityId, String countryId);
}
