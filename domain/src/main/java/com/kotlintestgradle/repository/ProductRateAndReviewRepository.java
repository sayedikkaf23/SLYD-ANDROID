package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.ProductRateAndReviewUseCase;
import io.reactivex.Single;
import java.util.ArrayList;

public interface ProductRateAndReviewRepository {

  Single<ProductRateAndReviewUseCase.ResponseValues> rateAndReview(int type, int reviewType,
      String productId,
      String attributeId, String reviewTitle,
      String reviewDescription, double rating, ArrayList<String> images, String city,
      String country, String ipAddress, double latitude, double longitude, String sellerReview,
      String sellerId, String orderId,
      String driverId, String driverReview);
}
