package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerDetailsData implements Parcelable {
	private String firstName;
	private String lastName;
	private String countryCode;
	private String userTypeText;
	private String mobile;
	private String id;
	private int userType;
	private String mqttTopic;
	private String fcmTopic;
	private String email;

	public CustomerDetailsData(String firstName, String lastName, String countryCode, String userTypeText, String mobile, String id, int userType, String mqttTopic, String fcmTopic, String email) {
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

	protected CustomerDetailsData(Parcel in) {
		firstName = in.readString();
		lastName = in.readString();
		countryCode = in.readString();
		userTypeText = in.readString();
		mobile = in.readString();
		id = in.readString();
		userType = in.readInt();
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
		dest.writeInt(userType);
		dest.writeString(mqttTopic);
		dest.writeString(fcmTopic);
		dest.writeString(email);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<CustomerDetailsData> CREATOR = new Creator<CustomerDetailsData>() {
		@Override
		public CustomerDetailsData createFromParcel(Parcel in) {
			return new CustomerDetailsData(in);
		}

		@Override
		public CustomerDetailsData[] newArray(int size) {
			return new CustomerDetailsData[size];
		}
	};

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setUserTypeText(String userTypeText){
		this.userTypeText = userTypeText;
	}

	public String getUserTypeText(){
		return userTypeText;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUserType(int userType){
		this.userType = userType;
	}

	public int getUserType(){
		return userType;
	}

	public void setMqttTopic(String mqttTopic){
		this.mqttTopic = mqttTopic;
	}

	public String getMqttTopic(){
		return mqttTopic;
	}

	public void setFcmTopic(String fcmTopic){
		this.fcmTopic = fcmTopic;
	}

	public String getFcmTopic(){
		return fcmTopic;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}
