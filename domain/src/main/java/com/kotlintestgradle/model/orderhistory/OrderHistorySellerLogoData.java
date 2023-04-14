package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistorySellerLogoData implements Parcelable {

  private String logoImageThumb;

  private String logoImageMobile;

  public OrderHistorySellerLogoData(String logoImageThumb, String logoImageMobile) {
    this.logoImageThumb = logoImageThumb;
    this.logoImageMobile = logoImageMobile;
  }

  protected OrderHistorySellerLogoData(Parcel in) {
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

  public static final Creator<OrderHistorySellerLogoData> CREATOR =
      new Creator<OrderHistorySellerLogoData>() {
        @Override
        public OrderHistorySellerLogoData createFromParcel(Parcel in) {
          return new OrderHistorySellerLogoData(in);
        }

        @Override
        public OrderHistorySellerLogoData[] newArray(int size) {
          return new OrderHistorySellerLogoData[size];
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
}
