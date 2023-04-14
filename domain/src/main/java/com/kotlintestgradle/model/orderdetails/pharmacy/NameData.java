package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class NameData implements Parcelable {
	private String en;

	public NameData(String en) {
		this.en = en;
	}

	protected NameData(Parcel in) {
		en = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(en);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<NameData> CREATOR = new Creator<NameData>() {
		@Override
		public NameData createFromParcel(Parcel in) {
			return new NameData(in);
		}

		@Override
		public NameData[] newArray(int size) {
			return new NameData[size];
		}
	};

	public void setEn(String en){
		this.en = en;
	}

	public String getEn(){
		return en;
	}
}
