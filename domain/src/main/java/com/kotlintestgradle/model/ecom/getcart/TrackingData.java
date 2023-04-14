package com.kotlintestgradle.model.ecom.getcart;

public class TrackingData {
  private String product_order_id;
  private String orderId;
  private String store_order_id;
  private String action;
  private String package_id;
  private String title;
  private String body;
  private String categoryIdentifier;

  public TrackingData(String product_order_id, String action) {
    this.product_order_id = product_order_id;
    this.action = action;
  }

  public String getProduct_order_id() {
    return product_order_id;
  }

  public void setProduct_order_id(String product_order_id) {
    this.product_order_id = product_order_id;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getStore_order_id() {
    return store_order_id;
  }

  public void setStore_order_id(String store_order_id) {
    this.store_order_id = store_order_id;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getPackage_id() {
    return package_id;
  }

  public void setPackage_id(String package_id) {
    this.package_id = package_id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getCategoryIdentifier() {
    return categoryIdentifier;
  }

  public void setCategoryIdentifier(String categoryIdentifier) {
    this.categoryIdentifier = categoryIdentifier;
  }
}
