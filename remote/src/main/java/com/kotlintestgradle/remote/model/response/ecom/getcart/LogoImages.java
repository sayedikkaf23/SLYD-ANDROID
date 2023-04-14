package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class LogoImages implements Parcelable, ValidItem {
  @SerializedName("logoImageMobile")
  @Expose
  private String logoImageMobile;

  @SerializedName("logoImageThumb")
  @Expose
  private String logoImageThumb;

  @SerializedName("logoImageweb")
  @Expose
  private String logoImageweb;


  public LogoImages(String logoImageMobile,String logoImageThumb,String logoImageweb){
    this.logoImageMobile=logoImageMobile;
    this.logoImageThumb=logoImageThumb;
    this.logoImageweb=logoImageweb;

  }
  protected LogoImages(Parcel in) {
    logoImageMobile=in.readString();
    logoImageThumb=in.readString();
    logoImageweb=in.readString();
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

  public static final Creator<LogoImages> CREATOR = new Creator<LogoImages>() {
    @Override
    public LogoImages createFromParcel(Parcel in) {
      return new LogoImages(in);
    }

    @Override
    public LogoImages[] newArray(int size) {
      return new LogoImages[size];
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

  @Override
  public boolean isValid() {
    return true;
  }
}
