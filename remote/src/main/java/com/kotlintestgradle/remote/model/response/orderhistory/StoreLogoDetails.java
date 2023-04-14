package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class StoreLogoDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("logoImageThumb")
  private String logoImageThumb;
  @Expose
  @SerializedName("logoImageMobile")
  private String logoImageMobile;

  public StoreLogoDetails(String logoImageThumb, String logoImageMobile) {
    this.logoImageThumb = logoImageThumb;
    this.logoImageMobile = logoImageMobile;
  }

  protected StoreLogoDetails(Parcel in) {
    logoImageThumb = in.readString();
    logoImageMobile = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(logoImageThumb);
    dest.writeString(logoImageMobile);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<StoreLogoDetails> CREATOR = new Creator<StoreLogoDetails>() {
    @Override
    public StoreLogoDetails createFromParcel(Parcel in) {
      return new StoreLogoDetails(in);
    }

    @Override
    public StoreLogoDetails[] newArray(int size) {
      return new StoreLogoDetails[size];
    }
  };

  public String getLogoImageThumb() {
    return logoImageThumb;
  }

  public void setLogoImageThumb(String logoImageThumb) {
    this.logoImageThumb = logoImageThumb;
  }

  public String getLogoImageMobile() {
    return logoImageMobile;
  }

  public void setLogoImageMobile(String logoImageMobile) {
    this.logoImageMobile = logoImageMobile;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
