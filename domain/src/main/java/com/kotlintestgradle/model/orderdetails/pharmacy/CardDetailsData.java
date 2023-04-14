package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CardDetailsData implements Parcelable {
	private String last4;
	private ArrayList<ChargesItems> charges;
	private String chargeId;
	private String cardId;

	public CardDetailsData(String last4, ArrayList<ChargesItems> charges, String chargeId, String cardId) {
		this.last4 = last4;
		this.charges = charges;
		this.chargeId = chargeId;
		this.cardId = cardId;
	}

	protected CardDetailsData(Parcel in) {
		last4 = in.readString();
		chargeId = in.readString();
		cardId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(last4);
		dest.writeString(chargeId);
		dest.writeString(cardId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<CardDetailsData> CREATOR = new Creator<CardDetailsData>() {
		@Override
		public CardDetailsData createFromParcel(Parcel in) {
			return new CardDetailsData(in);
		}

		@Override
		public CardDetailsData[] newArray(int size) {
			return new CardDetailsData[size];
		}
	};

	public void setLast4(String last4){
		this.last4 = last4;
	}

	public String getLast4(){
		return last4;
	}

	public void setCharges(ArrayList<ChargesItems> charges){
		this.charges = charges;
	}

	public ArrayList<ChargesItems> getCharges(){
		return charges;
	}

	public void setChargeId(String chargeId){
		this.chargeId = chargeId;
	}

	public String getChargeId(){
		return chargeId;
	}

	public void setCardId(String cardId){
		this.cardId = cardId;
	}

	public String getCardId(){
		return cardId;
	}
}