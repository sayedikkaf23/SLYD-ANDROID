package com.kotlintestgradle.model.ecom.location;

public class LocationRideSpecialPoints {

  private double lat=0.0;
  private double lng=0.0;
  public LocationRideSpecialPoints(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }
}
