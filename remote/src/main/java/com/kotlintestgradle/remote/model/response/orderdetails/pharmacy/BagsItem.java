package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class BagsItem implements Parcelable, ValidItem {

	@Expose
	@SerializedName("bagId")
	private String bagId;

	@Expose
	@SerializedName("lable")
	private String lable;

	protected BagsItem(Parcel in) {
		bagId = in.readString();
		lable = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(bagId);
		dest.writeString(lable);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<BagsItem> CREATOR = new Creator<BagsItem>() {
		@Override
		public BagsItem createFromParcel(Parcel in) {
			return new BagsItem(in);
		}

		@Override
		public BagsItem[] newArray(int size) {
			return new BagsItem[size];
		}
	};

	public String getBagId(){
		return bagId;
	}

	public String getLable(){
		return lable;
	}

	public void setBagId(String bagId) {
		this.bagId = bagId;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}
