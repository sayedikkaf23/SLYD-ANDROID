package com.kotlintestgradle.interactor.ecom;

public class CommonData {

  private String message;
  private String orderId = "";

  public CommonData(String message) {
    this.message = message;
  }

  public CommonData(String message, String orderId) {
    this.message = message;
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
