package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("offerType")
	private String offerType;

	@Expose
	@SerializedName("offerId")
	private String offerId;

	@Expose
	@SerializedName("offerValue")
	private String offerValue;

	@Expose
	@SerializedName("offerTitle")
	private String offerTitle;

	protected OfferDetails(Parcel in) {
		offerType = in.readString();
		offerId = in.readString();
		offerValue = in.readString();
		offerTitle = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(offerType);
		dest.writeString(offerId);
		dest.writeString(offerValue);
		dest.writeString(offerTitle);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<OfferDetails> CREATOR = new Creator<OfferDetails>() {
		@Override
		public OfferDetails createFromParcel(Parcel in) {
			return new OfferDetails(in);
		}

		@Override
		public OfferDetails[] newArray(int size) {
			return new OfferDetails[size];
		}
	};

	public void setOfferType(String offerType){
		this.offerType = offerType;
	}

	public String getOfferType(){
		return offerType;
	}

	public void setOfferId(String offerId){
		this.offerId = offerId;
	}

	public String getOfferId(){
		return offerId;
	}

	public void setOfferValue(String offerValue){
		this.offerValue = offerValue;
	}

	public String getOfferValue(){
		return offerValue;
	}

	public void setOfferTitle(String offerTitle){
		this.offerTitle = offerTitle;
	}

	public String getOfferTitle(){
		return offerTitle;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}