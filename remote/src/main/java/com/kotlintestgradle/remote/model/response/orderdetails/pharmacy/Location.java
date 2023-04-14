package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location implements Parcelable, ValidItem {

	@Expose
	@SerializedName("lon")
	private int lon;

	@Expose
	@SerializedName("lat")
	private int lat;

	protected Location(Parcel in) {
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

	public static final Creator<Location> CREATOR = new Creator<Location>() {
		@Override
		public Location createFromParcel(Parcel in) {
			return new Location(in);
		}

		@Override
		public Location[] newArray(int size) {
			return new Location[size];
		}
	};

	public int getLon(){
		return lon;
	}

	public int getLat(){
		return lat;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
