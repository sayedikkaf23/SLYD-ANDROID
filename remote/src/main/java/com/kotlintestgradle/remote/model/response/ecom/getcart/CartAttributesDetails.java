package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CartAttributesDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("measurementUnitName")
  private String measurementUnitName;

  @Expose
  @SerializedName("customizable")
  private int customizable;

  @Expose
  @SerializedName("rateable")
  private int rateable;

  @Expose
  @SerializedName("linkedtounit")
  private int linkedtounit;

  @Expose
  @SerializedName("attrname")
  private String attrname;

  @Expose
  @SerializedName("searchable")
  private int searchable;

  @Expose
  @SerializedName("attributeDataType")
  private int attributeDataType;

  @Expose
  @SerializedName("measurementUnit")
  private String measurementUnit;

  @Expose
  @SerializedName("dropdownSelectType")
  private int dropdownSelectType;

  @Expose
  @SerializedName("attributeId")
  private String attributeId;

  @Expose
  @SerializedName("attriubteType")
  private int attriubteType;

  @Expose
  @SerializedName("isAddOn")
  private boolean isAddOn;

  @Expose
  @SerializedName("value")
  private String value;

  protected CartAttributesDetails(Parcel in) {
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

  public static final Creator<CartAttributesDetails> CREATOR = new Creator<CartAttributesDetails>() {
    @Override
    public CartAttributesDetails createFromParcel(Parcel in) {
      return new CartAttributesDetails(in);
    }

    @Override
    public CartAttributesDetails[] newArray(int size) {
      return new CartAttributesDetails[size];
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

  @Override
  public boolean isValid() {
    return true;
  }
}
