package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferDetailsData implements Parcelable {
	private String offerType;
	private String offerId;
	private String offerValue;
	private String offerTitle;

	public OfferDetailsData(String offerType, String offerId, String offerValue, String offerTitle) {
		this.offerType = offerType;
		this.offerId = offerId;
		this.offerValue = offerValue;
		this.offerTitle = offerTitle;
	}

	protected OfferDetailsData(Parcel in) {
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

	public static final Creator<OfferDetailsData> CREATOR = new Creator<OfferDetailsData>() {
		@Override
		public OfferDetailsData createFromParcel(Parcel in) {
			return new OfferDetailsData(in);
		}

		@Override
		public OfferDetailsData[] newArray(int size) {
			return new OfferDetailsData[size];
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
}
