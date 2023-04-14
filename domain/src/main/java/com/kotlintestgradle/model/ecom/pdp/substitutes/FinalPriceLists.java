package com.kotlintestgradle.model.ecom.pdp.substitutes;

import android.os.Parcel;
import android.os.Parcelable;

public class FinalPriceLists implements Parcelable {
	private int discountPercentage;
	private double discountPrice;
	private double finalPrice;
	private int basePrice;

	public FinalPriceLists(int discountPercentage, double discountPrice, double finalPrice, int basePrice) {
		this.discountPercentage = discountPercentage;
		this.discountPrice = discountPrice;
		this.finalPrice = finalPrice;
		this.basePrice = basePrice;
	}

	protected FinalPriceLists(Parcel in) {
		discountPercentage = in.readInt();
		discountPrice = in.readDouble();
		finalPrice = in.readDouble();
		basePrice = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(discountPercentage);
		dest.writeDouble(discountPrice);
		dest.writeDouble(finalPrice);
		dest.writeInt(basePrice);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<FinalPriceLists> CREATOR = new Creator<FinalPriceLists>() {
		@Override
		public FinalPriceLists createFromParcel(Parcel in) {
			return new FinalPriceLists(in);
		}

		@Override
		public FinalPriceLists[] newArray(int size) {
			return new FinalPriceLists[size];
		}
	};

	public void setDiscountPercentage(int discountPercentage){
		this.discountPercentage = discountPercentage;
	}

	public int getDiscountPercentage(){
		return discountPercentage;
	}

	public void setDiscountPrice(double discountPrice){
		this.discountPrice = discountPrice;
	}

	public double getDiscountPrice(){
		return discountPrice;
	}

	public void setFinalPrice(double finalPrice){
		this.finalPrice = finalPrice;
	}

	public double getFinalPrice(){
		return finalPrice;
	}

	public void setBasePrice(int basePrice){
		this.basePrice = basePrice;
	}

	public int getBasePrice(){
		return basePrice;
	}
}
