package com.kotlintestgradle.remote.model.response.tracking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class TrackingItemDetails  implements Parcelable , ValidItem {
  @SerializedName("statusText")
  @Expose
  private String statusText;
  @SerializedName("time")
  @Expose
  private String time;
  @SerializedName("formatedDate")
  @Expose
  private String formatedDate;
  @SerializedName("status")
  @Expose
  private String status;

  public TrackingItemDetails(String statusText, String time, String formatedDate,
      String status) {
    this.statusText = statusText;
    this.time = time;
    this.formatedDate = formatedDate;
    this.status = status;
  }

  protected TrackingItemDetails(Parcel in) {
    statusText = in.readString();
    time = in.readString();
    formatedDate = in.readString();
    status = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(statusText);
    dest.writeString(time);
    dest.writeString(formatedDate);
    dest.writeString(status);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TrackingItemDetails> CREATOR = new Creator<TrackingItemDetails>() {
    @Override
    public TrackingItemDetails createFromParcel(Parcel in) {
      return new TrackingItemDetails(in);
    }

    @Override
    public TrackingItemDetails[] newArray(int size) {
      return new TrackingItemDetails[size];
    }
  };

  public String getStatusText ()
  {
    return statusText;
  }

  public void setStatusText (String statusText)
  {
    this.statusText = statusText;
  }

  public String getTime ()
  {
    return time;
  }

  public void setTime (String time)
  {
    this.time = time;
  }

  public String getFormatedDate ()
  {
    return formatedDate;
  }

  public void setFormatedDate (String formatedDate)
  {
    this.formatedDate = formatedDate;
  }

  public String getStatus ()
  {
    return status;
  }

  public void setStatus (String status)
  {
    this.status = status;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
