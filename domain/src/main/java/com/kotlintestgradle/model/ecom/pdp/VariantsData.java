package com.kotlintestgradle.model.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class VariantsData implements Parcelable {
  private String image;
  private String childProductId;
  private boolean isPrimary;
  private String name;
  private String unitId;
  private String rgb;
  private ArrayList<SizeData> sizeData;

  public VariantsData(String image, String childProductId,
      boolean isPrimary, String name, String unitId, String rgb,
      ArrayList<SizeData> sizeData) {
    this.image = image;
    this.childProductId = childProductId;
    this.isPrimary = isPrimary;
    this.name = name;
    this.unitId = unitId;
    this.rgb = rgb;
    this.sizeData = sizeData;
  }

  protected VariantsData(Parcel in) {
    image = in.readString();
    childProductId = in.readString();
    isPrimary = in.readByte() != 0;
    name = in.readString();
    unitId = in.readString();
    rgb = in.readString();
    sizeData = in.createTypedArrayList(SizeData.CREATOR);
  }

  public static final Creator<VariantsData> CREATOR = new Creator<VariantsData>() {
    @Override
    public VariantsData createFromParcel(Parcel in) {
      return new VariantsData(in);
    }

    @Override
    public VariantsData[] newArray(int size) {
      return new VariantsData[size];
    }
  };

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public boolean getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getRgb() {
    return rgb;
  }

  public void setRgb(String rgb) {
    this.rgb = rgb;
  }

  public ArrayList<SizeData> getSizeData() {
    return sizeData;
  }

  public void setSizeData(ArrayList<SizeData> sizeData) {
    this.sizeData = sizeData;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(image);
    parcel.writeString(childProductId);
    parcel.writeByte((byte) (isPrimary ? 1 : 0));
    parcel.writeString(name);
    parcel.writeString(unitId);
    parcel.writeString(rgb);
    parcel.writeTypedList(sizeData);
  }
}
