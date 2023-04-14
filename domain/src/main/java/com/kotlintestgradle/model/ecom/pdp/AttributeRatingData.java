package com.kotlintestgradle.model.ecom.pdp;

public class AttributeRatingData {
  private String attributeId;

  private Float rating;

  private String attributeName;

  private Float mTotalStarRating;

  public AttributeRatingData(String attributeId, Float rating, String attributeName,
      Float totalStarRating) {
    this.attributeId = attributeId;
    this.rating = rating;
    this.attributeName = attributeName;
    this.mTotalStarRating = totalStarRating;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public Float getRating() {
    return rating;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public Float getTotalStarRating() {
    return mTotalStarRating;
  }

  public void setTotalStarRating(Float totalStarRating) {
    this.mTotalStarRating = totalStarRating;
  }
}
