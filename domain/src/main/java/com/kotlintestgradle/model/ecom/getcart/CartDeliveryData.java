package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartDeliveryData implements Parcelable {

  private String deliveryFee;

  private String deliveryByDeliveryPartner;

  private String deliveryByFleetDriver;

  private String time;

  public CartDeliveryData(String deliveryFee, String deliveryByDeliveryPartner,
      String deliveryByFleetDriver, String time) {
    this.deliveryFee = deliveryFee;
    this.deliveryByDeliveryPartner = deliveryByDeliveryPartner;
    this.deliveryByFleetDriver = deliveryByFleetDriver;
    this.time = time;
  }

  protected CartDeliveryData(Parcel in) {
    deliveryFee = in.readString();
    deliveryByDeliveryPartner = in.readString();
    deliveryByFleetDriver = in.readString();
    time = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(deliveryFee);
    dest.writeString(deliveryByDeliveryPartner);
    dest.writeString(deliveryByFleetDriver);
    dest.writeString(time);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartDeliveryData> CREATOR = new Creator<CartDeliveryData>() {
    @Override
    public CartDeliveryData createFromParcel(Parcel in) {
      return new CartDeliveryData(in);
    }

    @Override
    public CartDeliveryData[] newArray(int size) {
      return new CartDeliveryData[size];
    }
  };

  public String getDeliveryFee() {
    return deliveryFee;
  }

  public void setDeliveryFee(String deliveryFee) {
    this.deliveryFee = deliveryFee;
  }

  public String getDeliveryByDeliveryPartner() {
    return deliveryByDeliveryPartner;
  }

  public void setDeliveryByDeliveryPartner(String deliveryByDeliveryPartner) {
    this.deliveryByDeliveryPartner = deliveryByDeliveryPartner;
  }

  public String getDeliveryByFleetDriver() {
    return deliveryByFleetDriver;
  }

  public void setDeliveryByFleetDriver(String deliveryByFleetDriver) {
    this.deliveryByFleetDriver = deliveryByFleetDriver;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
