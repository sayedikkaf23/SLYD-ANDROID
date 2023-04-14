package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationData implements Parcelable {
	private int lon;
	private int lat;

	public LocationData(int lon, int lat) {
		this.lon = lon;
		this.lat = lat;
	}

	protected LocationData(Parcel in) {
		lon = in.readInt();
		lat = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(lon);
		dest.writeInt(lat);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
		@Override
		public LocationData createFromParcel(Parcel in) {
			return new LocationData(in);
		}

		@Override
		public LocationData[] newArray(int size) {
			return new LocationData[size];
		}
	};

	public void setLon(int lon){
		this.lon = lon;
	}

	public int getLon(){
		return lon;
	}

	public void setLat(int lat){
		this.lat = lat;
	}

	public int getLat(){
		return lat;
	}
}
