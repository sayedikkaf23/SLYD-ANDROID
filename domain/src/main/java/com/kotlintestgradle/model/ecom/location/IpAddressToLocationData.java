package com.kotlintestgradle.model.ecom.location;

public class IpAddressToLocationData {

  private String ip;

  private String city;

  private String region;

  private String country;

  private String postal;

  private String timezone;

  private LocationData location;

  public IpAddressToLocationData(String ip, String city, String region, String country,
      String postal, String timezone, LocationData location) {
    this.ip = ip;
    this.city = city;
    this.region = region;
    this.country = country;
    this.postal = postal;
    this.timezone = timezone;
    this.location = location;
  }

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

  public LocationData getLocation() {
    return location;
  }

  public void setLocation(LocationData location) {
    this.location = location;
  }
}
