package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class DeliveryAddress implements Parcelable, ValidItem {

	@Expose
	@SerializedName("country")
	private String country;

	@Expose
	@SerializedName("city")
	private String city;

	@Expose
	@SerializedName("mobileNumber")
	private String mobileNumber;

	@Expose
	@SerializedName("latitude")
	private double latitude;

	@Expose
	@SerializedName("alternatePhoneCode")
	private String alternatePhoneCode;

	@Expose
	@SerializedName("placeId")
	private String placeId;

	@SerializedName("createdTimeStamp")
	@Expose
	private int createdTimeStamp;

	@SerializedName("cityId")
	@Expose
	private String cityId;

	@SerializedName("countryId")
	@Expose
	private String countryId;

	@SerializedName("default")
	@Expose
	private boolean jsonMemberDefault;

	@SerializedName("cityName")
	@Expose
	private String cityName;

	@SerializedName("tagged")
	@Expose
	private int tagged;

	@SerializedName("zoneId")
	@Expose
	private String zoneId;

	@SerializedName("addLine1")
	@Expose
	private String addLine1;

	@Expose
	@SerializedName("addLine2")
	private String addLine2;

	@Expose
	@SerializedName("state")
	private String state;

	@Expose
	@SerializedName("landmark")
	private String landmark;

	@Expose
	@SerializedName("taggedAs")
	private String taggedAs;

	@Expose
	@SerializedName("longitude")
	private double longitude;

	@Expose
	@SerializedName("mobileNumberCode")
	private String mobileNumberCode;

	@Expose
	@SerializedName("pincode")
	private String pincode;

	@Expose
	@SerializedName("flatNumber")
	private String flatNumber;

	@SerializedName("alternatePhone")
	@Expose
	private String alternatePhone;

	@SerializedName("locality")
	@Expose
	private String locality;

	@SerializedName("timeZone")
	@Expose
	private String timeZone;

	@SerializedName("userId")
	@Expose
	private String userId;

	@SerializedName("createdIsoDate")
	@Expose
	private String createdIsoDate;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("fullAddress")
	@Expose
	private String fullAddress;

	@SerializedName("_id")
	@Expose
	private String id;

	@SerializedName("userType")
	@Expose
	private int userType;

	@SerializedName("countryName")
	@Expose
	private String countryName;

	protected DeliveryAddress(Parcel in) {
		country = in.readString();
		city = in.readString();
		mobileNumber = in.readString();
		latitude = in.readDouble();
		alternatePhoneCode = in.readString();
		placeId = in.readString();
		createdTimeStamp = in.readInt();
		cityId = in.readString();
		countryId = in.readString();
		jsonMemberDefault = in.readByte() != 0;
		cityName = in.readString();
		tagged = in.readInt();
		zoneId = in.readString();
		addLine1 = in.readString();
		addLine2 = in.readString();
		state = in.readString();
		landmark = in.readString();
		taggedAs = in.readString();
		longitude = in.readDouble();
		mobileNumberCode = in.readString();
		pincode = in.readString();
		flatNumber = in.readString();
		alternatePhone = in.readString();
		locality = in.readString();
		timeZone = in.readString();
		userId = in.readString();
		createdIsoDate = in.readString();
		name = in.readString();
		fullAddress = in.readString();
		id = in.readString();
		userType = in.readInt();
		countryName = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(country);
		dest.writeString(city);
		dest.writeString(mobileNumber);
		dest.writeDouble(latitude);
		dest.writeString(alternatePhoneCode);
		dest.writeString(placeId);
		dest.writeInt(createdTimeStamp);
		dest.writeString(cityId);
		dest.writeString(countryId);
		dest.writeByte((byte) (jsonMemberDefault ? 1 : 0));
		dest.writeString(cityName);
		dest.writeInt(tagged);
		dest.writeString(zoneId);
		dest.writeString(addLine1);
		dest.writeString(addLine2);
		dest.writeString(state);
		dest.writeString(landmark);
		dest.writeString(taggedAs);
		dest.writeDouble(longitude);
		dest.writeString(mobileNumberCode);
		dest.writeString(pincode);
		dest.writeString(flatNumber);
		dest.writeString(alternatePhone);
		dest.writeString(locality);
		dest.writeString(timeZone);
		dest.writeString(userId);
		dest.writeString(createdIsoDate);
		dest.writeString(name);
		dest.writeString(fullAddress);
		dest.writeString(id);
		dest.writeInt(userType);
		dest.writeString(countryName);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DeliveryAddress> CREATOR = new Creator<DeliveryAddress>() {
		@Override
		public DeliveryAddress createFromParcel(Parcel in) {
			return new DeliveryAddress(in);
		}

		@Override
		public DeliveryAddress[] newArray(int size) {
			return new DeliveryAddress[size];
		}
	};

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setMobileNumber(String mobileNumber){
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setAlternatePhoneCode(String alternatePhoneCode){
		this.alternatePhoneCode = alternatePhoneCode;
	}

	public String getAlternatePhoneCode(){
		return alternatePhoneCode;
	}

	public void setPlaceId(String placeId){
		this.placeId = placeId;
	}

	public String getPlaceId(){
		return placeId;
	}

	public void setCreatedTimeStamp(int createdTimeStamp){
		this.createdTimeStamp = createdTimeStamp;
	}

	public int getCreatedTimeStamp(){
		return createdTimeStamp;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	public void setCountryId(String countryId){
		this.countryId = countryId;
	}

	public String getCountryId(){
		return countryId;
	}

	public void setJsonMemberDefault(boolean jsonMemberDefault){
		this.jsonMemberDefault = jsonMemberDefault;
	}

	public boolean isJsonMemberDefault(){
		return jsonMemberDefault;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setTagged(int tagged){
		this.tagged = tagged;
	}

	public int getTagged(){
		return tagged;
	}

	public void setZoneId(String zoneId){
		this.zoneId = zoneId;
	}

	public String getZoneId(){
		return zoneId;
	}

	public void setAddLine1(String addLine1){
		this.addLine1 = addLine1;
	}

	public String getAddLine1(){
		return addLine1;
	}

	public void setAddLine2(String addLine2){
		this.addLine2 = addLine2;
	}

	public String getAddLine2(){
		return addLine2;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setLandmark(String landmark){
		this.landmark = landmark;
	}

	public String getLandmark(){
		return landmark;
	}

	public void setTaggedAs(String taggedAs){
		this.taggedAs = taggedAs;
	}

	public String getTaggedAs(){
		return taggedAs;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}

	public void setMobileNumberCode(String mobileNumberCode){
		this.mobileNumberCode = mobileNumberCode;
	}

	public String getMobileNumberCode(){
		return mobileNumberCode;
	}

	public void setPincode(String pincode){
		this.pincode = pincode;
	}

	public String getPincode(){
		return pincode;
	}

	public void setFlatNumber(String flatNumber){
		this.flatNumber = flatNumber;
	}

	public String getFlatNumber(){
		return flatNumber;
	}

	public void setAlternatePhone(String alternatePhone){
		this.alternatePhone = alternatePhone;
	}

	public String getAlternatePhone(){
		return alternatePhone;
	}

	public void setLocality(String locality){
		this.locality = locality;
	}

	public String getLocality(){
		return locality;
	}

	public void setTimeZone(String timeZone){
		this.timeZone = timeZone;
	}

	public String getTimeZone(){
		return timeZone;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setCreatedIsoDate(String createdIsoDate){
		this.createdIsoDate = createdIsoDate;
	}

	public String getCreatedIsoDate(){
		return createdIsoDate;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setFullAddress(String fullAddress){
		this.fullAddress = fullAddress;
	}

	public String getFullAddress(){
		return fullAddress;
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

	public void setCountryName(String countryName){
		this.countryName = countryName;
	}

	public String getCountryName(){
		return countryName;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}