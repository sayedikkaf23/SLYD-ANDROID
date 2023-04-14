package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class QuantityData implements Parcelable {
	private String unit;
	private int value;

	public QuantityData(String unit, int value) {
		this.unit = unit;
		this.value = value;
	}

	protected QuantityData(Parcel in) {
		unit = in.readString();
		value = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(unit);
		dest.writeInt(value);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<QuantityData> CREATOR = new Creator<QuantityData>() {
		@Override
		public QuantityData createFromParcel(Parcel in) {
			return new QuantityData(in);
		}

		@Override
		public QuantityData[] newArray(int size) {
			return new QuantityData[size];
		}
	};

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}

	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}
}
