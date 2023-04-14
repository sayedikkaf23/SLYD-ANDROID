package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class PackagingData implements Parcelable {
	private String unitType;
	private String packingType;
	private int unitValue;

	public PackagingData(String unitType, String packingType, int unitValue) {
		this.unitType = unitType;
		this.packingType = packingType;
		this.unitValue = unitValue;
	}

	protected PackagingData(Parcel in) {
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

	public static final Creator<PackagingData> CREATOR = new Creator<PackagingData>() {
		@Override
		public PackagingData createFromParcel(Parcel in) {
			return new PackagingData(in);
		}

		@Override
		public PackagingData[] newArray(int size) {
			return new PackagingData[size];
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
}
