package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderTimeStampData implements Parcelable {

  private String inDispatch;

  private String created;

  private String readyForPickup;

  private String accepted;

  private String cancelled;

  private String completed;

  private String packed;

  public OrderTimeStampData(String inDispatch, String created, String readyForPickup,
      String accepted, String cancelled, String completed, String packed) {
    this.inDispatch = inDispatch;
    this.created = created;
    this.readyForPickup = readyForPickup;
    this.accepted = accepted;
    this.cancelled = cancelled;
    this.completed = completed;
    this.packed = packed;
  }

  protected OrderTimeStampData(Parcel in) {
    inDispatch = in.readString();
    created = in.readString();
    readyForPickup = in.readString();
    accepted = in.readString();
    cancelled = in.readString();
    completed = in.readString();
    packed = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(inDispatch);
    dest.writeString(created);
    dest.writeString(readyForPickup);
    dest.writeString(accepted);
    dest.writeString(cancelled);
    dest.writeString(completed);
    dest.writeString(packed);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderTimeStampData> CREATOR = new Creator<OrderTimeStampData>() {
    @Override
    public OrderTimeStampData createFromParcel(Parcel in) {
      return new OrderTimeStampData(in);
    }

    @Override
    public OrderTimeStampData[] newArray(int size) {
      return new OrderTimeStampData[size];
    }
  };

  public String getInDispatch() {
    return inDispatch;
  }

  public void setInDispatch(String inDispatch) {
    this.inDispatch = inDispatch;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getReadyForPickup() {
    return readyForPickup;
  }

  public void setReadyForPickup(String readyForPickup) {
    this.readyForPickup = readyForPickup;
  }

  public String getAccepted() {
    return accepted;
  }

  public void setAccepted(String accepted) {
    this.accepted = accepted;
  }

  public String getCancelled() {
    return cancelled;
  }

  public void setCancelled(String cancelled) {
    this.cancelled = cancelled;
  }

  public String getCompleted() {
    return completed;
  }

  public void setCompleted(String completed) {
    this.completed = completed;
  }

  public String getPacked() {
    return packed;
  }

  public void setPacked(String packed) {
    this.packed = packed;
  }
}
