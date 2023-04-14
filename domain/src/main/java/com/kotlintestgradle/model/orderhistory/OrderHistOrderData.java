package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderHistOrderData implements Parcelable {

  private String storeOrderId;
  private ArrayList<String> productOrderId;

  public OrderHistOrderData(String storeOrderId, ArrayList<String> productOrderId) {
    this.storeOrderId = storeOrderId;
    this.productOrderId = productOrderId;
  }

  protected OrderHistOrderData(Parcel in) {
    storeOrderId = in.readString();
    productOrderId = in.createStringArrayList();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(storeOrderId);
    dest.writeStringList(productOrderId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistOrderData> CREATOR = new Creator<OrderHistOrderData>() {
    @Override
    public OrderHistOrderData createFromParcel(Parcel in) {
      return new OrderHistOrderData(in);
    }

    @Override
    public OrderHistOrderData[] newArray(int size) {
      return new OrderHistOrderData[size];
    }
  };

  public String getStoreOrderId() {
    return storeOrderId;
  }

  public void setStoreOrderId(String storeOrderId) {
    this.storeOrderId = storeOrderId;
  }

  public ArrayList<String> getProductOrderId() {
    return productOrderId;
  }

  public void setProductOrderId(ArrayList<String> productOrderId) {
    this.productOrderId = productOrderId;
  }
}
