package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class PartnerDetailsData implements Parcelable {
	private String name;
	private String id;
	private String trackingId;

	public PartnerDetailsData(String name, String id, String trackingId) {
		this.name = name;
		this.id = id;
		this.trackingId = trackingId;
	}

	protected PartnerDetailsData(Parcel in) {
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

	public static final Creator<PartnerDetailsData> CREATOR = new Creator<PartnerDetailsData>() {
		@Override
		public PartnerDetailsData createFromParcel(Parcel in) {
			return new PartnerDetailsData(in);
		}

		@Override
		public PartnerDetailsData[] newArray(int size) {
			return new PartnerDetailsData[size];
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
