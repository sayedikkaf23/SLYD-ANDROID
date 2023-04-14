package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("name")
	private String name;

	@Expose
	@SerializedName("id")
	private String id;

	@Expose
	@SerializedName("trackingId")
	private String trackingId;

	protected ShippingDetails(Parcel in) {
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

	public static final Creator<ShippingDetails> CREATOR = new Creator<ShippingDetails>() {
		@Override
		public ShippingDetails createFromParcel(Parcel in) {
			return new ShippingDetails(in);
		}

		@Override
		public ShippingDetails[] newArray(int size) {
			return new ShippingDetails[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}