package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.GetSellerListUseCase;
import io.reactivex.Single;

public interface GetSellerListRepository {

  Single<GetSellerListUseCase.ResponseValues> getSellerList(String productId,
      String parentProductId, String loginType);
}
