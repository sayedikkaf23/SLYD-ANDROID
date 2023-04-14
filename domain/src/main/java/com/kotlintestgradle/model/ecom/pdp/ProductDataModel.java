package com.kotlintestgradle.model.ecom.pdp;

import com.kotlintestgradle.model.ecom.pdp.substitutes.SubstitutesData;

public class ProductDataModel {

  private ReviewData review;

  private ProductData productData;

  private SubstitutesData substituteData;

  public ProductDataModel(ReviewData review, ProductData productData, SubstitutesData data) {
    this.review = review;
    this.productData = productData;
    this.substituteData = data;
  }

  public ProductDataModel(ReviewData review, ProductData productData) {
    this.review = review;
    this.productData = productData;
  }

  public ReviewData getReview() {
    return review;
  }

  public void setReview(ReviewData review) {
    this.review = review;
  }

  public ProductData getProductData() {
    return productData;
  }

  public void setProductData(ProductData productData) {
    this.productData = productData;
  }

  public SubstitutesData getSubstituteData() {
    return substituteData;
  }

  public void setSubstituteData(SubstitutesData substituteData) {
    this.substituteData = substituteData;
  }
}
