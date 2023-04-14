package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class PayByData implements Parcelable {
	private int wallet;
	private double cash;
	private double card;

	public PayByData(int wallet, double cash, double card) {
		this.wallet = wallet;
		this.cash = cash;
		this.card = card;
	}

	protected PayByData(Parcel in) {
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

	public static final Creator<PayByData> CREATOR = new Creator<PayByData>() {
		@Override
		public PayByData createFromParcel(Parcel in) {
			return new PayByData(in);
		}

		@Override
		public PayByData[] newArray(int size) {
			return new PayByData[size];
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
}
