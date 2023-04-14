package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistOfferDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("offerTitle")
  private String offerTitle;
  @Expose
  @SerializedName("offerId")
  private String offerId;
  @Expose
  @SerializedName("offerType")
  private String offerType;
  @Expose
  @SerializedName("offerValue")
  private String offerValue;

  public OrderHistOfferDetails(String offerTitle, String offerId, String offerType,
      String offerValue) {
    this.offerTitle = offerTitle;
    this.offerId = offerId;
    this.offerType = offerType;
    this.offerValue = offerValue;
  }

  protected OrderHistOfferDetails(Parcel in) {
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

  public static final Creator<OrderHistOfferDetails> CREATOR =
      new Creator<OrderHistOfferDetails>() {
        @Override
        public OrderHistOfferDetails createFromParcel(Parcel in) {
          return new OrderHistOfferDetails(in);
        }

        @Override
        public OrderHistOfferDetails[] newArray(int size) {
          return new OrderHistOfferDetails[size];
        }
      };

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

  @Override
  public boolean isValid() {
    return true;
  }
}
