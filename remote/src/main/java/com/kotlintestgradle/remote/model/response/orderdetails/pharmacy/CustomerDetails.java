package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CustomerDetails implements Parcelable, ValidItem {

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
	private int userType;

	@Expose
	@SerializedName("mqttTopic")
	private String mqttTopic;

	@Expose
	@SerializedName("fcmTopic")
	private String fcmTopic;

	@Expose
	@SerializedName("email")
	private String email;

	protected CustomerDetails(Parcel in) {
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

	public static final Creator<CustomerDetails> CREATOR = new Creator<CustomerDetails>() {
		@Override
		public CustomerDetails createFromParcel(Parcel in) {
			return new CustomerDetails(in);
		}

		@Override
		public CustomerDetails[] newArray(int size) {
			return new CustomerDetails[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}