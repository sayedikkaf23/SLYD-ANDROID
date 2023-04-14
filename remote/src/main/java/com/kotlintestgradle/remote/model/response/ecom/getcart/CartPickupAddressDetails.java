package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CartPickupAddressDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("country")
  private String country;

  @Expose
  @SerializedName("address")
  private String address;

  @Expose
  @SerializedName("locality")
  private String locality;

  @Expose
  @SerializedName("cityId")
  private String cityId;

  /*@Expose
  @SerializedName("long")
  private double longitude;*/

  @Expose
  @SerializedName("sublocality_level_2")
  private String sublocality_level_2;

  @Expose
  @SerializedName("sublocality_level_1")
  private String sublocality_level_1;

  @Expose
  @SerializedName("route")
  private String route;

  @Expose
  @SerializedName("administrative_area_level_2")
  private String administrative_area_level_2;

  @Expose
  @SerializedName("cityName")
  private String cityName;

  @Expose
  @SerializedName("administrative_area_level_1")
  private String administrative_area_level_1;

  @Expose
  @SerializedName("postal_code")
  private String postal_code;

  @Expose
  @SerializedName("addressArea")
  private String addressArea;

 /* @Expose
  @SerializedName("lat")
  private double lat;*/

  public CartPickupAddressDetails(String country, String address, String locality,
      String cityId, String sublocality_level_2,
      String sublocality_level_1, String route, String administrative_area_level_2,
      String cityName, String administrative_area_level_1, String postal_code,String addressArea) {
    this.country = country;
    this.address = address;
    this.locality = locality;
    this.cityId = cityId;
    this.addressArea = addressArea;
/*
    this.longitude = longitude;
*/
    this.sublocality_level_2 = sublocality_level_2;
    this.sublocality_level_1 = sublocality_level_1;
    this.route = route;
    this.administrative_area_level_2 = administrative_area_level_2;
    this.cityName = cityName;
    this.administrative_area_level_1 = administrative_area_level_1;
    this.postal_code = postal_code;
    //this.lat = lat;
  }

  protected CartPickupAddressDetails(Parcel in) {
    country = in.readString();
    address = in.readString();
    locality = in.readString();
    cityId = in.readString();
    sublocality_level_2 = in.readString();
    sublocality_level_1 = in.readString();
    route = in.readString();
    administrative_area_level_2 = in.readString();
    cityName = in.readString();
    administrative_area_level_1 = in.readString();
    postal_code = in.readString();
    addressArea = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(country);
    dest.writeString(address);
    dest.writeString(locality);
    dest.writeString(cityId);
    dest.writeString(sublocality_level_2);
    dest.writeString(sublocality_level_1);
    dest.writeString(route);
    dest.writeString(administrative_area_level_2);
    dest.writeString(cityName);
    dest.writeString(administrative_area_level_1);
    dest.writeString(postal_code);
    dest.writeString(addressArea);
  }

  public String getAddressArea() {
    return addressArea;
  }

  public void setAddressArea(String addressArea) {
    this.addressArea = addressArea;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartPickupAddressDetails> CREATOR =
      new Creator<CartPickupAddressDetails>() {
        @Override
        public CartPickupAddressDetails createFromParcel(Parcel in) {
          return new CartPickupAddressDetails(in);
        }

        @Override
        public CartPickupAddressDetails[] newArray(int size) {
          return new CartPickupAddressDetails[size];
        }
      };

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  /*public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
*/
  public String getSublocality_level_2() {
    return sublocality_level_2;
  }

  public void setSublocality_level_2(String sublocality_level_2) {
    this.sublocality_level_2 = sublocality_level_2;
  }

  public String getSublocality_level_1() {
    return sublocality_level_1;
  }

  public void setSublocality_level_1(String sublocality_level_1) {
    this.sublocality_level_1 = sublocality_level_1;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public String getAdministrative_area_level_2() {
    return administrative_area_level_2;
  }

  public void setAdministrative_area_level_2(String administrative_area_level_2) {
    this.administrative_area_level_2 = administrative_area_level_2;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getAdministrative_area_level_1() {
    return administrative_area_level_1;
  }

  public void setAdministrative_area_level_1(String administrative_area_level_1) {
    this.administrative_area_level_1 = administrative_area_level_1;
  }

  public String getPostal_code() {
    return postal_code;
  }

  public void setPostal_code(String postal_code) {
    this.postal_code = postal_code;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  /*public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }*/
}
