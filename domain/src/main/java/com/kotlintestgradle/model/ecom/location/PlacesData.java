package com.kotlintestgradle.model.ecom.location;

public class PlacesData {
  private String fullAddress;
  private String placeId;
  private double lat;
  private double lang;
  private String subAddress;

  public PlacesData(String fullAddress, String placeId, double lat, double lang) {
    this.fullAddress = fullAddress;
    this.placeId = placeId;
    this.lat = lat;
    this.lang = lang;
  }
  public PlacesData(String fullAddress,String subAddress, String placeId, double lat, double lang) {
    this.fullAddress = fullAddress;
    this.placeId = placeId;
    this.lat = lat;
    this.lang = lang;
    this.subAddress = subAddress;
  }

  public String getFullAddress() {
    return fullAddress;
  }

  public void setFullAddress(String fullAddress) {
    this.fullAddress = fullAddress;
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLang() {
    return lang;
  }

  public void setLang(double lang) {
    this.lang = lang;
  }

  public String getSubAddress() {
    return subAddress;
  }

  public void setSubAddress(String subAddress) {
    this.subAddress = subAddress;
  }
}
