package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PickupAddress implements Parcelable, ValidItem {

	@Expose
	@SerializedName("country")
	private String country;

	@Expose
	@SerializedName("address")
	private String address;

	@Expose
	@SerializedName("googlePlaceId")
	private String googlePlaceId;

	@Expose
	@SerializedName("city")
	private String city;

	@Expose
	@SerializedName("locality")
	private String locality;

	@Expose
	@SerializedName("areaOrDistrict")
	private String areaOrDistrict;

	@Expose
	@SerializedName("googlePlaceName")
	private String googlePlaceName;

	@Expose
	@SerializedName("cityId")
	private String cityId;

	@Expose
	@SerializedName("long")
	private double jsonMemberLong;

	@Expose
	@SerializedName("route")
	private String route;

	@Expose
	@SerializedName("cityName")
	private String cityName;

	@Expose
	@SerializedName("addressLine1")
	private String addressLine1;

	@Expose
	@SerializedName("postCode")
	private String postCode;

	@Expose
	@SerializedName("addressLine2")
	private String addressLine2;

	@Expose
	@SerializedName("state")
	private String state;

	@Expose
	@SerializedName("addressArea")
	private String addressArea;

	@Expose
	@SerializedName("lat")
	private double lat;

	protected PickupAddress(Parcel in) {
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

	public static final Creator<PickupAddress> CREATOR = new Creator<PickupAddress>() {
		@Override
		public PickupAddress createFromParcel(Parcel in) {
			return new PickupAddress(in);
		}

		@Override
		public PickupAddress[] newArray(int size) {
			return new PickupAddress[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}