package com.kotlintestgradle.model.ecom.pdp.substitutes;

import android.os.Parcel;
import android.os.Parcelable;

public class Images implements Parcelable {
	private String small;
	private String large;
	private String extraLarge;
	private String altText;
	private String medium;

	public Images(String small, String large, String extraLarge, String altText, String medium) {
		this.small = small;
		this.large = large;
		this.extraLarge = extraLarge;
		this.altText = altText;
		this.medium = medium;
	}

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
}
