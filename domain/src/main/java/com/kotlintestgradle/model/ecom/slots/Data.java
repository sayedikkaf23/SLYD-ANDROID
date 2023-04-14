package com.kotlintestgradle.model.ecom.slots;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
	private String date;
	private String endTimeISO;
	private String endTimeStr;
	private String name;
	private String startTimeStr;
	private int startTime;
	private String id;
	private int endTime;
	private String startTimeISO;

	public Data(String date, String endTimeISO, String endTimeStr, String name, String startTimeISO,
				String startTimeStr, int startTime, String id, int endTime) {
		this.date = date;
		this.endTimeISO = endTimeISO;
		this.endTimeStr = endTimeStr;
		this.name = name;
		this.startTimeStr = startTimeStr;
		this.startTime = startTime;
		this.id = id;
		this.endTime = endTime;
		this.startTimeISO = startTimeISO;
	}

	protected Data(Parcel in) {
		date = in.readString();
		endTimeISO = in.readString();
		endTimeStr = in.readString();
		name = in.readString();
		startTimeStr = in.readString();
		startTime = in.readInt();
		id = in.readString();
		endTime = in.readInt();
		startTimeISO = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeString(endTimeISO);
		dest.writeString(endTimeStr);
		dest.writeString(name);
		dest.writeString(startTimeStr);
		dest.writeInt(startTime);
		dest.writeString(id);
		dest.writeInt(endTime);
		dest.writeString(startTimeISO);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Data> CREATOR = new Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setEndTimeISO(String endTimeISO){
		this.endTimeISO = endTimeISO;
	}

	public String getEndTimeISO(){
		return endTimeISO;
	}

	public void setEndTimeStr(String endTimeStr){
		this.endTimeStr = endTimeStr;
	}

	public String getEndTimeStr(){
		return endTimeStr;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setStartTimeStr(String startTimeStr){
		this.startTimeStr = startTimeStr;
	}

	public String getStartTimeStr(){
		return startTimeStr;
	}

	public void setStartTime(int startTime){
		this.startTime = startTime;
	}

	public int getStartTime(){
		return startTime;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEndTime(int endTime){
		this.endTime = endTime;
	}

	public int getEndTime(){
		return endTime;
	}

	public void setStartTimeISO(String startTimeISO){
		this.startTimeISO = startTimeISO;
	}

	public String getStartTimeISO(){
		return startTimeISO;
	}
}
