package com.kotlintestgradle.remote.model.response.orderdetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.pdp.OfferName;

public class PackageBoxDetails implements Parcelable , ValidItem {

  @Expose
  @SerializedName("image")
  private String image;
  @Expose
  @SerializedName("volumeCapacity")
  private String volumeCapacity;
  @Expose
  @SerializedName("voulumeCapacityUnit")
  private String voulumeCapacityUnit;
  @Expose
  @SerializedName("widthCapacityUnitName")
  private String widthCapacityUnitName;
  @Expose
  @SerializedName("weight")
  private String weight;
  @Expose
  @SerializedName("heightCapacityUnitName")
  private String heightCapacityUnitName;
  @Expose
  @SerializedName("voulumeCapacityUnitName")
  private String voulumeCapacityUnitName;
  @Expose
  @SerializedName("weightCapacityUnitName")
  private String weightCapacityUnitName;
  @Expose
  @SerializedName("widthCapacityUnit")
  private String widthCapacityUnit;
  @Expose
  @SerializedName("heightCapacity")
  private String heightCapacity;
  @Expose
  @SerializedName("weightCapacityUnit")
  private String weightCapacityUnit;
  @Expose
  @SerializedName("lengthCapacityUnitName")
  private String lengthCapacityUnitName;
  @Expose
  @SerializedName("name")
  private OfferName name;
  @Expose
  @SerializedName("heightCapacityUnit")
  private String heightCapacityUnit;
  @Expose
  @SerializedName("lengthCapacityUnit")
  private String lengthCapacityUnit;
  @Expose
  @SerializedName("widthCapacity")
  private String widthCapacity;
  @Expose
  @SerializedName("lengthCapacity")
  private String lengthCapacity;
  @Expose
  @SerializedName("boxId")
  private String boxId;

  public PackageBoxDetails(String image, String volumeCapacity, String voulumeCapacityUnit,
                           String widthCapacityUnitName, String weight, String heightCapacityUnitName,
                           String voulumeCapacityUnitName, String weightCapacityUnitName,
                           String widthCapacityUnit, String heightCapacity, String weightCapacityUnit,
                           String lengthCapacityUnitName, OfferName name, String heightCapacityUnit,
                           String lengthCapacityUnit, String widthCapacity, String lengthCapacity, String boxId) {
    this.image = image;
    this.volumeCapacity = volumeCapacity;
    this.voulumeCapacityUnit = voulumeCapacityUnit;
    this.widthCapacityUnitName = widthCapacityUnitName;
    this.weight = weight;
    this.heightCapacityUnitName = heightCapacityUnitName;
    this.voulumeCapacityUnitName = voulumeCapacityUnitName;
    this.weightCapacityUnitName = weightCapacityUnitName;
    this.widthCapacityUnit = widthCapacityUnit;
    this.heightCapacity = heightCapacity;
    this.weightCapacityUnit = weightCapacityUnit;
    this.lengthCapacityUnitName = lengthCapacityUnitName;
    this.name = name;
    this.heightCapacityUnit = heightCapacityUnit;
    this.lengthCapacityUnit = lengthCapacityUnit;
    this.widthCapacity = widthCapacity;
    this.lengthCapacity = lengthCapacity;
    this.boxId = boxId;
  }

