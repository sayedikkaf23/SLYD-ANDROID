package com.kotlintestgradle.model.ecom.common;

public class SellerData {
  private String productId;
  private String retailerPrice;
  private String supplierName;
  private String rating;
  private String currency;

  public SellerData(String retailerPrice, String supplierName, String rating, String currency,
      String productId) {
    this.retailerPrice = retailerPrice;
    this.supplierName = supplierName;
    this.rating = rating;
    this.currency = currency;
    this.productId = productId;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getRetailerPrice() {
    return retailerPrice;
  }

  public void setRetailerPrice(String retailerPrice) {
    this.retailerPrice = retailerPrice;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }
}
