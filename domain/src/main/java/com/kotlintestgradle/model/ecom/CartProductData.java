package com.kotlintestgradle.model.ecom;

public class CartProductData {
  private String parentProductId;
  private String childProductId;
  private String unitId;
  private int cartQuantity;
  private int action;

  public CartProductData(String parentProductId, int cartQuantity, int action) {
    this.parentProductId = parentProductId;
    this.cartQuantity = cartQuantity;
    this.action = action;
  }

  public int getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public String getParentProductId() {
    return parentProductId;
  }

  public void setParentProductId(String parentProductId) {
    this.parentProductId = parentProductId;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public int getCartQuantity() {
    return cartQuantity;
  }

  public void setCartQuantity(int cartQuantity) {
    this.cartQuantity = cartQuantity;
  }
}
