package com.kotlintestgradle.cache.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_address")
public class RecentAddress {
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "addressId")
  public String addressId;
  @ColumnInfo(name = "address")
  public String address;
  @ColumnInfo(name = "longitude")
  public double longitude;
  @ColumnInfo(name = "latitude")
  public double latitude;
  @ColumnInfo(name = "city")
  public String city;
  @ColumnInfo(name = "country")
  public String country;

  public RecentAddress(@NonNull String addressId, String address, double longitude, double latitude,
      String city, String country) {
    this.addressId = addressId;
    this.address = address;
    this.longitude = longitude;
    this.latitude = latitude;
    this.city = city;
    this.country = country;
  }

  @NonNull
  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(@NonNull String addressId) {
    this.addressId = addressId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
