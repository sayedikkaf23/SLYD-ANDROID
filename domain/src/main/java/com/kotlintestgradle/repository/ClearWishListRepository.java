package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.ClearWishListUseCase;
import io.reactivex.Single;

public interface ClearWishListRepository {

  Single<ClearWishListUseCase.ResponseValues> clearWishList();
}

