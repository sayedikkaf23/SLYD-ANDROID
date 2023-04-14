package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartOfferData implements Parcelable {
  private String offerType;
  private String offerId;
  private String offerValue;
  private String offerTitle;

  public CartOfferData(String offerType, String offerId, String offerValue,
      String offerTitle) {
    this.offerType = offerType;
    this.offerId = offerId;
    this.offerValue = offerValue;
    this.offerTitle = offerTitle;
  }

  protected CartOfferData(Parcel in) {
    offerType = in.readString();
    offerId = in.readString();
    offerValue = in.readString();
    offerTitle = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(offerType);
    dest.writeString(offerId);
    dest.writeString(offerValue);
    dest.writeString(offerTitle);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartOfferData> CREATOR = new Creator<CartOfferData>() {
    @Override
    public CartOfferData createFromParcel(Parcel in) {
      return new CartOfferData(in);
    }

    @Override
    public CartOfferData[] newArray(int size) {
      return new CartOfferData[size];
    }
  };

  public String getOfferType() {
    return offerType;
  }

  public void setOfferType(String offerType) {
    this.offerType = offerType;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public String getOfferValue() {
    return offerValue;
  }

  public void setOfferValue(String offerValue) {
    this.offerValue = offerValue;
  }

  public String getOfferTitle() {
    return offerTitle;
  }

  public void setOfferTitle(String offerTitle) {
    this.offerTitle = offerTitle;
  }
}
