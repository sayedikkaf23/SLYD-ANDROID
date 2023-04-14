package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OfferName implements ValidItem, Parcelable {

  @SerializedName("en")
  @Expose
  private String en;

  public OfferName(String en) {
    this.en = en;
  }

  protected OfferName(Parcel in) {
    en = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(en);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OfferName> CREATOR = new Creator<OfferName>() {
    @Override
    public OfferName createFromParcel(Parcel in) {
      return new OfferName(in);
    }

    @Override
    public OfferName[] newArray(int size) {
      return new OfferName[size];
    }
  };

  public String getEn() {
    return en;
  }

  public void setEn(String en) {
    this.en = en;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
