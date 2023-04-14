package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistorySellerLogoDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("logoImageThumb")
  private String logoImageThumb;

  @Expose
  @SerializedName("logoImageMobile")
  private String logoImageMobile;

  public OrderHistorySellerLogoDetails(String logoImageThumb, String logoImageMobile) {
    this.logoImageThumb = logoImageThumb;
    this.logoImageMobile = logoImageMobile;
  }

  protected OrderHistorySellerLogoDetails(Parcel in) {
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

  public static final Creator<OrderHistorySellerLogoDetails> CREATOR =
      new Creator<OrderHistorySellerLogoDetails>() {
        @Override
        public OrderHistorySellerLogoDetails createFromParcel(Parcel in) {
          return new OrderHistorySellerLogoDetails(in);
        }

        @Override
        public OrderHistorySellerLogoDetails[] newArray(int size) {
          return new OrderHistorySellerLogoDetails[size];
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
