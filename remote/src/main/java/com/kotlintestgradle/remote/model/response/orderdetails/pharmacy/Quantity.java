package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quantity implements Parcelable, ValidItem {

	@Expose
	@SerializedName("unit")
	private String unit;

	@Expose
	@SerializedName("value")
	private int value;

	protected Quantity(Parcel in) {
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

	public static final Creator<Quantity> CREATOR = new Creator<Quantity>() {
		@Override
		public Quantity createFromParcel(Parcel in) {
			return new Quantity(in);
		}

		@Override
		public Quantity[] newArray(int size) {
			return new Quantity[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}