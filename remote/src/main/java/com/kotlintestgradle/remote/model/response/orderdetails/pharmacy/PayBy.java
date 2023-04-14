package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayBy implements Parcelable, ValidItem {

	@Expose
	@SerializedName("wallet")
	private int wallet;

	@Expose
	@SerializedName("cash")
	private double cash;

	@Expose
	@SerializedName("card")
	private double card;

	protected PayBy(Parcel in) {
		wallet = in.readInt();
		cash = in.readDouble();
		card = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(wallet);
		dest.writeDouble(cash);
		dest.writeDouble(card);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PayBy> CREATOR = new Creator<PayBy>() {
		@Override
		public PayBy createFromParcel(Parcel in) {
			return new PayBy(in);
		}

		@Override
		public PayBy[] newArray(int size) {
			return new PayBy[size];
		}
	};

	public void setWallet(int wallet){
		this.wallet = wallet;
	}

	public int getWallet(){
		return wallet;
	}

	public void setCash(double cash){
		this.cash = cash;
	}

	public double getCash(){
		return cash;
	}

	public void setCard(double card){
		this.card = card;
	}

	public double getCard(){
		return card;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}