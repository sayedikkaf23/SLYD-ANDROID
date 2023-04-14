package com.kotlintestgradle.model.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;

public class ColorsData implements Parcelable {

  public static final Creator<ColorsData> CREATOR = new Creator<ColorsData>() {
    @Override
    public ColorsData createFromParcel(Parcel in) {
      return new ColorsData(in);
    }

    @Override
    public ColorsData[] newArray(int size) {
      return new ColorsData[size];
    }
  };
  private String image;
  private String isPrimary;
  private String childProductId;
  private String name;
  private String rgb;

  public ColorsData(String image, String isPrimary, String childProductId, String name,
      String rgb) {
    this.image = image;
    this.isPrimary = isPrimary;
    this.childProductId = childProductId;
    this.name = name;
    this.rgb = rgb;
  }

  protected ColorsData(Parcel in) {
    image = in.readString();
    isPrimary = in.readString();
    childProductId = in.readString();
    name = in.readString();
    rgb = in.readString();
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(String isPrimary) {
    this.isPrimary = isPrimary;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRgb() {
    return rgb;
  }

  public void setRgb(String rgb) {
    this.rgb = rgb;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(image);
    parcel.writeString(isPrimary);
    parcel.writeString(childProductId);
    parcel.writeString(name);
    parcel.writeString(rgb);
  }
}
