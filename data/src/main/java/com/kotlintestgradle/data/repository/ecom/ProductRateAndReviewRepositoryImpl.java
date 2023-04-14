package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.ProductRateAndReviewUseCase;
import com.kotlintestgradle.remote.model.request.ecom.ProductRateAndReviewRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.ProductRateAndReviewRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import javax.inject.Inject;

public class ProductRateAndReviewRepositoryImpl extends BaseRepository implements
    ProductRateAndReviewRepository {
  private DataSource dataSource;
  private PreferenceManager mPreferenceManager;
  private SuccessMapper mSuccessMapper = new SuccessMapper();

  @Inject
  public ProductRateAndReviewRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<ProductRateAndReviewUseCase.ResponseValues> rateAndReview(int type,int reviewType,
      String productId, String attributeId, String reviewTitle, String reviewDescription,
      double rating, ArrayList<String> images, String city, String country, String ipAddress,
      double latitude, double longitude,String sellerReview,String sellerId,String orderId,String driverId,
      String driverReview) {
    return dataSource.api().handler().rateAndReviewProduct(mPreferenceManager.getAuthToken(), new ProductRateAndReviewRequest(type,reviewType, productId, attributeId,
        reviewTitle, reviewDescription, rating, images, city, country,sellerReview,sellerId,orderId,driverId,driverReview),ipAddress,latitude,
        longitude).flatMap(
        new Function<CommonModel, SingleSource<?
            extends ProductRateAndReviewUseCase.ResponseValues>>() {
            @Override
            public SingleSource<? extends ProductRateAndReviewUseCase.ResponseValues> apply(
                CommonModel model) throws Exception {
              return Single.just(
                  new ProductRateAndReviewUseCase.ResponseValues(mSuccessMapper.mapper(model)));
            }
          });
  }
}
