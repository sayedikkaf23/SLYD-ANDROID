package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.GetSimilarProductsUseCase;
import io.reactivex.Single;

public interface GetSimilarProductsRepository {
  Single<GetSimilarProductsUseCase.ResponseValues> getSimilarProducts(String itemId, int limit);
}
