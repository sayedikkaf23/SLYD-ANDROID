package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.BasicSizeDetails;
import java.util.ArrayList;

public class VariantDetails implements ValidItem, Parcelable {

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

  @SerializedName("rgb")
  @Expose
  private String rgb;

  @SerializedName("sizeData")
  @Expose
  private ArrayList<BasicSizeDetails> sizeData;

  public VariantDetails(String image, String childProductId, boolean isPrimary, String name,
      String unitId, String rgb, ArrayList<BasicSizeDetails> sizeData) {
    this.image = image;
    this.childProductId = childProductId;
    this.isPrimary = isPrimary;
    this.name = name;
    this.unitId = unitId;
    this.rgb = rgb;
    this.sizeData = sizeData;
  }

  protected VariantDetails(Parcel in) {
    image = in.readString();
    childProductId = in.readString();
    isPrimary = in.readByte() != 0;
    name = in.readString();
    unitId = in.readString();
    rgb = in.readString();
    sizeData = in.createTypedArrayList(BasicSizeDetails.CREATOR);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(image);
    dest.writeString(childProductId);
    dest.writeByte((byte) (isPrimary ? 1 : 0));
    dest.writeString(name);
    dest.writeString(unitId);
    dest.writeString(rgb);
    dest.writeTypedList(sizeData);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<VariantDetails> CREATOR = new Creator<VariantDetails>() {
    @Override
    public VariantDetails createFromParcel(Parcel in) {
      return new VariantDetails(in);
    }

    @Override
    public VariantDetails[] newArray(int size) {
      return new VariantDetails[size];
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

  public ArrayList<BasicSizeDetails> getSizeData() {
    return sizeData;
  }

  public void setSizeData(ArrayList<BasicSizeDetails> sizeData) {
    this.sizeData = sizeData;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}

