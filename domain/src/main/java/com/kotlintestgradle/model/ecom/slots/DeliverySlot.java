package com.kotlintestgradle.model.ecom.slots;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliverySlot implements Parcelable {

  private String storeId;
  private String slotId;

  public DeliverySlot(String storeId, String slotId) {
    this.storeId = storeId;
    this.slotId = slotId;
  }

  protected DeliverySlot(Parcel in) {
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

  public static final Creator<DeliverySlot> CREATOR = new Creator<DeliverySlot>() {
    @Override
    public DeliverySlot createFromParcel(Parcel in) {
      return new DeliverySlot(in);
    }

    @Override
    public DeliverySlot[] newArray(int size) {
      return new DeliverySlot[size];
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
