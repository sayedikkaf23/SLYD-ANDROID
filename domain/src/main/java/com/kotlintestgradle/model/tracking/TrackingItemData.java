package com.kotlintestgradle.model.tracking;

import android.os.Parcel;
import android.os.Parcelable;

public class TrackingItemData implements Parcelable {
  public static final Creator<TrackingItemData> CREATOR = new Creator<TrackingItemData>() {
    @Override
    public TrackingItemData createFromParcel(Parcel in) {
      return new TrackingItemData(in);
    }

    @Override
    public TrackingItemData[] newArray(int size) {
      return new TrackingItemData[size];
    }
  };
  private String statusText;
  private String time;
  private String formatedDate;
  private String status;
  private boolean viewStatus1;
  private boolean viewStatus2;

  public TrackingItemData(String statusText, String time, String formatedDate,
      String status) {
    this.statusText = statusText;
    this.time = time;
    this.formatedDate = formatedDate;
    this.status = status;
  }

  protected TrackingItemData(Parcel in) {
    statusText = in.readString();
    time = in.readString();
    formatedDate = in.readString();
    status = in.readString();
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getFormatedDate() {
    return formatedDate;
  }

  public void setFormatedDate(String formatedDate) {
    this.formatedDate = formatedDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public boolean isViewStatus1() {
    return viewStatus1;
  }

  public void setViewStatus1(boolean viewStatus1) {
    this.viewStatus1 = viewStatus1;
  }

  public boolean isViewStatus2() {
    return viewStatus2;
  }

  public void setViewStatus2(boolean viewStatus2) {
    this.viewStatus2 = viewStatus2;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(statusText);
    dest.writeString(time);
    dest.writeString(formatedDate);
    dest.writeString(status);
  }
}
