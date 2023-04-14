package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.cart.PdpUseCase;
import io.reactivex.Single;

public interface ProductDetailsRepository {
  Single<PdpUseCase.ResponseValues> getPdp(String productId, String parentProductId, double lat,
      double longitude,String ipAddress, String city , String country);
}
