package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.GetCartProductsUseCase;
import io.reactivex.Single;

public interface GetCartProductsRepository {

  Single<GetCartProductsUseCase.ResponseValues> getCartProducts(boolean isDine,String userId,
      String currencySymb,String currencyCode);
}
