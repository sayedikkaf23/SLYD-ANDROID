package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistShippingDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("id;")
  private String id;
  @Expose
  @SerializedName("trackingId")
  private String trackingId;

  public OrderHistShippingDetails(String name, String id, String trackingId) {
    this.name = name;
    this.id = id;
    this.trackingId = trackingId;
  }

  protected OrderHistShippingDetails(Parcel in) {
    name = in.readString();
    id = in.readString();
    trackingId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(id);
    dest.writeString(trackingId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistShippingDetails> CREATOR =
      new Creator<OrderHistShippingDetails>() {
        @Override
        public OrderHistShippingDetails createFromParcel(Parcel in) {
          return new OrderHistShippingDetails(in);
        }

        @Override
        public OrderHistShippingDetails[] newArray(int size) {
          return new OrderHistShippingDetails[size];
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
  public boolean isValid() {
    return true;
  }
}
