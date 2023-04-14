package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class SizeDetails implements ValidItem, Parcelable {

  @SerializedName("colourId")
  @Expose
  private String colourId;

  @SerializedName("image")
  @Expose
  private String image;

  @SerializedName("size")
  @Expose
  private ArrayList<String> size;

  @SerializedName("isPrimary")
  @Expose
  private boolean isPrimary;

  @SerializedName("childProductId")
  @Expose
  private String childProductId;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("unitId")
  @Expose
  private String unitId;

  public SizeDetails(String colourId, String image, ArrayList<String> size, boolean isPrimary,
      String childProductId, String name, String unitId) {
    this.colourId = colourId;
    this.image = image;
    this.size = size;
    this.isPrimary = isPrimary;
    this.childProductId = childProductId;
    this.name = name;
    this.unitId = unitId;
  }

  protected SizeDetails(Parcel in) {
    colourId = in.readString();
    image = in.readString();
    size = in.createStringArrayList();
    isPrimary = in.readByte() != 0;
    childProductId = in.readString();
    name = in.readString();
    unitId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(colourId);
    dest.writeString(image);
    dest.writeStringList(size);
    dest.writeByte((byte) (isPrimary ? 1 : 0));
    dest.writeString(childProductId);
    dest.writeString(name);
    dest.writeString(unitId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SizeDetails> CREATOR = new Creator<SizeDetails>() {
    @Override
    public SizeDetails createFromParcel(Parcel in) {
      return new SizeDetails(in);
    }

    @Override
    public SizeDetails[] newArray(int size) {
      return new SizeDetails[size];
    }
  };

  public String getColourId() {
    return colourId;
  }

  public void setColourId(String colourId) {
    this.colourId = colourId;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public ArrayList<String> getSize() {
    return size;
  }

  public void setSize(ArrayList<String> size) {
    this.size = size;
  }

  public boolean getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(boolean isPrimary) {
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

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
