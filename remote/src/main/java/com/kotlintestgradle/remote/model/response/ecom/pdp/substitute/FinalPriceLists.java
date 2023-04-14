package com.kotlintestgradle.remote.model.response.ecom.pdp.substitute;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class FinalPriceLists implements ValidItem, Parcelable {

	@Expose
	@SerializedName("discountPercentage")
	private int discountPercentage;

	@Expose
	@SerializedName("discountPrice")
	private double discountPrice;

	@Expose
	@SerializedName("finalPrice")
	private double finalPrice;

	@Expose
	@SerializedName("basePrice")
	private int basePrice;

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

	@Override
	public boolean isValid() {
		return true;
	}
}