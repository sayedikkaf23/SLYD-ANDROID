package com.kotlintestgradle.model.ecom.getratable;

import java.util.ArrayList;

public class UserReviewData {
  private String reviewDescription;
  private String rating;
  private String productName;
  private String reviewTitle;
  private ArrayList<RatableAttributesData> attribute;
  private ArrayList<String> image;

  public UserReviewData(String reviewDescription, String rating, String productName,
      String reviewTitle, ArrayList<String> image, ArrayList<RatableAttributesData> attribute) {
    this.reviewDescription = reviewDescription;
    this.rating = rating;
    this.productName = productName;
    this.reviewTitle = reviewTitle;
    this.image = image;
    this.attribute = attribute;
  }

  public ArrayList<String> getImage() {
    return image;
  }

  public void setImage(ArrayList<String> image) {
    this.image = image;
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

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  public ArrayList<RatableAttributesData> getAttribute() {
    return attribute;
  }

  public void setAttribute(
      ArrayList<RatableAttributesData> attribute) {
    this.attribute = attribute;
  }
}
