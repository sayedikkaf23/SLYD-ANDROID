package com.kotlintestgradle.model.ecom.getratable;

public class RatableProductData {
  private String productName;
  private String productId;
  private String image;
  private UserReviewData userReview;
  private Boolean isOrder;

  public RatableProductData(String productName, String productId, String image,
      UserReviewData userReview, Boolean isOrder) {
    this.productName = productName;
    this.productId = productId;
    this.image = image;
    this.userReview = userReview;
    this.isOrder = isOrder;
  }

  public UserReviewData getUserReview() {
    return userReview;
  }

  public void setUserReview(UserReviewData userReview) {
    this.userReview = userReview;
  }

  public Boolean getOrder() {
    return isOrder;
  }

  public void setOrder(Boolean order) {
    isOrder = order;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
