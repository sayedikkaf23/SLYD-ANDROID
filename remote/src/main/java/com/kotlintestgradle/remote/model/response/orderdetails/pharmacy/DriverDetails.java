package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class DriverDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("lastName")
	private String lastName;

	@Expose
	@SerializedName("firstName")
	private String firstName;

	@Expose
	@SerializedName("driverId")
	private String driverId;

	@Expose
	@SerializedName("countryCode")
	private String countryCode;

	@Expose
	@SerializedName("profilePic")
	private String profilePic;

	@Expose
	@SerializedName("mobile")
	private String mobile;

	@Expose
	@SerializedName("location")
	private Location location;

	@Expose
	@SerializedName("mqttTopic")
	private String mqttTopic;

	@Expose
	@SerializedName("fcmTopic")
	private String fcmTopic;

	@Expose
	@SerializedName("storeId")
	private String storeId;

	@Expose
	@SerializedName("email")
	private String email;

	@Expose
	@SerializedName("driverType")
	private int driverType;

	protected DriverDetails(Parcel in) {
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

	public static final Creator<DriverDetails> CREATOR = new Creator<DriverDetails>() {
		@Override
		public DriverDetails createFromParcel(Parcel in) {
			return new DriverDetails(in);
		}

		@Override
		public DriverDetails[] newArray(int size) {
			return new DriverDetails[size];
		}
	};

	public String getLastName(){
		return lastName;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getDriverId(){
		return driverId;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public String getProfilePic(){
		return profilePic;
	}

	public String getMobile(){
		return mobile;
	}

	public Location getLocation(){
		return location;
	}

	public String getMqttTopic(){
		return mqttTopic;
	}

	public String getFcmTopic(){
		return fcmTopic;
	}

	public String getStoreId(){
		return storeId;
	}

	public String getEmail(){
		return email;
	}

	public int getDriverType(){
		return driverType;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setMqttTopic(String mqttTopic) {
		this.mqttTopic = mqttTopic;
	}

	public void setFcmTopic(String fcmTopic) {
		this.fcmTopic = fcmTopic;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDriverType(int driverType) {
		this.driverType = driverType;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
