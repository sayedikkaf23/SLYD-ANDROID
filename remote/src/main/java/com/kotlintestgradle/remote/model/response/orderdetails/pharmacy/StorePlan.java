package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StorePlan implements Parcelable, ValidItem {

	@SerializedName("appCommissionTypeText")
	@Expose
	private String appCommissionTypeText;

	@SerializedName("appCommission")
	@Expose
	private int appCommission;

	@Expose
	@SerializedName("name")
	private String name;

	@Expose
	@SerializedName("appCommissionType")
	private int appCommissionType;

	protected StorePlan(Parcel in) {
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

	public static final Creator<StorePlan> CREATOR = new Creator<StorePlan>() {
		@Override
		public StorePlan createFromParcel(Parcel in) {
			return new StorePlan(in);
		}

		@Override
		public StorePlan[] newArray(int size) {
			return new StorePlan[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}