package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class FinalPriceListDetails implements ValidItem, Parcelable {

  @SerializedName("discountPrice")
  @Expose
  private String discountPrice;

  @SerializedName("finalPrice")
  @Expose
  private String finalPrice;

  @SerializedName("basePrice")
  @Expose
  private String basePrice;

  @SerializedName("mou")
  @Expose
  private String mou;

  @SerializedName("discountPercentage")
  @Expose
  private String discountPercentage;

  public FinalPriceListDetails(String discountPrice, String finalPrice, String basePrice,
      String discountPercentage,
      String mou) {
    this.discountPrice = discountPrice;
    this.finalPrice = finalPrice;
    this.basePrice = basePrice;
    this.discountPercentage = discountPercentage;
    this.mou = mou;
  }

  protected FinalPriceListDetails(Parcel in) {
    discountPrice = in.readString();
    finalPrice = in.readString();
    basePrice = in.readString();
    mou = in.readString();
    discountPercentage = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(discountPrice);
    dest.writeString(finalPrice);
    dest.writeString(basePrice);
    dest.writeString(mou);
    dest.writeString(discountPercentage);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<FinalPriceListDetails> CREATOR =
      new Creator<FinalPriceListDetails>() {
        @Override
        public FinalPriceListDetails createFromParcel(Parcel in) {
          return new FinalPriceListDetails(in);
        }

        @Override
        public FinalPriceListDetails[] newArray(int size) {
          return new FinalPriceListDetails[size];
        }
      };

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
  public boolean isValid() {
    return true;
  }
}
