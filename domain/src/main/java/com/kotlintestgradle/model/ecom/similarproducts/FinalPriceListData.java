package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;

public class FinalPriceListData implements Parcelable {
	private int discountPercentage;
	private int discountPrice;
	private int finalPrice;
	private int basePrice;

	public FinalPriceListData(int discountPercentage, int discountPrice, int finalPrice, int basePrice) {
		this.discountPercentage = discountPercentage;
		this.discountPrice = discountPrice;
		this.finalPrice = finalPrice;
		this.basePrice = basePrice;
	}

	protected FinalPriceListData(Parcel in) {
		discountPercentage = in.readInt();
		discountPrice = in.readInt();
		finalPrice = in.readInt();
		basePrice = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(discountPercentage);
		dest.writeInt(discountPrice);
		dest.writeInt(finalPrice);
		dest.writeInt(basePrice);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<FinalPriceListData> CREATOR = new Creator<FinalPriceListData>() {
		@Override
		public FinalPriceListData createFromParcel(Parcel in) {
			return new FinalPriceListData(in);
		}

		@Override
		public FinalPriceListData[] newArray(int size) {
			return new FinalPriceListData[size];
		}
	};

	public void setDiscountPercentage(int discountPercentage){
		this.discountPercentage = discountPercentage;
	}

	public int getDiscountPercentage(){
		return discountPercentage;
	}

	public void setDiscountPrice(int discountPrice){
		this.discountPrice = discountPrice;
	}

	public int getDiscountPrice(){
		return discountPrice;
	}

	public void setFinalPrice(int finalPrice){
		this.finalPrice = finalPrice;
	}

	public int getFinalPrice(){
		return finalPrice;
	}

	public void setBasePrice(int basePrice){
		this.basePrice = basePrice;
	}

	public int getBasePrice(){
		return basePrice;
	}
}
