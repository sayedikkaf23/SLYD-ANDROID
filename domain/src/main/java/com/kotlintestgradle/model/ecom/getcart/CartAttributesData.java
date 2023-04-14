package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartAttributesData implements Parcelable {
  private String measurementUnitName;
  private int customizable;
  private int rateable;
  private int linkedtounit;
  private String attrname;
  private int searchable;
  private int attributeDataType;
  private String measurementUnit;
  private int dropdownSelectType;
  private String attributeId;
  private int attriubteType;
  private boolean isAddOn;
  private String value;

  public CartAttributesData(String measurementUnitName, int customizable, int rateable, int linkedtounit,
                            String attrname, int searchable, int attributeDataType, String measurementUnit,
                            int dropdownSelectType, String attributeId, int attriubteType, boolean isAddOn, String value) {
    this.measurementUnitName = measurementUnitName;
    this.customizable = customizable;
    this.rateable = rateable;
    this.linkedtounit = linkedtounit;
    this.attrname = attrname;
    this.searchable = searchable;
    this.attributeDataType = attributeDataType;
    this.measurementUnit = measurementUnit;
    this.dropdownSelectType = dropdownSelectType;
    this.attributeId = attributeId;
    this.attriubteType = attriubteType;
    this.isAddOn = isAddOn;
    this.value = value;
  }

  protected CartAttributesData(Parcel in) {
    measurementUnitName = in.readString();
    customizable = in.readInt();
    rateable = in.readInt();
    linkedtounit = in.readInt();
    attrname = in.readString();
    searchable = in.readInt();
    attributeDataType = in.readInt();
    measurementUnit = in.readString();
    dropdownSelectType = in.readInt();
    attributeId = in.readString();
    attriubteType = in.readInt();
    isAddOn = in.readByte() != 0;
    value = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(measurementUnitName);
    dest.writeInt(customizable);
    dest.writeInt(rateable);
    dest.writeInt(linkedtounit);
    dest.writeString(attrname);
    dest.writeInt(searchable);
    dest.writeInt(attributeDataType);
    dest.writeString(measurementUnit);
    dest.writeInt(dropdownSelectType);
    dest.writeString(attributeId);
    dest.writeInt(attriubteType);
    dest.writeByte((byte) (isAddOn ? 1 : 0));
    dest.writeString(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartAttributesData> CREATOR = new Creator<CartAttributesData>() {
    @Override
    public CartAttributesData createFromParcel(Parcel in) {
      return new CartAttributesData(in);
    }

    @Override
    public CartAttributesData[] newArray(int size) {
      return new CartAttributesData[size];
    }
  };

  public void setMeasurementUnitName(String measurementUnitName){
    this.measurementUnitName = measurementUnitName;
  }

  public String getMeasurementUnitName(){
    return measurementUnitName;
  }

  public void setCustomizable(int customizable){
    this.customizable = customizable;
  }

  public int getCustomizable(){
    return customizable;
  }

  public void setRateable(int rateable){
    this.rateable = rateable;
  }

  public int getRateable(){
    return rateable;
  }

  public void setLinkedtounit(int linkedtounit){
    this.linkedtounit = linkedtounit;
  }

  public int getLinkedtounit(){
    return linkedtounit;
  }

  public void setAttrname(String attrname){
    this.attrname = attrname;
  }

  public String getAttrname(){
    return attrname;
  }

  public void setSearchable(int searchable){
    this.searchable = searchable;
  }

  public int getSearchable(){
    return searchable;
  }

  public void setAttributeDataType(int attributeDataType){
    this.attributeDataType = attributeDataType;
  }

  public int getAttributeDataType(){
    return attributeDataType;
  }

  public void setMeasurementUnit(String measurementUnit){
    this.measurementUnit = measurementUnit;
  }

  public String getMeasurementUnit(){
    return measurementUnit;
  }

  public void setDropdownSelectType(int dropdownSelectType){
    this.dropdownSelectType = dropdownSelectType;
  }

  public int getDropdownSelectType(){
    return dropdownSelectType;
  }

  public void setAttributeId(String attributeId){
    this.attributeId = attributeId;
  }

  public String getAttributeId(){
    return attributeId;
  }

  public void setAttriubteType(int attriubteType){
    this.attriubteType = attriubteType;
  }

  public int getAttriubteType(){
    return attriubteType;
  }

  public void setIsAddOn(boolean isAddOn){
    this.isAddOn = isAddOn;
  }

  public boolean isIsAddOn(){
    return isAddOn;
  }

  public void setValue(String value){
    this.value = value;
  }

  public String getValue(){
    return value;
  }
}
