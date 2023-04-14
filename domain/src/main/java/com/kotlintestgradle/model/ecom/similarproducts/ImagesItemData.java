package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesItemData implements Parcelable {
	private String small;
	private String extraLarge;
	private String altText;
	private String large;
	private String medium;

	public ImagesItemData(String small, String extraLarge, String altText, String large, String medium) {
		this.small = small;
		this.extraLarge = extraLarge;
		this.altText = altText;
		this.large = large;
		this.medium = medium;
	}

	protected ImagesItemData(Parcel in) {
		small = in.readString();
		extraLarge = in.readString();
		altText = in.readString();
		large = in.readString();
		medium = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(small);
		dest.writeString(extraLarge);
		dest.writeString(altText);
		dest.writeString(large);
		dest.writeString(medium);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ImagesItemData> CREATOR = new Creator<ImagesItemData>() {
		@Override
		public ImagesItemData createFromParcel(Parcel in) {
			return new ImagesItemData(in);
		}

		@Override
		public ImagesItemData[] newArray(int size) {
			return new ImagesItemData[size];
		}
	};

	public void setSmall(String small){
		this.small = small;
	}

	public String getSmall(){
		return small;
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

	public void setLarge(String large){
		this.large = large;
	}

	public String getLarge(){
		return large;
	}

	public void setMedium(String medium){
		this.medium = medium;
	}

	public String getMedium(){
		return medium;
	}
}
