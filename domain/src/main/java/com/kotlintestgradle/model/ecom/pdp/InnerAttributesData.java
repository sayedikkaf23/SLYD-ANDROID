package com.kotlintestgradle.model.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;

public class InnerAttributesData implements Parcelable {
  public static final Creator<InnerAttributesData> CREATOR = new Creator<InnerAttributesData>() {
    @Override
    public InnerAttributesData createFromParcel(Parcel in) {
      return new InnerAttributesData(in);
    }

    @Override
    public InnerAttributesData[] newArray(int size) {
      return new InnerAttributesData[size];
    }
  };
  private String name;
  private String value;

  protected InnerAttributesData(Parcel in) {
    name = in.readString();
    value = in.readString();
  }

  public InnerAttributesData(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    dest.writeString(name);
    dest.writeString(value);
  }
}
