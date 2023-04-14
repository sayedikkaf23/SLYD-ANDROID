package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.GetWishListUseCase;
import io.reactivex.Single;

public interface GetWishListRepository {

  Single<GetWishListUseCase.ResponseValues> getWishListProducts(int sortType, String searchQuery);
}
