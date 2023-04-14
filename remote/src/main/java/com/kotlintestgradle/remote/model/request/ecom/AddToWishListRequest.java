package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToWishListRequest implements Parcelable {

  @SerializedName("userid")
  @Expose
  private String userid;

  @SerializedName("productid")
  @Expose
  private String productid;

  @SerializedName("ipaddress")
  @Expose
  private String ipaddress;

  @SerializedName("latitude")
  @Expose
  private double latitude;

  @SerializedName("longitude")
  @Expose
  private double longitude;

  @SerializedName("cityid")
  @Expose
  private String cityid;

  @SerializedName("countryid")
  @Expose
  private String countryid;

  public AddToWishListRequest(String userid, String productid, String ipaddress, double latitude,
      double longitude) {
    this.userid = userid;
    this.productid = productid;
    this.ipaddress = ipaddress;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public AddToWishListRequest(String userid, String productid, String ipaddress, double latitude,
      double longitude, String cityid, String countryid) {
    this.userid = userid;
    this.productid = productid;
    this.ipaddress = ipaddress;
    this.latitude = latitude;
    this.longitude = longitude;
    this.cityid = cityid;
    this.countryid = countryid;
  }

  protected AddToWishListRequest(Parcel in) {
    userid = in.readString();
    productid = in.readString();
    ipaddress = in.readString();
    latitude = in.readDouble();
    longitude = in.readDouble();
    cityid = in.readString();
    countryid = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(userid);
    dest.writeString(productid);
    dest.writeString(ipaddress);
    dest.writeDouble(latitude);
    dest.writeDouble(longitude);
    dest.writeString(cityid);
    dest.writeString(countryid);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<AddToWishListRequest> CREATOR = new Creator<AddToWishListRequest>() {
    @Override
    public AddToWishListRequest createFromParcel(Parcel in) {
      return new AddToWishListRequest(in);
    }

    @Override
    public AddToWishListRequest[] newArray(int size) {
      return new AddToWishListRequest[size];
    }
  };

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getProductid() {
    return productid;
  }

  public void setProductid(String productid) {
    this.productid = productid;
  }

  public String getIpaddress() {
    return ipaddress;
  }

  public void setIpaddress(String ipaddress) {
    this.ipaddress = ipaddress;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getCityid() {
    return cityid;
  }

  public void setCityid(String cityid) {
    this.cityid = cityid;
  }

  public String getCountryid() {
    return countryid;
  }

  public void setCountryid(String countryid) {
    this.countryid = countryid;
  }
}
