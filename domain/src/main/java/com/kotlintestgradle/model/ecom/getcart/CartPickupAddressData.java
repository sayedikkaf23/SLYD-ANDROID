package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartPickupAddressData implements Parcelable {
  public static final Creator<CartPickupAddressData> CREATOR =
      new Creator<CartPickupAddressData>() {
        @Override
        public CartPickupAddressData createFromParcel(Parcel in) {
          return new CartPickupAddressData(in);
        }

        @Override
        public CartPickupAddressData[] newArray(int size) {
          return new CartPickupAddressData[size];
        }
      };
  private String country;
  private String address;
  private String locality;
  private String cityId;
  /*
    private double longitude;
  */
  private String subLocalityLevel2;
  private String subLocalityLevel1;
  private String route;
  private String administrativeAreaLevel2;
  private String cityName;
  private String addressArea;
  private String administrativeAreaLevel1;
  /*
    private double lat;
  */
  private String mPostalCode;

  public CartPickupAddressData(String country, String address, String locality,
      String cityId, String subLocalityLevel2,
      String subLocalityLevel1, String route, String administrativeAreaLevel2,
      String cityName, String administrativeAreaLevel1, String postalCode,String addressArea) {
    this.country = country;
    this.address = address;
    this.locality = locality;
    this.cityId = cityId;
    this.addressArea = addressArea;
    // this.longitude = longitude;
    this.subLocalityLevel2 = subLocalityLevel2;
    this.subLocalityLevel1 = subLocalityLevel1;
    this.route = route;
    this.administrativeAreaLevel2 = administrativeAreaLevel2;
    this.cityName = cityName;
    this.administrativeAreaLevel1 = administrativeAreaLevel1;
    this.mPostalCode = postalCode;
    // this.lat = lat;
  }

  protected CartPickupAddressData(Parcel in) {
    country = in.readString();
    address = in.readString();
    locality = in.readString();
    cityId = in.readString();
    subLocalityLevel2 = in.readString();
    subLocalityLevel1 = in.readString();
    route = in.readString();
    administrativeAreaLevel2 = in.readString();
    cityName = in.readString();
    administrativeAreaLevel1 = in.readString();
    mPostalCode = in.readString();
  }

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

  public String getAddressArea() {
    return addressArea;
  }

  public void setAddressArea(String addressArea) {
    this.addressArea = addressArea;
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

  public String getSubLocalityLevel2() {
    return subLocalityLevel2;
  }

  public void setSubLocalityLevel2(String subLocalityLevel2) {
    this.subLocalityLevel2 = subLocalityLevel2;
  }

  public String getSubLocalityLevel1() {
    return subLocalityLevel1;
  }

  public void setSubLocalityLevel1(String subLocalityLevel1) {
    this.subLocalityLevel1 = subLocalityLevel1;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public String getAdministrativeAreaLevel2() {
    return administrativeAreaLevel2;
  }

  public void setAdministrativeAreaLevel2(String administrativeAreaLevel2) {
    this.administrativeAreaLevel2 = administrativeAreaLevel2;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getAdministrativeAreaLevel1() {
    return administrativeAreaLevel1;
  }

  public void setAdministrativeAreaLevel1(String administrativeAreaLevel1) {
    this.administrativeAreaLevel1 = administrativeAreaLevel1;
  }

  public String getPostalCode() {
    return mPostalCode;
  }

  public void setPostalCode(String postalCode) {
    this.mPostalCode = postalCode;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(country);
    dest.writeString(address);
    dest.writeString(locality);
    dest.writeString(cityId);
    dest.writeString(subLocalityLevel2);
    dest.writeString(subLocalityLevel1);
    dest.writeString(route);
    dest.writeString(administrativeAreaLevel2);
    dest.writeString(cityName);
    dest.writeString(administrativeAreaLevel1);
    dest.writeString(mPostalCode);
  }
}
