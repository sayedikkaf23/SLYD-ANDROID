package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PickupSlotDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("date")
	private String date;

	@Expose
	@SerializedName("shiftName")
	private String shiftName;

	@Expose
	@SerializedName("startDateTime")
	private int startDateTime;

	@Expose
	@SerializedName("startTime")
	private String startTime;

	@Expose
	@SerializedName("endTime")
	private String endTime;

	@Expose
	@SerializedName("endDateTime")
	private int endDateTime;

	protected PickupSlotDetails(Parcel in) {
		date = in.readString();
		shiftName = in.readString();
		startDateTime = in.readInt();
		startTime = in.readString();
		endTime = in.readString();
		endDateTime = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeString(shiftName);
		dest.writeInt(startDateTime);
		dest.writeString(startTime);
		dest.writeString(endTime);
		dest.writeInt(endDateTime);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PickupSlotDetails> CREATOR = new Creator<PickupSlotDetails>() {
		@Override
		public PickupSlotDetails createFromParcel(Parcel in) {
			return new PickupSlotDetails(in);
		}

		@Override
		public PickupSlotDetails[] newArray(int size) {
			return new PickupSlotDetails[size];
		}
	};

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getShiftName() {
		return shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	public int getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(int startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(int endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}