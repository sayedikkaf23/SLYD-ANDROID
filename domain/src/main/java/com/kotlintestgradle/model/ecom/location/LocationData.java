package com.kotlintestgradle.model.ecom.location;

public class  LocationData {
  private double latitude=0.0;
  private double longitude=0.0;
  private String cityName="";
  private String address="";

  public LocationData(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.cityName = cityName;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public LocationData(double latitude, double longitude, String cityName) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.cityName = cityName;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
