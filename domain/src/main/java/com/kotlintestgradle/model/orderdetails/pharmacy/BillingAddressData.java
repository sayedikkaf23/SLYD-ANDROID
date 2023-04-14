package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class BillingAddressData implements Parcelable {
	private String city;
	private String mobileNumber;
	private double latitude;
	private String alternatePhoneCode;
	private String placeId;
	private int createdTimeStamp;
	private String cityId;
	private String countryId;
	private boolean jsonMemberDefault;
	private String cityName;
	private int tagged;
	private String zoneId;
	private String addLine1;
	private String addLine2;
	private String state;
	private String landmark;
	private String taggedAs;
	private double longitude;
	private String mobileNumberCode;
	private String pincode;
	private String flatNumber;
	private String alternatePhone;
	private String locality;
	private String timeZone;
	private String userId;
	private String createdIsoDate;
	private String name;
	private String fullAddress;
	private String id;
	private int userType;
	private String countryName;
	private String country;

	public BillingAddressData(String city, String mobileNumber, double latitude, String alternatePhoneCode, String placeId, int createdTimeStamp, String cityId, String countryId, boolean jsonMemberDefault, String cityName, int tagged, String zoneId, String addLine1, String addLine2, String state, String landmark, String taggedAs, double longitude, String mobileNumberCode, String pincode, String flatNumber, String alternatePhone, String locality, String timeZone, String userId, String createdIsoDate, String name, String fullAddress, String id, int userType, String countryName, String country) {
		this.city = city;
		this.mobileNumber = mobileNumber;
		this.latitude = latitude;
		this.alternatePhoneCode = alternatePhoneCode;
		this.placeId = placeId;
		this.createdTimeStamp = createdTimeStamp;
		this.cityId = cityId;
		this.countryId = countryId;
		this.jsonMemberDefault = jsonMemberDefault;
		this.cityName = cityName;
		this.tagged = tagged;
		this.zoneId = zoneId;
		this.addLine1 = addLine1;
		this.addLine2 = addLine2;
		this.state = state;
		this.landmark = landmark;
		this.taggedAs = taggedAs;
		this.longitude = longitude;
		this.mobileNumberCode = mobileNumberCode;
		this.pincode = pincode;
		this.flatNumber = flatNumber;
		this.alternatePhone = alternatePhone;
		this.locality = locality;
		this.timeZone = timeZone;
		this.userId = userId;
		this.createdIsoDate = createdIsoDate;
		this.name = name;
		this.fullAddress = fullAddress;
		this.id = id;
		this.userType = userType;
		this.countryName = countryName;
		this.country = country;
	}

	protected BillingAddressData(Parcel in) {
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
		country = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
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
		dest.writeString(country);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<BillingAddressData> CREATOR = new Creator<BillingAddressData>() {
		@Override
		public BillingAddressData createFromParcel(Parcel in) {
			return new BillingAddressData(in);
		}

		@Override
		public BillingAddressData[] newArray(int size) {
			return new BillingAddressData[size];
		}
	};

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

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}
}
