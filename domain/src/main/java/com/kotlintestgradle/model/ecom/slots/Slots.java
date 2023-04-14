package com.kotlintestgradle.model.ecom.slots;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Slots implements Parcelable {

	private List<Data> data;

	private String message;

	public Slots(ArrayList<Data> data, String message) {
		this.data = data;
		this.message = message;
	}

	protected Slots(Parcel in) {
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

	public static final Creator<Slots> CREATOR = new Creator<Slots>() {
		@Override
		public Slots createFromParcel(Parcel in) {
			return new Slots(in);
		}

		@Override
		public Slots[] newArray(int size) {
			return new Slots[size];
		}
	};

	public void setData(List<Data> data){
		this.data = data;
	}

	public List<Data> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}