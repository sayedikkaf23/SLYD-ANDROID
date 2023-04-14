package com.kotlintestgradle.model.ecom.sellerlist;

import java.util.ArrayList;

public class SellerListData {
  private ArrayList<SellerListDataItem> data;

  private String message;

  public SellerListData(
      ArrayList<SellerListDataItem> data, String message) {
    this.data = data;
    this.message = message;
  }

  public ArrayList<SellerListDataItem> getData() {
    return data;
  }

  public void setData(ArrayList<SellerListDataItem> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
