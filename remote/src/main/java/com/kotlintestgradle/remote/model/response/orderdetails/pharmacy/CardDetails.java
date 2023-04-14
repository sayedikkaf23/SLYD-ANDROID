package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

import java.util.List;

public class CardDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("last4")
	private String last4;

	@Expose
	@SerializedName("charges")
	private List<ChargesItem> charges;

	@Expose
	@SerializedName("chargeId")
	private String chargeId;

	@Expose
	@SerializedName("cardId")
	private String cardId;

	protected CardDetails(Parcel in) {
		last4 = in.readString();
		charges = in.createTypedArrayList(ChargesItem.CREATOR);
		chargeId = in.readString();
		cardId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(last4);
		dest.writeTypedList(charges);
		dest.writeString(chargeId);
		dest.writeString(cardId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public List<ChargesItem> getCharges() {
		return charges;
	}

	public void setCharges(List<ChargesItem> charges) {
		this.charges = charges;
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public static final Creator<CardDetails> CREATOR = new Creator<CardDetails>() {
		@Override
		public CardDetails createFromParcel(Parcel in) {
			return new CardDetails(in);
		}

		@Override
		public CardDetails[] newArray(int size) {
			return new CardDetails[size];
		}
	};

	@Override
	public boolean isValid() {
		return true;
	}
}