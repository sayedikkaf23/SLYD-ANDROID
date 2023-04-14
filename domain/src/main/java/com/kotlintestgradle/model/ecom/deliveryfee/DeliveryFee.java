package com.kotlintestgradle.model.ecom.deliveryfee;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryFee implements Parcelable {
	private Data data;
	private String message;

	public DeliveryFee(Data data, String message) {
		this.data = data;
		this.message = message;
	}

	protected DeliveryFee(Parcel in) {
		message = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(message);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DeliveryFee> CREATOR = new Creator<DeliveryFee>() {
		@Override
		public DeliveryFee createFromParcel(Parcel in) {
			return new DeliveryFee(in);
		}

		@Override
		public DeliveryFee[] newArray(int size) {
			return new DeliveryFee[size];
		}
	};

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}
