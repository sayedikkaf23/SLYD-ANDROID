package com.kotlintestgradle.data.mapper;

import com.customer.domain.model.getratable.DriverData;
import com.customer.remote.http.model.response.getratable.DriverDetails;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.getratable.RatableAttributesData;
import com.kotlintestgradle.model.ecom.getratable.RatableData;
import com.kotlintestgradle.model.ecom.getratable.RatableProductData;
import com.kotlintestgradle.model.ecom.getratable.SellerData;
import com.kotlintestgradle.model.ecom.getratable.UserReviewData;
import com.kotlintestgradle.remote.model.response.ecom.getratable.RatableDetails;
import com.kotlintestgradle.remote.model.response.ecom.getratable.RatableProducts;
import com.kotlintestgradle.remote.model.response.ecom.getratable.RatedAttributes;
import com.kotlintestgradle.remote.model.response.ecom.getratable.SellerDetails;
import com.kotlintestgradle.remote.model.response.ecom.getratable.UserReview;
import java.util.ArrayList;

public class GetRatableProductMapper {
  public RatableData mapper(RatableDetails details) {
    return new RatableData(convertToRatableProductData(details.getReviewData()),
        convertToSellerData(details.getSellerData()),
        details.getMessage(),convertToDriverData(details.getDriverData()));
  }

  private ArrayList<RatableProductData> convertToRatableProductData(
      ArrayList<RatableProducts> productsList) {
    ArrayList<RatableProductData> productData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(productsList)) {
      for (RatableProducts productsData : productsList) {
        RatableProductData data = new RatableProductData(productsData.getProductName(),
            productsData.getProductId(), productsData.getImage(),
            convertToUserReviewData(productsData.getUserReview()), productsData.getOrder());
        productData.add(data);
      }
    }
    return productData;
  }

  private ArrayList<RatableAttributesData> convertToRatableAttributesData(
      ArrayList<RatedAttributes> attributesList) {
    ArrayList<RatableAttributesData> attributesData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(attributesList)) {
      for (RatedAttributes attributes : attributesList) {
        attributesData.add(new RatableAttributesData(attributes.getAttributeName(),
            attributes.getAttributeId(), attributes.getAttributeRating()));
      }
    }
    return attributesData;
  }

  private UserReviewData convertToUserReviewData(
      UserReview userReview) {
    UserReviewData userReviewData = null;
    if (userReview != null) {
      userReviewData = new UserReviewData(userReview.getReviewDescription(),
          userReview.getRating(), userReview.getProductName(), userReview.getReviewTitle(),
          userReview.getImage(), convertToRatableAttributesData(userReview.getAttribute()));
    }
    return userReviewData;
  }

  private SellerData convertToSellerData(SellerDetails sellerDetails) {
    SellerData sellerData = null;
    if (sellerDetails != null) {
      sellerData = new SellerData(sellerDetails.getReviewDescription(),
          sellerDetails.getRating(), sellerDetails.getStoreName(),
          convertToRatableAttributesData(sellerDetails.getAttribute()),
          sellerDetails.getStoreId(), sellerDetails.getReviewTitle(),sellerDetails.getSellerReview());
    }
    return sellerData;
  }

  private DriverData convertToDriverData(DriverDetails driverDetails) {
    DriverData driverData = null;
    if (driverDetails != null && driverDetails.getDriverId()!=null) {
      driverData = new DriverData(driverDetails.getDriverName(),
              driverDetails.getProfilePic(), driverDetails.getDriverId(),
          driverDetails.getDriverReview(),driverDetails.getRating(),
          convertToRatableAttributesData(driverDetails.getAttribute())
              );
    }
    return driverData;
  }
}
