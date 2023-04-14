package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CartUserDetails implements Parcelable, ValidItem {
  @Expose
  @SerializedName("firstName")
  private String firstName;

  @Expose
  @SerializedName("city")
  private String city;

  @Expose
  @SerializedName("countryCode")
  private String countryCode;

  @Expose
  @SerializedName("mobile")
  private String mobile;

  @Expose
  @SerializedName("userIp")
  private String userIp;

  @Expose
  @SerializedName("userPostCode")
  private String userPostCode;

  @Expose
  @SerializedName("userId")
  private String userId;

  @Expose
  @SerializedName("userLong")
  private String userLong;

  @Expose
  @SerializedName("userLat")
  private String userLat;

  @Expose
  @SerializedName("email")
  private String email;

  public CartUserDetails(String firstName, String city, String countryCode, String mobile,
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

  protected CartUserDetails(Parcel in) {
    firstName = in.readString();
    city = in.readString();
    countryCode = in.readString();
    mobile = in.readString();
    userIp = in.readString();
    userPostCode = in.readString();
    userId = in.readString();
    userLong = in.readString();
    userLat = in.readString();
    email = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(firstName);
    dest.writeString(city);
    dest.writeString(countryCode);
    dest.writeString(mobile);
    dest.writeString(userIp);
    dest.writeString(userPostCode);
    dest.writeString(userId);
    dest.writeString(userLong);
    dest.writeString(userLat);
    dest.writeString(email);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartUserDetails> CREATOR = new Creator<CartUserDetails>() {
    @Override
    public CartUserDetails createFromParcel(Parcel in) {
      return new CartUserDetails(in);
    }

    @Override
    public CartUserDetails[] newArray(int size) {
      return new CartUserDetails[size];
    }
  };

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

  @Override
  public boolean isValid() {
    return true;
  }
}
