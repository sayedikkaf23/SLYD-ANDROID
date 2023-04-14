package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class OrderHistoryDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("count")
  private int count;

  @Expose
  @SerializedName("message")
  private String message;

  @Expose
  @SerializedName("data")
  private ArrayList<OrderHistoryItemDetails> data;

  public OrderHistoryDetails(int count, String message,
      ArrayList<OrderHistoryItemDetails> data) {
    this.count = count;
    this.message = message;
    this.data = data;
  }

  protected OrderHistoryDetails(Parcel in) {
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

  public static final Creator<OrderHistoryDetails> CREATOR = new Creator<OrderHistoryDetails>() {
    @Override
    public OrderHistoryDetails createFromParcel(Parcel in) {
      return new OrderHistoryDetails(in);
    }

    @Override
    public OrderHistoryDetails[] newArray(int size) {
      return new OrderHistoryDetails[size];
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

  public ArrayList<OrderHistoryItemDetails> getData() {
    return data;
  }

  public void setData(
      ArrayList<OrderHistoryItemDetails> data) {
    this.data = data;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
