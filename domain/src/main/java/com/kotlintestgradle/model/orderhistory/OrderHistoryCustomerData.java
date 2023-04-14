package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistoryCustomerData implements Parcelable {

  private String firstName;

  private String lastName;

  private String countryCode;

  private String userTypeText;

  private String mobile;

  private String id;

  private String userType;

  private String mqttTopic;

  private String fcmTopic;

  private String email;

  public OrderHistoryCustomerData(String firstName, String lastName, String countryCode, String userTypeText, String mobile, String id, String userType, String mqttTopic, String fcmTopic, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.countryCode = countryCode;
    this.userTypeText = userTypeText;
    this.mobile = mobile;
    this.id = id;
    this.userType = userType;
    this.mqttTopic = mqttTopic;
    this.fcmTopic = fcmTopic;
    this.email = email;
  }

  protected OrderHistoryCustomerData(Parcel in) {
    firstName = in.readString();
    lastName = in.readString();
    countryCode = in.readString();
    userTypeText = in.readString();
    mobile = in.readString();
    id = in.readString();
    userType = in.readString();
    mqttTopic = in.readString();
    fcmTopic = in.readString();
    email = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(firstName);
    dest.writeString(lastName);
    dest.writeString(countryCode);
    dest.writeString(userTypeText);
    dest.writeString(mobile);
    dest.writeString(id);
    dest.writeString(userType);
    dest.writeString(mqttTopic);
    dest.writeString(fcmTopic);
    dest.writeString(email);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistoryCustomerData> CREATOR = new Creator<OrderHistoryCustomerData>() {
    @Override
    public OrderHistoryCustomerData createFromParcel(Parcel in) {
      return new OrderHistoryCustomerData(in);
    }

    @Override
    public OrderHistoryCustomerData[] newArray(int size) {
      return new OrderHistoryCustomerData[size];
    }
  };

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getUserTypeText() {
    return userTypeText;
  }

  public void setUserTypeText(String userTypeText) {
    this.userTypeText = userTypeText;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getMqttTopic() {
    return mqttTopic;
  }

  public void setMqttTopic(String mqttTopic) {
    this.mqttTopic = mqttTopic;
  }

  public String getFcmTopic() {
    return fcmTopic;
  }

  public void setFcmTopic(String fcmTopic) {
    this.fcmTopic = fcmTopic;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
