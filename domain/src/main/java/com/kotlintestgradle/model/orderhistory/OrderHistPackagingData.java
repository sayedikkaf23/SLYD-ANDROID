package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistPackagingData implements Parcelable {
  private String unitType;

  private String packingType;

/*
  private String unitValue;
*/

  public OrderHistPackagingData(String unitType, String packingType) {
    this.unitType = unitType;
    this.packingType = packingType;
/*
    this.unitValue = unitValue;
*/
  }

  protected OrderHistPackagingData(Parcel in) {
    unitType = in.readString();
    packingType = in.readString();
/*
    unitValue = in.readString();
*/
  }

  public static final Creator<OrderHistPackagingData> CREATOR =
      new Creator<OrderHistPackagingData>() {
        @Override
        public OrderHistPackagingData createFromParcel(Parcel in) {
          return new OrderHistPackagingData(in);
        }

        @Override
        public OrderHistPackagingData[] newArray(int size) {
          return new OrderHistPackagingData[size];
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

 /* public String getUnitValue() {
    return unitValue;
  }

  public void setUnitValue(String unitValue) {
    this.unitValue = unitValue;
  }*/

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unitType);
    dest.writeString(packingType);
/*
    dest.writeString(unitValue);
*/
  }
}
