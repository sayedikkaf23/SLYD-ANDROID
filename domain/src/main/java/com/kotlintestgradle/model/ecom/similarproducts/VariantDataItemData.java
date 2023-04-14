package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;

public class VariantDataItemData implements Parcelable {
	private String value;

	public VariantDataItemData(String value) {
		this.value = value;
	}

	protected VariantDataItemData(Parcel in) {
		value = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(value);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<VariantDataItemData> CREATOR = new Creator<VariantDataItemData>() {
		@Override
		public VariantDataItemData createFromParcel(Parcel in) {
			return new VariantDataItemData(in);
		}

		@Override
		public VariantDataItemData[] newArray(int size) {
			return new VariantDataItemData[size];
		}
	};

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}
}
