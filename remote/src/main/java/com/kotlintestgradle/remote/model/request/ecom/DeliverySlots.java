package com.kotlintestgradle.remote.model.request.ecom;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverySlots implements Parcelable {

  @Expose
  @SerializedName("storeId")
  private String storeId;

  @Expose
  @SerializedName("slotId")
  private String slotId;

  public DeliverySlots(String storeId, String slotId) {
    this.storeId = storeId;
    this.slotId = slotId;
  }

  protected DeliverySlots(Parcel in) {
    storeId = in.readString();
    slotId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(storeId);
    dest.writeString(slotId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<DeliverySlots> CREATOR = new Creator<DeliverySlots>() {
    @Override
    public DeliverySlots createFromParcel(Parcel in) {
      return new DeliverySlots(in);
    }

    @Override
    public DeliverySlots[] newArray(int size) {
      return new DeliverySlots[size];
    }
  };

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getSlotId() {
    return slotId;
  }

  public void setSlotId(String slotId) {
    this.slotId = slotId;
  }
}
