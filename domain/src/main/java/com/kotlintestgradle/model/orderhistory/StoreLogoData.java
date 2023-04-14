package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class StoreLogoData implements Parcelable {

  private String logoImageThumb;

  private String logoImageMobile;

  public StoreLogoData(String logoImageThumb, String logoImageMobile) {
    this.logoImageThumb = logoImageThumb;
    this.logoImageMobile = logoImageMobile;
  }

  protected StoreLogoData(Parcel in) {
    logoImageThumb = in.readString();
    logoImageMobile = in.readString();
  }

  public static final Creator<StoreLogoData> CREATOR = new Creator<StoreLogoData>() {
    @Override
    public StoreLogoData createFromParcel(Parcel in) {
      return new StoreLogoData(in);
    }

    @Override
    public StoreLogoData[] newArray(int size) {
      return new StoreLogoData[size];
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
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(logoImageThumb);
    dest.writeString(logoImageMobile);
  }
}
