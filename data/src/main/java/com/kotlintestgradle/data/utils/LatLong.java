package com.kotlintestgradle.data.utils;

public class LatLong {
  private double lat;
  private double longitude;

  public LatLong(double lat, double longitude) {
    this.lat = lat;
    this.longitude = longitude;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
