package com.kotlintestgradle.remote.model.response.ecom.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class IpAddressToLocationDetails implements Parcelable, ValidItem {
  @Expose
  @SerializedName("ip")
  private String ip;
  @Expose
  @SerializedName("city")
  private String city;
  @Expose
  @SerializedName("region")
  private String region;
  @Expose
  @SerializedName("country")
  private String country;
  @Expose
  @SerializedName("postal")
  private String postal;
  @Expose
  @SerializedName("timezone")
  private String timezone;
  @Expose
  @SerializedName("location")
  private LocationData mLocationData;

  public IpAddressToLocationDetails(String ip, String city, String region, String country,
      String postal, String timezone, LocationData locationData) {
    this.ip = ip;
    this.city = city;
    this.region = region;
    this.country = country;
    this.postal = postal;
    this.timezone = timezone;
    this.mLocationData = locationData;
  }

  protected IpAddressToLocationDetails(Parcel in) {
    ip = in.readString();
    city = in.readString();
    region = in.readString();
    country = in.readString();
    postal = in.readString();
    timezone = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(ip);
    dest.writeString(city);
    dest.writeString(region);
    dest.writeString(country);
    dest.writeString(postal);
    dest.writeString(timezone);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<IpAddressToLocationDetails> CREATOR =
      new Creator<IpAddressToLocationDetails>() {
        @Override
        public IpAddressToLocationDetails createFromParcel(Parcel in) {
          return new IpAddressToLocationDetails(in);
        }

        @Override
        public IpAddressToLocationDetails[] newArray(int size) {
          return new IpAddressToLocationDetails[size];
        }
      };

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostal() {
    return postal;
  }

  public void setPostal(String postal) {
    this.postal = postal;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public LocationData getLocationData() {
    return mLocationData;
  }

  public void setLocationData(LocationData locationData) {
    this.mLocationData = locationData;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
