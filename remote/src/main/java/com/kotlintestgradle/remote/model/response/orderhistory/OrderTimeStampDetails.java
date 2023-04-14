package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderTimeStampDetails implements Parcelable, ValidItem {
  public static final Creator<OrderTimeStampDetails> CREATOR =
      new Creator<OrderTimeStampDetails>() {
        @Override
        public OrderTimeStampDetails createFromParcel(Parcel in) {
          return new OrderTimeStampDetails(in);
        }

        @Override
        public OrderTimeStampDetails[] newArray(int size) {
          return new OrderTimeStampDetails[size];
        }
      };
  @Expose
  @SerializedName("inDispatch")
  private String inDispatch;
  @Expose
  @SerializedName("created")
  private String created;
  @Expose
  @SerializedName("readyForPickup")
  private String readyForPickup;
  @Expose
  @SerializedName("accepted")
  private String accepted;
  @Expose
  @SerializedName("cancelled")
  private String cancelled;
  @Expose
  @SerializedName("completed")
  private String completed;
  @Expose
  @SerializedName("packed")
  private String packed;

  public OrderTimeStampDetails(String inDispatch, String created, String readyForPickup,
      String accepted, String cancelled, String completed, String packed) {
    this.inDispatch = inDispatch;
    this.created = created;
    this.readyForPickup = readyForPickup;
    this.accepted = accepted;
    this.cancelled = cancelled;
    this.completed = completed;
    this.packed = packed;
  }

  protected OrderTimeStampDetails(Parcel in) {
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

  @Override
  public boolean isValid() {
    return true;
  }
}
