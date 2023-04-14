package com.kotlintestgradle.remote.model.response.tracking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

import java.util.ArrayList;

public class TrackingListOrderStatusData implements Parcelable, ValidItem  {
  @SerializedName("orderStatus")
  @Expose
  private ArrayList<TrackingItemDetails> orderStatus;

  protected TrackingListOrderStatusData(Parcel in) {
    orderStatus = in.createTypedArrayList(TrackingItemDetails.CREATOR);
  }

  public TrackingListOrderStatusData(
      ArrayList<TrackingItemDetails> orderStatus) {
    this.orderStatus = orderStatus;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(orderStatus);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TrackingListOrderStatusData> CREATOR = new Creator<TrackingListOrderStatusData>() {
    @Override
    public TrackingListOrderStatusData createFromParcel(Parcel in) {
      return new TrackingListOrderStatusData(in);
    }

    @Override
    public TrackingListOrderStatusData[] newArray(int size) {
      return new TrackingListOrderStatusData[size];
    }
  };

  public ArrayList<TrackingItemDetails> getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(
      ArrayList<TrackingItemDetails> orderStatus) {
    this.orderStatus = orderStatus;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
