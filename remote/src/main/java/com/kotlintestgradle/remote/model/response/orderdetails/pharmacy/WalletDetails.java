package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("charges")
	private List<Object> charges;

	protected WalletDetails(Parcel in) {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<WalletDetails> CREATOR = new Creator<WalletDetails>() {
		@Override
		public WalletDetails createFromParcel(Parcel in) {
			return new WalletDetails(in);
		}

		@Override
		public WalletDetails[] newArray(int size) {
			return new WalletDetails[size];
		}
	};

	public void setCharges(List<Object> charges){
		this.charges = charges;
	}

	public List<Object> getCharges(){
		return charges;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}