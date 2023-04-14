package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubstitutesWith implements Parcelable, ValidItem {

  @Expose
  @SerializedName("centralProductId")
  private String centralProductId;

  @Expose
  @SerializedName("images")
  private Images images;

  @Expose
  @SerializedName("brandName")
  private String brandName;

  @Expose
  @SerializedName("quantity")
  private Quantity quantity;

  @Expose
  @SerializedName("color")
  private String color;

  @Expose
  @SerializedName("originalPrice")
  private double originalPrice;

  @Expose
  @SerializedName("name")
  private String name;

  @Expose
  @SerializedName("unitId")
  private String unitId;

  @Expose
  @SerializedName("_id")
  private String id;

  protected SubstitutesWith(Parcel in) {
    centralProductId = in.readString();
    images = in.readParcelable(Images.class.getClassLoader());
    brandName = in.readString();
    quantity = in.readParcelable(Quantity.class.getClassLoader());
    color = in.readString();
    originalPrice = in.readDouble();
    name = in.readString();
    unitId = in.readString();
    id = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(centralProductId);
    dest.writeParcelable(images, flags);
    dest.writeString(brandName);
    dest.writeParcelable(quantity, flags);
    dest.writeString(color);
    dest.writeDouble(originalPrice);
    dest.writeString(name);
    dest.writeString(unitId);
    dest.writeString(id);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SubstitutesWith> CREATOR = new Creator<SubstitutesWith>() {
    @Override
    public SubstitutesWith createFromParcel(Parcel in) {
      return new SubstitutesWith(in);
    }

    @Override
    public SubstitutesWith[] newArray(int size) {
      return new SubstitutesWith[size];
    }
  };

  public void setCentralProductId(String centralProductId){
    this.centralProductId = centralProductId;
  }

  public String getCentralProductId(){
    return centralProductId;
  }

  public void setImages(Images images){
    this.images = images;
  }

  public Images getImages(){
    return images;
  }

  public void setBrandName(String brandName){
    this.brandName = brandName;
  }

  public String getBrandName(){
    return brandName;
  }

  public void setQuantity(Quantity quantity){
    this.quantity = quantity;
  }

  public Quantity getQuantity(){
    return quantity;
  }

  public void setColor(String color){
    this.color = color;
  }

  public String getColor(){
    return color;
  }

  public void setOriginalPrice(double originalPrice){
    this.originalPrice = originalPrice;
  }

  public double getOriginalPrice(){
    return originalPrice;
  }

  public void setName(String name){
    this.name = name;
  }

  public String getName(){
    return name;
  }

  public void setUnitId(String unitId){
    this.unitId = unitId;
  }

  public String getUnitId(){
    return unitId;
  }

  public void setId(String id){
    this.id = id;
  }

  public String getId(){
    return id;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
