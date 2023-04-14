package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;

public class QuantityData implements Parcelable {
  private String unit;
  private String value;

  public QuantityData(String unit, String value) {
    this.unit = unit;
    this.value = value;
  }

  protected QuantityData(Parcel in) {
    unit = in.readString();
    value = in.readString();
  }

  public static final Creator<QuantityData> CREATOR = new Creator<QuantityData>() {
    @Override
    public QuantityData createFromParcel(Parcel in) {
      return new QuantityData(in);
    }

    @Override
    public QuantityData[] newArray(int size) {
      return new QuantityData[size];
    }
  };

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unit);
    dest.writeString(value);
  }
}
