package com.kotlintestgradle.remote.model.response.tracking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class TrackingListDetails  implements Parcelable, ValidItem {
  @SerializedName("data")
  @Expose
  private TrackingListOrderStatusData data;
  @SerializedName("message")
  @Expose
  private String message;

  public TrackingListDetails(TrackingListOrderStatusData data, String message) {
    this.data = data;
    this.message = message;
  }

  protected TrackingListDetails(Parcel in) {
    data = in.readParcelable(TrackingListOrderStatusData.class.getClassLoader());
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(data, flags);
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TrackingListDetails> CREATOR = new Creator<TrackingListDetails>() {
    @Override
    public TrackingListDetails createFromParcel(Parcel in) {
      return new TrackingListDetails(in);
    }

    @Override
    public TrackingListDetails[] newArray(int size) {
      return new TrackingListDetails[size];
    }
  };

  public TrackingListOrderStatusData getData() {
    return data;
  }

  public void setData(TrackingListOrderStatusData data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
