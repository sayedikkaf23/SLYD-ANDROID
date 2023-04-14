package com.kotlintestgradle.model.ecom;

import android.os.Parcel;
import android.os.Parcelable;

public class LogoImageData implements Parcelable {
  private String logoImageMobile;

  private String logoImageThumb;

  protected LogoImageData(Parcel in) {
    logoImageMobile = in.readString();
    logoImageThumb = in.readString();
    logoImageweb = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(logoImageMobile);
    dest.writeString(logoImageThumb);
    dest.writeString(logoImageweb);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<LogoImageData> CREATOR = new Creator<LogoImageData>() {
    @Override
    public LogoImageData createFromParcel(Parcel in) {
      return new LogoImageData(in);
    }

    @Override
    public LogoImageData[] newArray(int size) {
      return new LogoImageData[size];
    }
  };

  public String getLogoImageMobile() {
    return logoImageMobile;
  }

  public void setLogoImageMobile(String logoImageMobile) {
    this.logoImageMobile = logoImageMobile;
  }

  public String getLogoImageThumb() {
    return logoImageThumb;
  }

  public void setLogoImageThumb(String logoImageThumb) {
    this.logoImageThumb = logoImageThumb;
  }

  public String getLogoImageweb() {
    return logoImageweb;
  }

  public void setLogoImageweb(String logoImageweb) {
    this.logoImageweb = logoImageweb;
  }

  public LogoImageData(String logoImageMobile, String logoImageThumb, String logoImageweb) {
    this.logoImageMobile = logoImageMobile;
    this.logoImageThumb = logoImageThumb;
    this.logoImageweb = logoImageweb;
  }

  private String logoImageweb;
}
