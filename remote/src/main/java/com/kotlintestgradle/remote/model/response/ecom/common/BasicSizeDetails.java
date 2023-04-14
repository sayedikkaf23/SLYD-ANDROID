package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class BasicSizeDetails implements ValidItem, Parcelable {
  @SerializedName("childProductId")
  @Expose
  private String childProductId;

  @SerializedName("isPrimary")
  @Expose
  private boolean isPrimary;

  @SerializedName("size")
  @Expose
  private String size;

  @SerializedName("colourId")
  @Expose
  private String colourId;

  @SerializedName("image")
  @Expose
  private String image;

  @SerializedName("availableStock")
  @Expose
  private String availableStock;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("outOfStock")
  @Expose
  private String outOfStock;

  @SerializedName("unitId")
  @Expose
  private String unitId;

  @SerializedName("rgb")
  @Expose
  private String rgb;

  @SerializedName("productName")
  @Expose
  private String productName;

  @SerializedName("visible")
  @Expose
  private boolean visible;

  public BasicSizeDetails(String childProductId, boolean isPrimary, String size) {
    this.childProductId = childProductId;
    this.isPrimary = isPrimary;
    this.size = size;
  }

  public BasicSizeDetails(String childProductId, boolean isPrimary, String size, String colourId,
      String image, String availableStock, String name,
      String outOfStock, String unitId, String rgb, String productName) {
    this.childProductId = childProductId;
    this.isPrimary = isPrimary;
    this.size = size;
    this.colourId = colourId;
    this.image = image;
    this.availableStock = availableStock;
    this.name = name;
    this.outOfStock = outOfStock;
    this.unitId = unitId;
    this.rgb = rgb;
    this.productName = productName;
  }

  protected BasicSizeDetails(Parcel in) {
    childProductId = in.readString();
    isPrimary = in.readByte() != 0;
    size = in.readString();
    colourId = in.readString();
    image = in.readString();
    availableStock = in.readString();
    name = in.readString();
    outOfStock = in.readString();
    unitId = in.readString();
    rgb = in.readString();
    productName = in.readString();
    visible = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(childProductId);
    dest.writeByte((byte) (isPrimary ? 1 : 0));
    dest.writeString(size);
    dest.writeString(colourId);
    dest.writeString(image);
    dest.writeString(availableStock);
    dest.writeString(name);
    dest.writeString(outOfStock);
    dest.writeString(unitId);
    dest.writeString(rgb);
    dest.writeString(productName);
    dest.writeByte((byte) (visible ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<BasicSizeDetails> CREATOR = new Creator<BasicSizeDetails>() {
    @Override
    public BasicSizeDetails createFromParcel(Parcel in) {
      return new BasicSizeDetails(in);
    }

    @Override
    public BasicSizeDetails[] newArray(int size) {
      return new BasicSizeDetails[size];
    }
  };

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public boolean isPrimary() {
    return isPrimary;
  }

  public void setPrimary(boolean primary) {
    isPrimary = primary;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

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

  public String getAvailableStock() {
    return availableStock;
  }

  public void setAvailableStock(String availableStock) {
    this.availableStock = availableStock;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOutOfStock() {
    return outOfStock;
  }

  public void setOutOfStock(String outOfStock) {
    this.outOfStock = outOfStock;
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

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
