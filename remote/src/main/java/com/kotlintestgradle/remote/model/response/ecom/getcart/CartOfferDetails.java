package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CartOfferDetails  implements Parcelable, ValidItem {
  @Expose
  @SerializedName("offerType")
  private String offerType;
  @Expose
  @SerializedName("offerId")
  private String offerId;
  @Expose
  @SerializedName("offerValue")
  private String offerValue;
  @Expose
  @SerializedName("offerTitle")
  private String offerTitle;

  public CartOfferDetails(String offerType, String offerId, String offerValue,
      String offerTitle) {
    this.offerType = offerType;
    this.offerId = offerId;
    this.offerValue = offerValue;
    this.offerTitle = offerTitle;
  }

  protected CartOfferDetails(Parcel in) {
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

  public static final Creator<CartOfferDetails> CREATOR = new Creator<CartOfferDetails>() {
    @Override
    public CartOfferDetails createFromParcel(Parcel in) {
      return new CartOfferDetails(in);
    }

    @Override
    public CartOfferDetails[] newArray(int size) {
      return new CartOfferDetails[size];
    }
  };

  public String getOfferType ()
  {
    return offerType;
  }

  public void setOfferType (String offerType)
  {
    this.offerType = offerType;
  }

  public String getOfferId ()
  {
    return offerId;
  }

  public void setOfferId (String offerId)
  {
    this.offerId = offerId;
  }

  public String getOfferValue ()
  {
    return offerValue;
  }

  public void setOfferValue (String offerValue)
  {
    this.offerValue = offerValue;
  }

  public String getOfferTitle ()
  {
    return offerTitle;
  }

  public void setOfferTitle (String offerTitle)
  {
    this.offerTitle = offerTitle;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
