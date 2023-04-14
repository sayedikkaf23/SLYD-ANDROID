package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class BagsItemData implements Parcelable {
	private String bagId;
	private String lable;

	public BagsItemData(String bagId, String lable) {
		this.bagId = bagId;
		this.lable = lable;
	}

	protected BagsItemData(Parcel in) {
		bagId = in.readString();
		lable = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(bagId);
		dest.writeString(lable);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<BagsItemData> CREATOR = new Creator<BagsItemData>() {
		@Override
		public BagsItemData createFromParcel(Parcel in) {
			return new BagsItemData(in);
		}

		@Override
		public BagsItemData[] newArray(int size) {
			return new BagsItemData[size];
		}
	};

	public void setBagId(String bagId){
		this.bagId = bagId;
	}

	public String getBagId(){
		return bagId;
	}

	public void setLable(String lable){
		this.lable = lable;
	}

	public String getLable(){
		return lable;
	}
}
