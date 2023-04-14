package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistShippingData implements Parcelable {
  private String name;

  private String id;

  private String trackingId;

  public OrderHistShippingData(String name, String id, String trackingId) {
    this.name = name;
    this.id = id;
    this.trackingId = trackingId;
  }

  protected OrderHistShippingData(Parcel in) {
    name = in.readString();
    id = in.readString();
    trackingId = in.readString();
  }

  public static final Creator<OrderHistShippingData> CREATOR =
      new Creator<OrderHistShippingData>() {
        @Override
        public OrderHistShippingData createFromParcel(Parcel in) {
          return new OrderHistShippingData(in);
        }

        @Override
        public OrderHistShippingData[] newArray(int size) {
          return new OrderHistShippingData[size];
        }
      };

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(id);
    dest.writeString(trackingId);
  }
}
