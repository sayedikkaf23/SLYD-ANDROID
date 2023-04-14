package com.kotlintestgradle.cache.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "address")
public class UserAddress {
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "addressId")
  public String id;
  @ColumnInfo(name = "mobileNumberCode")
  public String mobileNumberCode;
  @ColumnInfo(name = "country")
  public String country;
  @ColumnInfo(name = "pinCode")
  public String pinCode;
  @ColumnInfo(name = "city")
  public String city;
  @ColumnInfo(name = "mobileNumber")
  public String mobileNumber;
  @ColumnInfo(name = "flatNumber")
  public String flatNumber;
  @ColumnInfo(name = "latitude")
  public String latitude;
  @ColumnInfo(name = "alternatePhoneCode")
  public String alternatePhoneCode;
  @ColumnInfo(name = "alternatePhone")
  public String alternatePhone;
  @ColumnInfo(name = "locality")
  public String locality;
  @ColumnInfo(name = "placeId")
  public String placeId;
  @ColumnInfo(name = "createdTimeStamp")
  public String createdTimeStamp;
  @ColumnInfo(name = "userId")
  public String userId;
  @ColumnInfo(name = "createdIsoDate")
  public String createdIsoDate;
  @ColumnInfo(name = "username")
  public String name;
  @ColumnInfo(name = "addLine1")
  public String addLine1;
  @ColumnInfo(name = "addLine2")
  public String addLine2;
  @ColumnInfo(name = "state")
  public String state;
  @ColumnInfo(name = "userType")
  public String userType;
  @ColumnInfo(name = "landmark")
  public String landmark;
  @ColumnInfo(name = "taggedAs")
  public String taggedAs;
  @ColumnInfo(name = "tagged")
  public String tagged;
  @ColumnInfo(name = "longitude")
  public String longitude;
  @ColumnInfo(name = "IsDefault")
  public String defaultAd;
  @ColumnInfo(name = "cityId")
  public String cityId;
  @ColumnInfo(name = "countryId")
  public String countryId;
  public UserAddress(@NotNull String id, String mobileNumberCode, String country, String pinCode,
      String city, String mobileNumber, String flatNumber, String latitude,
      String alternatePhoneCode, String alternatePhone, String locality, String placeId,
      String createdTimeStamp, String userId, String createdIsoDate, String name,
      String addLine1, String addLine2, String state, String userType, String landmark,
      String taggedAs, String tagged, String defaultAd, String longitude,String cityId,String countryId) {
    this.id = id;
    this.mobileNumberCode = mobileNumberCode;
    this.country = country;
    this.pinCode = pinCode;
    this.city = city;
    this.mobileNumber = mobileNumber;
    this.flatNumber = flatNumber;
    this.latitude = latitude;
    this.alternatePhoneCode = alternatePhoneCode;
    this.alternatePhone = alternatePhone;
    this.locality = locality;
    this.placeId = placeId;
    this.createdTimeStamp = createdTimeStamp;
    this.userId = userId;
    this.createdIsoDate = createdIsoDate;
    this.name = name;
    this.addLine1 = addLine1;
    this.addLine2 = addLine2;
    this.state = state;
    this.userType = userType;
    this.landmark = landmark;
    this.taggedAs = taggedAs;
    this.tagged = tagged;
    this.longitude = longitude;
    this.defaultAd = defaultAd;
    this.cityId=cityId;
    this.countryId=countryId;
  }

  @NotNull
  public String getId() {
    return id;
  }

  public void setId(@NotNull String id) {
    this.id = id;
  }

  public String getMobileNumberCode() {
    return mobileNumberCode;
  }

  public void setMobileNumberCode(String mobileNumberCode) {
    this.mobileNumberCode = mobileNumberCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getFlatNumber() {
    return flatNumber;
  }

  public void setFlatNumber(String flatNumber) {
    this.flatNumber = flatNumber;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getAlternatePhoneCode() {
    return alternatePhoneCode;
  }

  public void setAlternatePhoneCode(String alternatePhoneCode) {
    this.alternatePhoneCode = alternatePhoneCode;
  }

  public String getAlternatePhone() {
    return alternatePhone;
  }

  public void setAlternatePhone(String alternatePhone) {
    this.alternatePhone = alternatePhone;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public String getCreatedTimeStamp() {
    return createdTimeStamp;
  }

  public void setCreatedTimeStamp(String createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getCreatedIsoDate() {
    return createdIsoDate;
  }

  public void setCreatedIsoDate(String createdIsoDate) {
    this.createdIsoDate = createdIsoDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddLine1() {
    return addLine1;
  }

  public void setAddLine1(String addLine1) {
    this.addLine1 = addLine1;
  }

  public String getAddLine2() {
    return addLine2;
  }

  public void setAddLine2(String addLine2) {
    this.addLine2 = addLine2;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getLandmark() {
    return landmark;
  }

  public void setLandmark(String landmark) {
    this.landmark = landmark;
  }

  public String getTaggedAs() {
    return taggedAs;
  }

  public void setTaggedAs(String taggedAs) {
    this.taggedAs = taggedAs;
  }

  public String getTagged() {
    return tagged;
  }

  public void setTagged(String tagged) {
    this.tagged = tagged;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getDefaultAd() {
    return defaultAd;
  }

  public void setDefaultAd(String defaultAd) {
    this.defaultAd = defaultAd;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public String getCountryId() {
    return countryId;
  }

  public void setCountryId(String countryId) {
    this.countryId = countryId;
  }
}
