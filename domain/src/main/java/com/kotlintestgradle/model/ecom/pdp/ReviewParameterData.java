package com.kotlintestgradle.model.ecom.pdp;

public class ReviewParameterData {
  private String attributeId;
  private String name;
  private String rating;
  private Float mTotalStarRating;

  public ReviewParameterData(String attributeId, String name, String rating,
      Float totalStarRating) {
    this.attributeId = attributeId;
    this.name = name;
    this.rating = rating;
    mTotalStarRating = totalStarRating;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public Float getTotalStarRating() {
    return mTotalStarRating;
  }

  public void setTotalStarRating(Float totalStarRating) {
    this.mTotalStarRating = totalStarRating;
  }
}
