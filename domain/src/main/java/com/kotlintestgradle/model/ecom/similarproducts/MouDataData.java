package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;

public class MouDataData implements Parcelable {
	private int mouQty;
	private String mou;
	private String minimumPurchaseUnit;
	private String mouUnit;

	public MouDataData(int mouQty, String mou, String minimumPurchaseUnit, String mouUnit) {
		this.mouQty = mouQty;
		this.mou = mou;
		this.minimumPurchaseUnit = minimumPurchaseUnit;
		this.mouUnit = mouUnit;
	}

	protected MouDataData(Parcel in) {
		mouQty = in.readInt();
		mou = in.readString();
		minimumPurchaseUnit = in.readString();
		mouUnit = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mouQty);
		dest.writeString(mou);
		dest.writeString(minimumPurchaseUnit);
		dest.writeString(mouUnit);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<MouDataData> CREATOR = new Creator<MouDataData>() {
		@Override
		public MouDataData createFromParcel(Parcel in) {
			return new MouDataData(in);
		}

		@Override
		public MouDataData[] newArray(int size) {
			return new MouDataData[size];
		}
	};

	public void setMouQty(int mouQty){
		this.mouQty = mouQty;
	}

	public int getMouQty(){
		return mouQty;
	}

	public void setMou(String mou){
		this.mou = mou;
	}

	public String getMou(){
		return mou;
	}

	public void setMinimumPurchaseUnit(String minimumPurchaseUnit){
		this.minimumPurchaseUnit = minimumPurchaseUnit;
	}

	public String getMinimumPurchaseUnit(){
		return minimumPurchaseUnit;
	}

	public void setMouUnit(String mouUnit){
		this.mouUnit = mouUnit;
	}

	public String getMouUnit(){
		return mouUnit;
	}
}
