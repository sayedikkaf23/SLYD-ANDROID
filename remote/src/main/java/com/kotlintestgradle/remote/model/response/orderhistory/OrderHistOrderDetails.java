package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class OrderHistOrderDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("storeOrderId")
  private String storeOrderId;
  @Expose
  @SerializedName("productOrderId")
  private ArrayList<String> productOrderId;

  public OrderHistOrderDetails(String storeOrderId,
      ArrayList<String> productOrderId) {
    this.storeOrderId = storeOrderId;
    this.productOrderId = productOrderId;
  }

  protected OrderHistOrderDetails(Parcel in) {
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

  public static final Creator<OrderHistOrderDetails> CREATOR =
      new Creator<OrderHistOrderDetails>() {
        @Override
        public OrderHistOrderDetails createFromParcel(Parcel in) {
          return new OrderHistOrderDetails(in);
        }

        @Override
        public OrderHistOrderDetails[] newArray(int size) {
          return new OrderHistOrderDetails[size];
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

  @Override
  public boolean isValid() {
    return true;
  }
}
