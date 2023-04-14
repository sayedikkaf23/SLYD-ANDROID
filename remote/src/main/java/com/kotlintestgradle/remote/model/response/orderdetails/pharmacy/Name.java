package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name implements Parcelable, ValidItem {
	@Expose
	@SerializedName("en")
	private String en;

	protected Name(Parcel in) {
		en = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(en);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Name> CREATOR = new Creator<Name>() {
		@Override
		public Name createFromParcel(Parcel in) {
			return new Name(in);
		}

		@Override
		public Name[] newArray(int size) {
			return new Name[size];
		}
	};

	public void setEn(String en){
		this.en = en;
	}

	public String getEn(){
		return en;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
