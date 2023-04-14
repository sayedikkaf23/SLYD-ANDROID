package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class PickupAddressData implements Parcelable {
	private String country;
	private String address;
	private String googlePlaceId;
	private String city;
	private String locality;
	private String areaOrDistrict;
	private String googlePlaceName;
	private String cityId;
	private double jsonMemberLong;
	private String route;
	private String cityName;
	private String addressLine1;
	private String postCode;
	private String addressLine2;
	private String state;
	private String addressArea;
	private double lat;

	public PickupAddressData(String country, String address, String googlePlaceId, String city, String locality, String areaOrDistrict, String googlePlaceName, String cityId, double jsonMemberLong, String route, String cityName, String addressLine1, String postCode, String addressLine2, String state, String addressArea, double lat) {
		this.country = country;
		this.address = address;
		this.googlePlaceId = googlePlaceId;
		this.city = city;
		this.locality = locality;
		this.areaOrDistrict = areaOrDistrict;
		this.googlePlaceName = googlePlaceName;
		this.cityId = cityId;
		this.jsonMemberLong = jsonMemberLong;
		this.route = route;
		this.cityName = cityName;
		this.addressLine1 = addressLine1;
		this.postCode = postCode;
		this.addressLine2 = addressLine2;
		this.state = state;
		this.addressArea = addressArea;
		this.lat = lat;
	}

	protected PickupAddressData(Parcel in) {
		country = in.readString();
		address = in.readString();
		googlePlaceId = in.readString();
		city = in.readString();
		locality = in.readString();
		areaOrDistrict = in.readString();
		googlePlaceName = in.readString();
		cityId = in.readString();
		jsonMemberLong = in.readDouble();
		route = in.readString();
		cityName = in.readString();
		addressLine1 = in.readString();
		postCode = in.readString();
		addressLine2 = in.readString();
		state = in.readString();
		addressArea = in.readString();
		lat = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(country);
		dest.writeString(address);
		dest.writeString(googlePlaceId);
		dest.writeString(city);
		dest.writeString(locality);
		dest.writeString(areaOrDistrict);
		dest.writeString(googlePlaceName);
		dest.writeString(cityId);
		dest.writeDouble(jsonMemberLong);
		dest.writeString(route);
		dest.writeString(cityName);
		dest.writeString(addressLine1);
		dest.writeString(postCode);
		dest.writeString(addressLine2);
		dest.writeString(state);
		dest.writeString(addressArea);
		dest.writeDouble(lat);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PickupAddressData> CREATOR = new Creator<PickupAddressData>() {
		@Override
		public PickupAddressData createFromParcel(Parcel in) {
			return new PickupAddressData(in);
		}

		@Override
		public PickupAddressData[] newArray(int size) {
			return new PickupAddressData[size];
		}
	};

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setGooglePlaceId(String googlePlaceId){
		this.googlePlaceId = googlePlaceId;
	}

	public String getGooglePlaceId(){
		return googlePlaceId;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLocality(String locality){
		this.locality = locality;
	}

	public String getLocality(){
		return locality;
	}

	public void setAreaOrDistrict(String areaOrDistrict){
		this.areaOrDistrict = areaOrDistrict;
	}

	public String getAreaOrDistrict(){
		return areaOrDistrict;
	}

	public void setGooglePlaceName(String googlePlaceName){
		this.googlePlaceName = googlePlaceName;
	}

	public String getGooglePlaceName(){
		return googlePlaceName;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	public void setJsonMemberLong(double jsonMemberLong){
		this.jsonMemberLong = jsonMemberLong;
	}

	public double getJsonMemberLong(){
		return jsonMemberLong;
	}

	public void setRoute(String route){
		this.route = route;
	}

	public String getRoute(){
		return route;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setAddressLine1(String addressLine1){
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine1(){
		return addressLine1;
	}

	public void setPostCode(String postCode){
		this.postCode = postCode;
	}

	public String getPostCode(){
		return postCode;
	}

	public void setAddressLine2(String addressLine2){
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine2(){
		return addressLine2;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setAddressArea(String addressArea){
		this.addressArea = addressArea;
	}

	public String getAddressArea(){
		return addressArea;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}
}
