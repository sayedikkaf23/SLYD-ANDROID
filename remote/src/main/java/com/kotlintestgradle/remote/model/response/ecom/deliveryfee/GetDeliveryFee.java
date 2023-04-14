package com.kotlintestgradle.remote.model.response.ecom.deliveryfee;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import com.kotlintestgradle.remote.core.ValidItem;
public class GetDeliveryFee implements ValidItem, Parcelable {

	@Expose
	@SerializedName("data")
	private DeliveryData data;

	@Expose
	@SerializedName("message")
	private String message;

	protected GetDeliveryFee(Parcel in) {
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

	public static final Creator<GetDeliveryFee> CREATOR = new Creator<GetDeliveryFee>() {
		@Override
		public GetDeliveryFee createFromParcel(Parcel in) {
			return new GetDeliveryFee(in);
		}

		@Override
		public GetDeliveryFee[] newArray(int size) {
			return new GetDeliveryFee[size];
		}
	};

	public void setData(DeliveryData data){
		this.data = data;
	}

	public DeliveryData getData(){
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