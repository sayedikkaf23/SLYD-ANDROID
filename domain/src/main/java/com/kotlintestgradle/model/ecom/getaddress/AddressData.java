package com.kotlintestgradle.model.ecom.getaddress;

import java.util.ArrayList;

public class AddressData {
  private ArrayList<AddressListItemData> data;

  private String message;

  public AddressData(
      ArrayList<AddressListItemData> data, String message) {
    this.data = data;
    this.message = message;
  }

  public ArrayList<AddressListItemData> getData() {
    return data;
  }

  public void setData(ArrayList<AddressListItemData> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "AddressData{" +
        "data=" + data +
        ", message='" + message + '\'' +
        '}';
  }
}
