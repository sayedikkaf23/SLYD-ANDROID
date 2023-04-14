package com.kotlintestgradle.model.orderdetails;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageBoxData implements Parcelable {

  private String image;

  private String volumeCapacity;

  private String voulumeCapacityUnit;

  private String widthCapacityUnitName;

  private String weight;

  private String heightCapacityUnitName;

  private String voulumeCapacityUnitName;

  private String weightCapacityUnitName;

  private String widthCapacityUnit;

  private String heightCapacity;

  private String weightCapacityUnit;

  private String lengthCapacityUnitName;

  private String name;

  private String heightCapacityUnit;

  private String lengthCapacityUnit;

  private String widthCapacity;

  private String lengthCapacity;

  private String boxId;

  public PackageBoxData(String image, String volumeCapacity, String voulumeCapacityUnit,
      String widthCapacityUnitName, String weight, String heightCapacityUnitName,
      String voulumeCapacityUnitName, String weightCapacityUnitName,
      String widthCapacityUnit, String heightCapacity, String weightCapacityUnit,
      String lengthCapacityUnitName, String name, String heightCapacityUnit,
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

  protected PackageBoxData(Parcel in) {
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
    name = in.readString();
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
    dest.writeString(name);
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

  public static final Creator<PackageBoxData> CREATOR = new Creator<PackageBoxData>() {
    @Override
    public PackageBoxData createFromParcel(Parcel in) {
      return new PackageBoxData(in);
    }

    @Override
    public PackageBoxData[] newArray(int size) {
      return new PackageBoxData[size];
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
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
}
