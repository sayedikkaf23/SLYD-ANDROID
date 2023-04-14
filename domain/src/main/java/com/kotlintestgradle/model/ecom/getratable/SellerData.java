package com.kotlintestgradle.model.ecom.getratable;

import java.util.ArrayList;

public class SellerData {
  private String reviewDescription;
  private String rating;
  private String storeName;
  private ArrayList<RatableAttributesData> attribute;
  private String storeId;
  private String sellerReview;
  private String reviewTitle;

  public SellerData(String reviewDescription, String rating, String storeName,
      ArrayList<RatableAttributesData> attribute, String storeId, String reviewTitle,
      String sellerReview) {
    this.reviewDescription = reviewDescription;
    this.rating = rating;
    this.storeName = storeName;
    this.attribute = attribute;
    this.storeId = storeId;
    this.reviewTitle = reviewTitle;
    this.sellerReview = sellerReview;
  }

  public String getReviewDescription() {
    return reviewDescription;
  }

  public void setReviewDescription(String reviewDescription) {
    this.reviewDescription = reviewDescription;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public ArrayList<RatableAttributesData> getAttribute() {
    return attribute;
  }

  public void setAttribute(ArrayList<RatableAttributesData> attribute) {
    this.attribute = attribute;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  public String getSellerReview() {
    return sellerReview;
  }

  public void setSellerReview(String sellerReview) {
    this.sellerReview = sellerReview;
  }
}
