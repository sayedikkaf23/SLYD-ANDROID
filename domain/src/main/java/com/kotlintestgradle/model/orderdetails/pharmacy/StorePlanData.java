package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class StorePlanData implements Parcelable {
	private String appCommissionTypeText;
	private int appCommission;
	private String name;
	private int appCommissionType;

	public StorePlanData(String appCommissionTypeText, int appCommission, String name, int appCommissionType) {
		this.appCommissionTypeText = appCommissionTypeText;
		this.appCommission = appCommission;
		this.name = name;
		this.appCommissionType = appCommissionType;
	}

	protected StorePlanData(Parcel in) {
		appCommissionTypeText = in.readString();
		appCommission = in.readInt();
		name = in.readString();
		appCommissionType = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(appCommissionTypeText);
		dest.writeInt(appCommission);
		dest.writeString(name);
		dest.writeInt(appCommissionType);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<StorePlanData> CREATOR = new Creator<StorePlanData>() {
		@Override
		public StorePlanData createFromParcel(Parcel in) {
			return new StorePlanData(in);
		}

		@Override
		public StorePlanData[] newArray(int size) {
			return new StorePlanData[size];
		}
	};

	public void setAppCommissionTypeText(String appCommissionTypeText){
		this.appCommissionTypeText = appCommissionTypeText;
	}

	public String getAppCommissionTypeText(){
		return appCommissionTypeText;
	}

	public void setAppCommission(int appCommission){
		this.appCommission = appCommission;
	}

	public int getAppCommission(){
		return appCommission;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setAppCommissionType(int appCommissionType){
		this.appCommissionType = appCommissionType;
	}

	public int getAppCommissionType(){
		return appCommissionType;
	}
}
