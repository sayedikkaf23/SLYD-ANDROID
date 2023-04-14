package com.kotlintestgradle.repository;


import com.kotlintestgradle.interactor.order.GetPackageDetailsUseCase;

import io.reactivex.Single;

public interface GetPackageDetailsRepository {
  Single<GetPackageDetailsUseCase.ResponseValues> getPackageDetails(String productOrderId,
                                                                    String packageID);
}
