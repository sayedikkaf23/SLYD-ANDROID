package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class QuantityDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("unit")
  private String unit;

  @Expose
  @SerializedName("value")
  private String value;

  public QuantityDetails(String unit, String value) {
    this.unit = unit;
    this.value = value;
  }

  protected QuantityDetails(Parcel in) {
    unit = in.readString();
    value = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unit);
    dest.writeString(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<QuantityDetails> CREATOR = new Creator<QuantityDetails>() {
    @Override
    public QuantityDetails createFromParcel(Parcel in) {
      return new QuantityDetails(in);
    }

    @Override
    public QuantityDetails[] newArray(int size) {
      return new QuantityDetails[size];
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
  public boolean isValid() {
    return true;
  }
}
