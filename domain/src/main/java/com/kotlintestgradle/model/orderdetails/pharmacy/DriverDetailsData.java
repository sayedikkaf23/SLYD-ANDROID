package com.kotlintestgradle.model.orderdetails.pharmacy;


import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.model.ecom.location.LocationData;

public class DriverDetailsData implements Parcelable {
	private String lastName;
	private String firstName;
	private String driverId;
	private String countryCode;
	private String profilePic;
	private String mobile;
	private com.kotlintestgradle.model.ecom.location.LocationData location;
	private String mqttTopic;
	private String fcmTopic;
	private String storeId;
	private String email;
	private int driverType;

	public DriverDetailsData(String lastName, String firstName, String driverId, String countryCode, String profilePic, String mobile, com.kotlintestgradle.model.ecom.location.LocationData location, String mqttTopic, String fcmTopic, String storeId, String email, int driverType) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.driverId = driverId;
		this.countryCode = countryCode;
		this.profilePic = profilePic;
		this.mobile = mobile;
		this.location = location;
		this.mqttTopic = mqttTopic;
		this.fcmTopic = fcmTopic;
		this.storeId = storeId;
		this.email = email;
		this.driverType = driverType;
	}

	protected DriverDetailsData(Parcel in) {
		lastName = in.readString();
		firstName = in.readString();
		driverId = in.readString();
		countryCode = in.readString();
		profilePic = in.readString();
		mobile = in.readString();
		mqttTopic = in.readString();
		fcmTopic = in.readString();
		storeId = in.readString();
		email = in.readString();
		driverType = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(lastName);
		dest.writeString(firstName);
		dest.writeString(driverId);
		dest.writeString(countryCode);
		dest.writeString(profilePic);
		dest.writeString(mobile);
		dest.writeString(mqttTopic);
		dest.writeString(fcmTopic);
		dest.writeString(storeId);
		dest.writeString(email);
		dest.writeInt(driverType);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DriverDetailsData> CREATOR = new Creator<DriverDetailsData>() {
		@Override
		public DriverDetailsData createFromParcel(Parcel in) {
			return new DriverDetailsData(in);
		}

		@Override
		public DriverDetailsData[] newArray(int size) {
			return new DriverDetailsData[size];
		}
	};

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setDriverId(String driverId){
		this.driverId = driverId;
	}

	public String getDriverId(){
		return driverId;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setProfilePic(String profilePic){
		this.profilePic = profilePic;
	}

	public String getProfilePic(){
		return profilePic;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setLocation(com.kotlintestgradle.model.ecom.location.LocationData location){
		this.location = location;
	}

	public LocationData getLocation(){
		return location;
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

	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return storeId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setDriverType(int driverType){
		this.driverType = driverType;
	}

	public int getDriverType(){
		return driverType;
	}
}
