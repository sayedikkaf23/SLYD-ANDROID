package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderStatusDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("statusText")
  private String statusText;
  @Expose
  @SerializedName("updatedOnTimeStamp")
  private String updatedOnTimeStamp;
  @Expose
  @SerializedName("updatedOn")
  private String updatedOn;

  @SerializedName("updatedBy")
  private String updatedBy;

  @Expose
  @SerializedName("status")
  private String status;

  public OrderStatusDetails(String statusText, String updatedOnTimeStamp, String updatedOn,
      String status) {
    this.statusText = statusText;
    this.updatedOnTimeStamp = updatedOnTimeStamp;
    this.updatedOn = updatedOn;
    this.status = status;
  }

  protected OrderStatusDetails(Parcel in) {
    statusText = in.readString();
    updatedOnTimeStamp = in.readString();
    updatedOn = in.readString();
    status = in.readString();
    updatedBy = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(statusText);
    dest.writeString(updatedOnTimeStamp);
    dest.writeString(updatedOn);
    dest.writeString(status);
    dest.writeString(updatedBy);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderStatusDetails> CREATOR = new Creator<OrderStatusDetails>() {
    @Override
    public OrderStatusDetails createFromParcel(Parcel in) {
      return new OrderStatusDetails(in);
    }

    @Override
    public OrderStatusDetails[] newArray(int size) {
      return new OrderStatusDetails[size];
    }
  };

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public String getUpdatedOnTimeStamp() {
    return updatedOnTimeStamp;
  }

  public void setUpdatedOnTimeStamp(String updatedOnTimeStamp) {
    this.updatedOnTimeStamp = updatedOnTimeStamp;
  }

  public String getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(String updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
