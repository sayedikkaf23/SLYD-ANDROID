package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OffersDataDetails implements ValidItem, Parcelable {

  @SerializedName("images")
  @Expose
  private ImagesDetails images;

  @SerializedName("offername")
  @Expose
  private String offername;

  @SerializedName("offerId")
  @Expose
  private String offerId;

  @SerializedName("discountValue")
  @Expose
  private String discountValue;

  @SerializedName("currencySymbol")
  @Expose
  private String currencySymbol;

  @SerializedName("currency")
  @Expose
  private String currency;

  @SerializedName("offerdescription")
  @Expose
  private String offerdescription;

  public OffersDataDetails(ImagesDetails images, String offername, String offerId,
      String discountValue) {
    this.images = images;
    this.offername = offername;
    this.offerId = offerId;
    this.discountValue = discountValue;
  }

  public OffersDataDetails(ImagesDetails images, String offername, String offerId,
      String currencySymbol, String currency, String offerdescription) {
    this.images = images;
    this.offername = offername;
    this.offerId = offerId;
    this.currencySymbol = currencySymbol;
    this.currency = currency;
    this.offerdescription = offerdescription;
  }

  public OffersDataDetails() {
  }

  protected OffersDataDetails(Parcel in) {
    images = in.readParcelable(ImagesDetails.class.getClassLoader());
    offername = in.readString();
    offerId = in.readString();
    discountValue = in.readString();
    currencySymbol = in.readString();
    currency = in.readString();
    offerdescription = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(images, flags);
    dest.writeString(offername);
    dest.writeString(offerId);
    dest.writeString(discountValue);
    dest.writeString(currencySymbol);
    dest.writeString(currency);
    dest.writeString(offerdescription);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OffersDataDetails> CREATOR = new Creator<OffersDataDetails>() {
    @Override
    public OffersDataDetails createFromParcel(Parcel in) {
      return new OffersDataDetails(in);
    }

    @Override
    public OffersDataDetails[] newArray(int size) {
      return new OffersDataDetails[size];
    }
  };

  public ImagesDetails getImagesDetails() {
    return images;
  }

  public void setImagesDetails(ImagesDetails imagesDetails) {
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

  public ImagesDetails getImages() {
    return images;
  }

  public void setImages(ImagesDetails images) {
    this.images = images;
  }

  public String getDiscountValue() {
    return discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
