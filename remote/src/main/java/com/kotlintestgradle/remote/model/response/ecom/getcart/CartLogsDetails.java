package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CartLogsDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("timeStampIso")
  private String timeStampIso;

  @Expose
  @SerializedName("statusMsg")
  private String statusMsg;

  @Expose
  @SerializedName("userType")
  private String userType;

  @Expose
  @SerializedName("userId")
  private String userId;

  @Expose
  @SerializedName("status")
  private String status;

  @Expose
  @SerializedName("timestamp")
  private String timestamp;

  public CartLogsDetails(String timeStampIso, String statusMsg, String userType,
      String userId, String status, String timestamp) {
    this.timeStampIso = timeStampIso;
    this.statusMsg = statusMsg;
    this.userType = userType;
    this.userId = userId;
    this.status = status;
    this.timestamp = timestamp;
  }

  protected CartLogsDetails(Parcel in) {
    timeStampIso = in.readString();
    statusMsg = in.readString();
    userType = in.readString();
    userId = in.readString();
    status = in.readString();
    timestamp = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(timeStampIso);
    dest.writeString(statusMsg);
    dest.writeString(userType);
    dest.writeString(userId);
    dest.writeString(status);
    dest.writeString(timestamp);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartLogsDetails> CREATOR = new Creator<CartLogsDetails>() {
    @Override
    public CartLogsDetails createFromParcel(Parcel in) {
      return new CartLogsDetails(in);
    }

    @Override
    public CartLogsDetails[] newArray(int size) {
      return new CartLogsDetails[size];
    }
  };

  public String getTimeStampIso() {
    return timeStampIso;
  }

  public void setTimeStampIso(String timeStampIso) {
    this.timeStampIso = timeStampIso;
  }

  public String getStatusMsg() {
    return statusMsg;
  }

  public void setStatusMsg(String statusMsg) {
    this.statusMsg = statusMsg;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
