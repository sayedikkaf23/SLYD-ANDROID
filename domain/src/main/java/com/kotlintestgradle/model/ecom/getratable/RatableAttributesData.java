package com.kotlintestgradle.model.ecom.getratable;

public class RatableAttributesData {

  private String attributeName;

  private String attributeId;

  private double attributeRating;

  public RatableAttributesData(String attributeName, String attributeId, int attributeRating) {
    this.attributeName = attributeName;
    this.attributeId = attributeId;
    this.attributeRating = attributeRating;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public double getAttributeRating() {
    return attributeRating;
  }

  public void setAttributeRating(double attributeRating) {
    this.attributeRating = attributeRating;
  }
}
