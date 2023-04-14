package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images implements Parcelable, ValidItem {

	@Expose
	@SerializedName("small")
	private String small;

	@Expose
	@SerializedName("large")
	private String large;

	@Expose
	@SerializedName("extraLarge")
	private String extraLarge;

	@Expose
	@SerializedName("altText")
	private String altText;

	@Expose
	@SerializedName("medium")
	private String medium;

	protected Images(Parcel in) {
		small = in.readString();
		large = in.readString();
		extraLarge = in.readString();
		altText = in.readString();
		medium = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(small);
		dest.writeString(large);
		dest.writeString(extraLarge);
		dest.writeString(altText);
		dest.writeString(medium);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Images> CREATOR = new Creator<Images>() {
		@Override
		public Images createFromParcel(Parcel in) {
			return new Images(in);
		}

		@Override
		public Images[] newArray(int size) {
			return new Images[size];
		}
	};

	public void setSmall(String small){
		this.small = small;
	}

	public String getSmall(){
		return small;
	}

	public void setLarge(String large){
		this.large = large;
	}

	public String getLarge(){
		return large;
	}

	public void setExtraLarge(String extraLarge){
		this.extraLarge = extraLarge;
	}

	public String getExtraLarge(){
		return extraLarge;
	}

	public void setAltText(String altText){
		this.altText = altText;
	}

	public String getAltText(){
		return altText;
	}

	public void setMedium(String medium){
		this.medium = medium;
	}

	public String getMedium(){
		return medium;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}