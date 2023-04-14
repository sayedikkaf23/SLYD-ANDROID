package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class CountsData implements Parcelable {
	private int unavailable;
	private int picked;
	private int substitutes;
	private int pending;
	private int packed;

	public CountsData(int unavailable, int picked, int substitutes, int pending, int packed) {
		this.unavailable = unavailable;
		this.picked = picked;
		this.substitutes = substitutes;
		this.pending = pending;
		this.packed = packed;
	}

	protected CountsData(Parcel in) {
		unavailable = in.readInt();
		picked = in.readInt();
		substitutes = in.readInt();
		pending = in.readInt();
		packed = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(unavailable);
		dest.writeInt(picked);
		dest.writeInt(substitutes);
		dest.writeInt(pending);
		dest.writeInt(packed);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<CountsData> CREATOR = new Creator<CountsData>() {
		@Override
		public CountsData createFromParcel(Parcel in) {
			return new CountsData(in);
		}

		@Override
		public CountsData[] newArray(int size) {
			return new CountsData[size];
		}
	};

	public void setUnavailable(int unavailable){
		this.unavailable = unavailable;
	}

	public int getUnavailable(){
		return unavailable;
	}

	public void setPicked(int picked){
		this.picked = picked;
	}

	public int getPicked(){
		return picked;
	}

	public void setSubstitutes(int substitutes){
		this.substitutes = substitutes;
	}

	public int getSubstitutes(){
		return substitutes;
	}

	public void setPending(int pending){
		this.pending = pending;
	}

	public int getPending(){
		return pending;
	}

	public void setPacked(int packed){
		this.packed = packed;
	}

	public int getPacked(){
		return packed;
	}
}
