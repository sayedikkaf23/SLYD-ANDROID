package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class LinkToUnit implements ValidItem, Parcelable {
  @SerializedName("image")
  @Expose
  private String image;

  @SerializedName("childProductId")
  @Expose
  private String childProductId;

  @SerializedName("isPrimary")
  @Expose
  private boolean isPrimary;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("unitId")
  @Expose
  private String unitId;

  @SerializedName("sizeData")
  @Expose
  private ArrayList<SizeDetails> sizeData;

  public LinkToUnit(String image, String childProductId, boolean isPrimary, String name,
      String unitId, ArrayList<SizeDetails> sizeData) {
    this.image = image;
    this.childProductId = childProductId;
    this.isPrimary = isPrimary;
    this.name = name;
    this.unitId = unitId;
    this.sizeData = sizeData;
  }

  protected LinkToUnit(Parcel in) {
    image = in.readString();
    childProductId = in.readString();
    isPrimary = in.readByte() != 0;
    name = in.readString();
    unitId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(image);
    dest.writeString(childProductId);
    dest.writeByte((byte) (isPrimary ? 1 : 0));
    dest.writeString(name);
    dest.writeString(unitId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<LinkToUnit> CREATOR = new Creator<LinkToUnit>() {
    @Override
    public LinkToUnit createFromParcel(Parcel in) {
      return new LinkToUnit(in);
    }

    @Override
    public LinkToUnit[] newArray(int size) {
      return new LinkToUnit[size];
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

  public ArrayList<SizeDetails> getSizeData() {
    return sizeData;
  }

  public void setSizeData(ArrayList<SizeDetails> sizeData) {
    this.sizeData = sizeData;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
