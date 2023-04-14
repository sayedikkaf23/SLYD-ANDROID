package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesData implements Parcelable {
	private String small;
	private String large;
	private String extraLarge;
	private String altText;
	private String medium;

	public ImagesData(String small, String large, String extraLarge, String altText, String medium) {
		this.small = small;
		this.large = large;
		this.extraLarge = extraLarge;
		this.altText = altText;
		this.medium = medium;
	}

	protected ImagesData(Parcel in) {
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

	public static final Creator<ImagesData> CREATOR = new Creator<ImagesData>() {
		@Override
		public ImagesData createFromParcel(Parcel in) {
			return new ImagesData(in);
		}

		@Override
		public ImagesData[] newArray(int size) {
			return new ImagesData[size];
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
}
