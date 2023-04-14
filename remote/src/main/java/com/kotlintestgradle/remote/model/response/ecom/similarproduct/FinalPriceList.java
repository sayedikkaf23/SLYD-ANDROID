package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class FinalPriceList implements Parcelable, ValidItem {

	@Expose
	@SerializedName("discountPercentage")
	private int discountPercentage;

	@Expose
	@SerializedName("discountPrice")
	private int discountPrice;

	@Expose
	@SerializedName("finalPrice")
	private int finalPrice;

	@Expose
	@SerializedName("basePrice")
	private int basePrice;

	protected FinalPriceList(Parcel in) {
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

	public static final Creator<FinalPriceList> CREATOR = new Creator<FinalPriceList>() {
		@Override
		public FinalPriceList createFromParcel(Parcel in) {
			return new FinalPriceList(in);
		}

		@Override
		public FinalPriceList[] newArray(int size) {
			return new FinalPriceList[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}