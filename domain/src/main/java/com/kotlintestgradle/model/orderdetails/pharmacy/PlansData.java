package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class PlansData implements Parcelable {
	private String appCommissionTypeText;
	private int appCommission;
	private String name;
	private int appCommissionType;

	public PlansData(String appCommissionTypeText, int appCommission, String name, int appCommissionType) {
		this.appCommissionTypeText = appCommissionTypeText;
		this.appCommission = appCommission;
		this.name = name;
		this.appCommissionType = appCommissionType;
	}

	protected PlansData(Parcel in) {
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

	public static final Creator<PlansData> CREATOR = new Creator<PlansData>() {
		@Override
		public PlansData createFromParcel(Parcel in) {
			return new PlansData(in);
		}

		@Override
		public PlansData[] newArray(int size) {
			return new PlansData[size];
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
