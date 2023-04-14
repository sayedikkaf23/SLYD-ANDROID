package com.kotlintestgradle.model.ecom.getratable;

import com.customer.domain.model.getratable.DriverData;
import java.util.ArrayList;

public class RatableData {
  private ArrayList<RatableProductData> reviewData;
  private SellerData sellerData;
  private DriverData driverData;
  private String message;

  public RatableData(
      ArrayList<RatableProductData> reviewData, SellerData sellerData, String message,DriverData driverData) {
    this.reviewData = reviewData;
    this.sellerData = sellerData;
    this.message = message;
    this.driverData = driverData;
  }

  public ArrayList<RatableProductData> getReviewData() {
    return reviewData;
  }

  public void setReviewData(
      ArrayList<RatableProductData> reviewData) {
    this.reviewData = reviewData;
  }

  public SellerData getSellerData() {
    return sellerData;
  }

  public void setSellerData(SellerData sellerData) {
    this.sellerData = sellerData;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public DriverData getDriverData() {
    return driverData;
  }

  public void setDriverData(DriverData driverData) {
    this.driverData = driverData;
  }
}
