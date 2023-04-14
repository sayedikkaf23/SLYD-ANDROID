package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistAttributeDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("rateable")
  private int rateable;
  @Expose
  @SerializedName("searchable")
  private int searchable;
  @Expose
  @SerializedName("attriubteType")
  private int attriubteType;
  @Expose
  @SerializedName("linkedtounit")
  private int linkedtounit;
  @Expose
  @SerializedName("attributeDataType")
  private int attributeDataType;
  @Expose
  @SerializedName("dropdownSelectType")
  private int dropdownSelectType;
  @Expose
  @SerializedName("attributeId")
  private String attributeId;
  @Expose
  @SerializedName("value")
  private String value;
  @Expose
  @SerializedName("measurementUnit")
  private String measurementUnit;
  @Expose
  @SerializedName("attrname")
  private String attrname;

  public OrderHistAttributeDetails(int rateable, int searchable, int attriubteType,
      int linkedtounit,
      int attributeDataType, int dropdownSelectType, String attributeId, String value,
      String measurementUnit, String attrname) {
    this.rateable = rateable;
    this.searchable = searchable;
    this.attriubteType = attriubteType;
    this.linkedtounit = linkedtounit;
    this.attributeDataType = attributeDataType;
    this.dropdownSelectType = dropdownSelectType;
    this.attributeId = attributeId;
    this.value = value;
    this.measurementUnit = measurementUnit;
    this.attrname = attrname;
  }

  protected OrderHistAttributeDetails(Parcel in) {
    rateable = in.readInt();
    searchable = in.readInt();
    attriubteType = in.readInt();
    linkedtounit = in.readInt();
    attributeDataType = in.readInt();
    dropdownSelectType = in.readInt();
    attributeId = in.readString();
    value = in.readString();
    measurementUnit = in.readString();
    attrname = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(rateable);
    dest.writeInt(searchable);
    dest.writeInt(attriubteType);
    dest.writeInt(linkedtounit);
    dest.writeInt(attributeDataType);
    dest.writeInt(dropdownSelectType);
    dest.writeString(attributeId);
    dest.writeString(value);
    dest.writeString(measurementUnit);
    dest.writeString(attrname);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistAttributeDetails> CREATOR =
      new Creator<OrderHistAttributeDetails>() {
        @Override
        public OrderHistAttributeDetails createFromParcel(Parcel in) {
          return new OrderHistAttributeDetails(in);
        }

        @Override
        public OrderHistAttributeDetails[] newArray(int size) {
          return new OrderHistAttributeDetails[size];
        }
      };

  public int getRateable() {
    return rateable;
  }

  public void setRateable(int rateable) {
    this.rateable = rateable;
  }

  public int getSearchable() {
    return searchable;
  }

  public void setSearchable(int searchable) {
    this.searchable = searchable;
  }

  public int getAttriubteType() {
    return attriubteType;
  }

  public void setAttriubteType(int attriubteType) {
    this.attriubteType = attriubteType;
  }

  public int getLinkedtounit() {
    return linkedtounit;
  }

  public void setLinkedtounit(int linkedtounit) {
    this.linkedtounit = linkedtounit;
  }

  public int getAttributeDataType() {
    return attributeDataType;
  }

  public void setAttributeDataType(int attributeDataType) {
    this.attributeDataType = attributeDataType;
  }

  public int getDropdownSelectType() {
    return dropdownSelectType;
  }

  public void setDropdownSelectType(int dropdownSelectType) {
    this.dropdownSelectType = dropdownSelectType;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getMeasurementUnit() {
    return measurementUnit;
  }

  public void setMeasurementUnit(String measurementUnit) {
    this.measurementUnit = measurementUnit;
  }

  public String getAttrname() {
    return attrname;
  }

  public void setAttrname(String attrname) {
    this.attrname = attrname;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
