package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class MouData implements Parcelable, ValidItem {

	@Expose
	@SerializedName("mouQty")
	private int mouQty;

	@Expose
	@SerializedName("mou")
	private String mou;

	@Expose
	@SerializedName("minimumPurchaseUnit")
	private String minimumPurchaseUnit;

	@Expose
	@SerializedName("mouUnit")
	private String mouUnit;

	protected MouData(Parcel in) {
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

	public static final Creator<MouData> CREATOR = new Creator<MouData>() {
		@Override
		public MouData createFromParcel(Parcel in) {
			return new MouData(in);
		}

		@Override
		public MouData[] newArray(int size) {
			return new MouData[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}