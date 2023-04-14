package com.kotlintestgradle.model.ecom;

public class UserData {
  private String name;
  private String mail;
  private String phoneNumber;
  private String countryCode;
  private String profilePic;
  private String city;
  private String region;
  private String country;

  public UserData() {
  }

  public UserData(String name, String mail, String phoneNumber, String countryCode,
      String profilePic, String city, String region, String country) {
    this.name = name;
    this.mail = mail;
    this.phoneNumber = phoneNumber;
    this.countryCode = countryCode;
    this.profilePic = profilePic;
    this.city = city;
    this.region = region;
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getProfilePic() {
    return profilePic;
  }

  public void setProfilePic(String profilePic) {
    this.profilePic = profilePic;
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
}
