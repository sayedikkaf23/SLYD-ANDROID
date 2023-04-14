package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class MasterOrdersDetails implements Parcelable {
	private OrdersData data;
	private String message;

	public MasterOrdersDetails(OrdersData data, String message) {
		this.data = data;
		this.message = message;
	}

	protected MasterOrdersDetails(Parcel in) {
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

	public static final Creator<MasterOrdersDetails> CREATOR = new Creator<MasterOrdersDetails>() {
		@Override
		public MasterOrdersDetails createFromParcel(Parcel in) {
			return new MasterOrdersDetails(in);
		}

		@Override
		public MasterOrdersDetails[] newArray(int size) {
			return new MasterOrdersDetails[size];
		}
	};

	public void setData(OrdersData data){
		this.data = data;
	}

	public OrdersData getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}
