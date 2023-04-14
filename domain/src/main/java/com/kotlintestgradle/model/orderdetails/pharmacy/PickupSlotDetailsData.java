package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class PickupSlotDetailsData implements Parcelable {
	private String date;
	private String shiftName;
	private int startDateTime;
	private String startTime;
	private String endTime;
	private int endDateTime;

	public PickupSlotDetailsData() {
	}

	public PickupSlotDetailsData(String date, String shiftName, int startDateTime, String startTime, String endTime, int endDateTime) {
		this.date = date;
		this.shiftName = shiftName;
		this.startDateTime = startDateTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.endDateTime = endDateTime;
	}

	protected PickupSlotDetailsData(Parcel in) {
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

	public static final Creator<PickupSlotDetailsData> CREATOR = new Creator<PickupSlotDetailsData>() {
		@Override
		public PickupSlotDetailsData createFromParcel(Parcel in) {
			return new PickupSlotDetailsData(in);
		}

		@Override
		public PickupSlotDetailsData[] newArray(int size) {
			return new PickupSlotDetailsData[size];
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
}
