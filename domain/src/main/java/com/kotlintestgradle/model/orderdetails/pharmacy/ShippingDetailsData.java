package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class ShippingDetailsData implements Parcelable {
	private String name;
	private String id;
	private String trackingId;

	public ShippingDetailsData(String name, String id, String trackingId) {
		this.name = name;
		this.id = id;
		this.trackingId = trackingId;
	}

	protected ShippingDetailsData(Parcel in) {
		name = in.readString();
		id = in.readString();
		trackingId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(id);
		dest.writeString(trackingId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ShippingDetailsData> CREATOR = new Creator<ShippingDetailsData>() {
		@Override
		public ShippingDetailsData createFromParcel(Parcel in) {
			return new ShippingDetailsData(in);
		}

		@Override
		public ShippingDetailsData[] newArray(int size) {
			return new ShippingDetailsData[size];
		}
	};

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTrackingId(String trackingId){
		this.trackingId = trackingId;
	}

	public String getTrackingId(){
		return trackingId;
	}
}
