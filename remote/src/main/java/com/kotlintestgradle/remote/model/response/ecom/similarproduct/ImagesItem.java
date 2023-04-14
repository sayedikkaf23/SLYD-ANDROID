package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ImagesItem implements Parcelable, ValidItem {

	@Expose
	@SerializedName("small")
	private String small;

	@Expose
	@SerializedName("extraLarge")
	private String extraLarge;

	@Expose
	@SerializedName("altText")
	private String altText;

	@Expose
	@SerializedName("large")
	private String large;

	@Expose
	@SerializedName("medium")
	private String medium;

	protected ImagesItem(Parcel in) {
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

	public static final Creator<ImagesItem> CREATOR = new Creator<ImagesItem>() {
		@Override
		public ImagesItem createFromParcel(Parcel in) {
			return new ImagesItem(in);
		}

		@Override
		public ImagesItem[] newArray(int size) {
			return new ImagesItem[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}