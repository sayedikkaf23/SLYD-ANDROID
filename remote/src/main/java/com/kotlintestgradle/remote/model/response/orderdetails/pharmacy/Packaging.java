package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Packaging implements Parcelable, ValidItem {

	@Expose
	@SerializedName("unitType")
	private String unitType;

	@Expose
	@SerializedName("packingType")
	private String packingType;

	@Expose
	@SerializedName("unitValue")
	private int unitValue;

	protected Packaging(Parcel in) {
		unitType = in.readString();
		packingType = in.readString();
		unitValue = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(unitType);
		dest.writeString(packingType);
		dest.writeInt(unitValue);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Packaging> CREATOR = new Creator<Packaging>() {
		@Override
		public Packaging createFromParcel(Parcel in) {
			return new Packaging(in);
		}

		@Override
		public Packaging[] newArray(int size) {
			return new Packaging[size];
		}
	};

	public void setUnitType(String unitType){
		this.unitType = unitType;
	}

	public String getUnitType(){
		return unitType;
	}

	public void setPackingType(String packingType){
		this.packingType = packingType;
	}

	public String getPackingType(){
		return packingType;
	}

	public void setUnitValue(int unitValue){
		this.unitValue = unitValue;
	}

	public int getUnitValue(){
		return unitValue;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}