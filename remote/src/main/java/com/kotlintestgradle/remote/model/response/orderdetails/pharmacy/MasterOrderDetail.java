package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class MasterOrderDetail implements Parcelable, ValidItem {

	@Expose
	@SerializedName("data")
	private MasterOrdersDetail data;

	@Expose
	@SerializedName("message")
	private String message;

	protected MasterOrderDetail(Parcel in) {
		data = in.readParcelable(MasterOrdersDetail.class.getClassLoader());
		message = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(data, flags);
		dest.writeString(message);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<MasterOrderDetail> CREATOR = new Creator<MasterOrderDetail>() {
		@Override
		public MasterOrderDetail createFromParcel(Parcel in) {
			return new MasterOrderDetail(in);
		}

		@Override
		public MasterOrderDetail[] newArray(int size) {
			return new MasterOrderDetail[size];
		}
	};

	public void setData(MasterOrdersDetail data){
		this.data = data;
	}

	public MasterOrdersDetail getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}