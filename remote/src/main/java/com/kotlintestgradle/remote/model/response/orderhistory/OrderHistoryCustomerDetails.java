package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistoryCustomerDetails  implements Parcelable , ValidItem {

  @Expose
  @SerializedName("firstName")
  private String firstName;

  @Expose
  @SerializedName("lastName")
  private String lastName;
  @Expose
  @SerializedName("countryCode")
  private String countryCode;
  @Expose
  @SerializedName("userTypeText")
  private String userTypeText;
  @Expose
  @SerializedName("mobile")
  private String mobile;
  @Expose
  @SerializedName("id")
  private String id;
  @Expose
  @SerializedName("userType")
  private String userType;
  @Expose
  @SerializedName("mqttTopic")
  private String mqttTopic;
  @Expose
  @SerializedName("fcmTopic")
  private String fcmTopic;
  @Expose
  @SerializedName("email")
  private String email;

  public OrderHistoryCustomerDetails(String firstName, String lastName, String countryCode,
      String userTypeText, String mobile, String id, String userType, String mqttTopic,
      String fcmTopic, String email) {
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

  protected OrderHistoryCustomerDetails(Parcel in) {
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

  public static final Creator<OrderHistoryCustomerDetails> CREATOR =
      new Creator<OrderHistoryCustomerDetails>() {
        @Override
        public OrderHistoryCustomerDetails createFromParcel(Parcel in) {
          return new OrderHistoryCustomerDetails(in);
        }

        @Override
        public OrderHistoryCustomerDetails[] newArray(int size) {
          return new OrderHistoryCustomerDetails[size];
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

  @Override
  public boolean isValid() {
    return true;
  }
}
