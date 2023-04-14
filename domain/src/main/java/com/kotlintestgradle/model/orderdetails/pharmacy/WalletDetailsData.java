package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class WalletDetailsData implements Parcelable {
	private List<Object> charges;

	public WalletDetailsData(List<Object> charges) {
		this.charges = charges;
	}

	protected WalletDetailsData(Parcel in) {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<WalletDetailsData> CREATOR = new Creator<WalletDetailsData>() {
		@Override
		public WalletDetailsData createFromParcel(Parcel in) {
			return new WalletDetailsData(in);
		}

		@Override
		public WalletDetailsData[] newArray(int size) {
			return new WalletDetailsData[size];
		}
	};

	public void setCharges(List<Object> charges){
		this.charges = charges;
	}

	public List<Object> getCharges(){
		return charges;
	}
}