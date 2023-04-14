package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CartDeliveryDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("deliveryFee")
  private String deliveryFee;

  @Expose
  @SerializedName("deliveryByDeliveryPartner")
  private String deliveryByDeliveryPartner;

  @Expose
  @SerializedName("deliveryByFleetDriver")
  private String deliveryByFleetDriver;

  @Expose
  @SerializedName("time")
  private String time;

  public CartDeliveryDetails(String deliveryFee, String deliveryByDeliveryPartner,
      String deliveryByFleetDriver, String time) {
    this.deliveryFee = deliveryFee;
    this.deliveryByDeliveryPartner = deliveryByDeliveryPartner;
    this.deliveryByFleetDriver = deliveryByFleetDriver;
    this.time = time;
  }

  protected CartDeliveryDetails(Parcel in) {
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

  public static final Creator<CartDeliveryDetails> CREATOR = new Creator<CartDeliveryDetails>() {
    @Override
    public CartDeliveryDetails createFromParcel(Parcel in) {
      return new CartDeliveryDetails(in);
    }

    @Override
    public CartDeliveryDetails[] newArray(int size) {
      return new CartDeliveryDetails[size];
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

  @Override
  public boolean isValid() {
    return true;
  }
}