  protected PackageBoxDetails(Parcel in) {
    image = in.readString();
    volumeCapacity = in.readString();
    voulumeCapacityUnit = in.readString();
    widthCapacityUnitName = in.readString();
    weight = in.readString();
    heightCapacityUnitName = in.readString();
    voulumeCapacityUnitName = in.readString();
    weightCapacityUnitName = in.readString();
    widthCapacityUnit = in.readString();
    heightCapacity = in.readString();
    weightCapacityUnit = in.readString();
    lengthCapacityUnitName = in.readString();
    heightCapacityUnit = in.readString();
    lengthCapacityUnit = in.readString();
    widthCapacity = in.readString();
    lengthCapacity = in.readString();
    boxId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(image);
    dest.writeString(volumeCapacity);
    dest.writeString(voulumeCapacityUnit);
    dest.writeString(widthCapacityUnitName);
    dest.writeString(weight);
    dest.writeString(heightCapacityUnitName);
    dest.writeString(voulumeCapacityUnitName);
    dest.writeString(weightCapacityUnitName);
    dest.writeString(widthCapacityUnit);
    dest.writeString(heightCapacity);
    dest.writeString(weightCapacityUnit);
    dest.writeString(lengthCapacityUnitName);
    dest.writeString(heightCapacityUnit);
    dest.writeString(lengthCapacityUnit);
    dest.writeString(widthCapacity);
    dest.writeString(lengthCapacity);
    dest.writeString(boxId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PackageBoxDetails> CREATOR = new Creator<PackageBoxDetails>() {
    @Override
    public PackageBoxDetails createFromParcel(Parcel in) {
      return new PackageBoxDetails(in);
    }

    @Override
    public PackageBoxDetails[] newArray(int size) {
      return new PackageBoxDetails[size];
    }
  };

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getVolumeCapacity() {
    return volumeCapacity;
  }

  public void setVolumeCapacity(String volumeCapacity) {
    this.volumeCapacity = volumeCapacity;
  }

  public String getVoulumeCapacityUnit() {
    return voulumeCapacityUnit;
  }

  public void setVoulumeCapacityUnit(String voulumeCapacityUnit) {
    this.voulumeCapacityUnit = voulumeCapacityUnit;
  }

  public String getWidthCapacityUnitName() {
    return widthCapacityUnitName;
  }

  public void setWidthCapacityUnitName(String widthCapacityUnitName) {
    this.widthCapacityUnitName = widthCapacityUnitName;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    this.weight = weight;
  }

  public String getHeightCapacityUnitName() {
    return heightCapacityUnitName;
  }

  public void setHeightCapacityUnitName(String heightCapacityUnitName) {
    this.heightCapacityUnitName = heightCapacityUnitName;
  }

  public String getVoulumeCapacityUnitName() {
    return voulumeCapacityUnitName;
  }

  public void setVoulumeCapacityUnitName(String voulumeCapacityUnitName) {
    this.voulumeCapacityUnitName = voulumeCapacityUnitName;
  }

  public String getWeightCapacityUnitName() {
    return weightCapacityUnitName;
  }

  public void setWeightCapacityUnitName(String weightCapacityUnitName) {
    this.weightCapacityUnitName = weightCapacityUnitName;
  }

  public String getWidthCapacityUnit() {
    return widthCapacityUnit;
  }

  public void setWidthCapacityUnit(String widthCapacityUnit) {
    this.widthCapacityUnit = widthCapacityUnit;
  }

  public String getHeightCapacity() {
    return heightCapacity;
  }

  public void setHeightCapacity(String heightCapacity) {
    this.heightCapacity = heightCapacity;
  }

  public String getWeightCapacityUnit() {
    return weightCapacityUnit;
  }

  public void setWeightCapacityUnit(String weightCapacityUnit) {
    this.weightCapacityUnit = weightCapacityUnit;
  }

  public String getLengthCapacityUnitName() {
    return lengthCapacityUnitName;
  }

  public void setLengthCapacityUnitName(String lengthCapacityUnitName) {
    this.lengthCapacityUnitName = lengthCapacityUnitName;
  }

  public OfferName getName() {
    return name;
  }

  public void setName(OfferName name) {
    this.name = name;
  }

  public String getHeightCapacityUnit() {
    return heightCapacityUnit;
  }

  public void setHeightCapacityUnit(String heightCapacityUnit) {
    this.heightCapacityUnit = heightCapacityUnit;
  }

  public String getLengthCapacityUnit() {
    return lengthCapacityUnit;
  }

  public void setLengthCapacityUnit(String lengthCapacityUnit) {
    this.lengthCapacityUnit = lengthCapacityUnit;
  }

  public String getWidthCapacity() {
    return widthCapacity;
  }

  public void setWidthCapacity(String widthCapacity) {
    this.widthCapacity = widthCapacity;
  }

  public String getLengthCapacity() {
    return lengthCapacity;
  }

  public void setLengthCapacity(String lengthCapacity) {
    this.lengthCapacity = lengthCapacity;
  }

  public String getBoxId() {
    return boxId;
  }

  public void setBoxId(String boxId) {
    this.boxId = boxId;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
