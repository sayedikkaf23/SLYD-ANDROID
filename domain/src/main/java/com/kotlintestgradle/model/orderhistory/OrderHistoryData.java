package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderHistoryData implements Parcelable {

  private int count;

  private String message;

  private ArrayList<OrderHistoryItemData> data;

  public OrderHistoryData(int count, String message,
      ArrayList<OrderHistoryItemData> data) {
    this.count = count;
    this.message = message;
    this.data = data;
  }

  protected OrderHistoryData(Parcel in) {
    count = in.readInt();
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(count);
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistoryData> CREATOR = new Creator<OrderHistoryData>() {
    @Override
    public OrderHistoryData createFromParcel(Parcel in) {
      return new OrderHistoryData(in);
    }

    @Override
    public OrderHistoryData[] newArray(int size) {
      return new OrderHistoryData[size];
    }
  };

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ArrayList<OrderHistoryItemData> getData() {
    return data;
  }

  public void setData(
      ArrayList<OrderHistoryItemData> data) {
    this.data = data;
  }
}
