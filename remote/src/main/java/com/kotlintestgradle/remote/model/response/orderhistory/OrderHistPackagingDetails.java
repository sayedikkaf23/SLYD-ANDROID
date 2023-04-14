package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistPackagingDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("unitType")
  private String unitType;
  @Expose
  @SerializedName("packingType")
  private String packingType;
 /* @Expose
  @SerializedName("unitValue")
  private String unitValue;*/

  public OrderHistPackagingDetails(String unitType, String packingType) {
    this.unitType = unitType;
    this.packingType = packingType;
/*
    this.unitValue = unitValue;
*/
  }

  protected OrderHistPackagingDetails(Parcel in) {
    unitType = in.readString();
    packingType = in.readString();
/*
    unitValue = in.readString();
*/
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unitType);
    dest.writeString(packingType);
/*
    dest.writeString(unitValue);
*/
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistPackagingDetails> CREATOR =
      new Creator<OrderHistPackagingDetails>() {
        @Override
        public OrderHistPackagingDetails createFromParcel(Parcel in) {
          return new OrderHistPackagingDetails(in);
        }

        @Override
        public OrderHistPackagingDetails[] newArray(int size) {
          return new OrderHistPackagingDetails[size];
        }
      };

  public String getUnitType() {
    return unitType;
  }

  public void setUnitType(String unitType) {
    this.unitType = unitType;
  }

  public String getPackingType() {
    return packingType;
  }

  public void setPackingType(String packingType) {
    this.packingType = packingType;
  }

  @Override
  public boolean isValid() {
    return true;
  }

 /* public String getUnitValue() {
    return unitValue;
  }

  public void setUnitValue(String unitValue) {
    this.unitValue = unitValue;
  }*/
}
