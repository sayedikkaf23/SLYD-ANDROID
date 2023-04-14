package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistOfferData implements Parcelable {

  private String offerTitle;
  private String offerId;
  private String offerType;
  private String offerValue;

  public OrderHistOfferData(String offerTitle, String offerId, String offerType, String offerValue) {
    this.offerTitle = offerTitle;
    this.offerId = offerId;
    this.offerType = offerType;
    this.offerValue = offerValue;
  }

  public String getOfferTitle() {
    return offerTitle;
  }

  public void setOfferTitle(String offerTitle) {
    this.offerTitle = offerTitle;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public String getOfferType() {
    return offerType;
  }

  public void setOfferType(String offerType) {
    this.offerType = offerType;
  }

  public String getOfferValue() {
    return offerValue;
  }

  public void setOfferValue(String offerValue) {
    this.offerValue = offerValue;
  }

  protected OrderHistOfferData(Parcel in) {
    offerTitle = in.readString();
    offerId = in.readString();
    offerType = in.readString();
    offerValue = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(offerTitle);
    dest.writeString(offerId);
    dest.writeString(offerType);
    dest.writeString(offerValue);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistOfferData> CREATOR = new Creator<OrderHistOfferData>() {
    @Override
    public OrderHistOfferData createFromParcel(Parcel in) {
      return new OrderHistOfferData(in);
    }

    @Override
    public OrderHistOfferData[] newArray(int size) {
      return new OrderHistOfferData[size];
    }
  };
}
