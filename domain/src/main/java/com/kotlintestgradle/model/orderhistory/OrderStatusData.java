package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderStatusData  implements Parcelable {

  private String statusText;

  private String updatedOnTimeStamp;

  private String updatedOn;
  private String updatedBy;

  private String status;

  public OrderStatusData(String statusText, String updatedOnTimeStamp, String updatedOn,
      String status,String updatedBy) {
    this.statusText = statusText;
    this.updatedOnTimeStamp = updatedOnTimeStamp;
    this.updatedOn = updatedOn;
    this.status = status;
    this.updatedBy = updatedBy;
  }

  protected OrderStatusData(Parcel in) {
    statusText = in.readString();
    updatedOnTimeStamp = in.readString();
    updatedOn = in.readString();
    status = in.readString();
    updatedBy = in.readString();
  }

  public static final Creator<OrderStatusData> CREATOR = new Creator<OrderStatusData>() {
    @Override
    public OrderStatusData createFromParcel(Parcel in) {
      return new OrderStatusData(in);
    }

    @Override
    public OrderStatusData[] newArray(int size) {
      return new OrderStatusData[size];
    }
  };

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
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(statusText);
    dest.writeString(updatedOnTimeStamp);
    dest.writeString(updatedOn);
    dest.writeString(status);
    dest.writeString(updatedBy);
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
