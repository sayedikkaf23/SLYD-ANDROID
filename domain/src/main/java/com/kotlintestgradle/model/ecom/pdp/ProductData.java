package com.kotlintestgradle.model.ecom.pdp;

import java.util.ArrayList;

public class ProductData {

  private ArrayList<ProductModel> data;

  private String message;

  public ProductData(ArrayList<ProductModel> data, String message) {
    this.data = data;
    this.message = message;
  }

  public ArrayList<ProductModel> getData() {
    return data;
  }

  public void setData(ArrayList<ProductModel> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
