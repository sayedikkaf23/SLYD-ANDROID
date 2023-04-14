package com.kotlintestgradle.model.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;

public class OffersListData implements Parcelable {

  private ImageData images;

  private String offername;

  private String offerId;

  private String currencySymbol;

  private String currency;

  private String offerdescription;

  private String discountValue;

  public OffersListData(ImageData images, String offername, String offerId, String discountValue) {
    this.images = images;
    this.offername = offername;
    this.offerId = offerId;
    this.discountValue = discountValue;
  }

  public OffersListData(ImageData images, String offername, String offerId, String currencySymbol,
      String currency, String offerdescription) {
    this.images = images;
    this.offername = offername;
    this.offerId = offerId;
    this.currencySymbol = currencySymbol;
    this.currency = currency;
    this.offerdescription = offerdescription;
  }

  protected OffersListData(Parcel in) {
    images = in.readParcelable(ImageData.class.getClassLoader());
    offername = in.readString();
    offerId = in.readString();
    currencySymbol = in.readString();
    currency = in.readString();
    offerdescription = in.readString();
    discountValue = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(images, flags);
    dest.writeString(offername);
    dest.writeString(offerId);
    dest.writeString(currencySymbol);
    dest.writeString(currency);
    dest.writeString(offerdescription);
    dest.writeString(discountValue);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OffersListData> CREATOR = new Creator<OffersListData>() {
    @Override
    public OffersListData createFromParcel(Parcel in) {
      return new OffersListData(in);
    }

    @Override
    public OffersListData[] newArray(int size) {
      return new OffersListData[size];
    }
  };

  public ImageData getImages() {
    return images;
  }

  public void setImages(ImageData images) {
    this.images = images;
  }

  public String getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  public ImageData getImagesDetails() {
    return images;
  }

  public void setImagesDetails(ImageData imagesDetails) {
    this.images = imagesDetails;
  }

  public String getOffername() {
    return offername;
  }

  public void setOffername(String offername) {
    this.offername = offername;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getOfferdescription() {
    return offerdescription;
  }

  public void setOfferdescription(String offerdescription) {
    this.offerdescription = offerdescription;
  }
}
