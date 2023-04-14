package com.kotlintestgradle.model.ecom.getcart;

public class CartUserData {

  private String firstName;

  private String city;

  private String countryCode;

  private String mobile;

  private String userIp;

  private String userPostCode;

  private String userId;

  private String userLong;

  private String userLat;

  private String email;

  public CartUserData(String firstName, String city, String countryCode, String mobile,
      String userIp, String userPostCode, String userId, String userLong, String userLat,
      String email) {
    this.firstName = firstName;
    this.city = city;
    this.countryCode = countryCode;
    this.mobile = mobile;
    this.userIp = userIp;
    this.userPostCode = userPostCode;
    this.userId = userId;
    this.userLong = userLong;
    this.userLat = userLat;
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getUserIp() {
    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }

  public String getUserPostCode() {
    return userPostCode;
  }

  public void setUserPostCode(String userPostCode) {
    this.userPostCode = userPostCode;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserLong() {
    return userLong;
  }

  public void setUserLong(String userLong) {
    this.userLong = userLong;
  }

  public String getUserLat() {
    return userLat;
  }

  public void setUserLat(String userLat) {
    this.userLat = userLat;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
