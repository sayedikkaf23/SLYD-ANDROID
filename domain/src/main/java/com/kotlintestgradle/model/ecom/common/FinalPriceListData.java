package com.kotlintestgradle.model.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;

public class FinalPriceListData implements Parcelable {

  public static final Creator<FinalPriceListData> CREATOR = new Creator<FinalPriceListData>() {
    @Override
    public FinalPriceListData createFromParcel(Parcel in) {
      return new FinalPriceListData(in);
    }

    @Override
    public FinalPriceListData[] newArray(int size) {
      return new FinalPriceListData[size];
    }
  };
  private String discountPrice;
  private String finalPrice;
  private String basePrice;
  private String mou;
  private String discountPercentage;

  public FinalPriceListData(String finalPrice, String basePrice) {
    this.finalPrice = finalPrice;
    this.basePrice = basePrice;
  }

  public FinalPriceListData(String discountPrice, String finalPrice, String basePrice,
      String discountPercentage, String mou) {
    this.discountPrice = discountPrice;
    this.finalPrice = finalPrice;
    this.basePrice = basePrice;
    this.discountPercentage = discountPercentage;
    this.mou = mou;
  }

  protected FinalPriceListData(Parcel in) {
    discountPrice = in.readString();
    finalPrice = in.readString();
    basePrice = in.readString();
    mou = in.readString();
    discountPercentage = in.readString();
  }

  public String getMou() {
    return mou;
  }

  public void setMou(String mou) {
    this.mou = mou;
  }

  public String getDiscountPercentage() {
    return discountPercentage;
  }

  public void setDiscountPercentage(String discountPercentage) {
    this.discountPercentage = discountPercentage;
  }

  public String getDiscountPrice() {
    return discountPrice;
  }

  public void setDiscountPrice(String discountPrice) {
    this.discountPrice = discountPrice;
  }

  public String getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(String finalPrice) {
    this.finalPrice = finalPrice;
  }

  public String getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(String basePrice) {
    this.basePrice = basePrice;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(discountPrice);
    parcel.writeString(finalPrice);
    parcel.writeString(basePrice);
    parcel.writeString(mou);
    parcel.writeString(discountPercentage);
  }
}
