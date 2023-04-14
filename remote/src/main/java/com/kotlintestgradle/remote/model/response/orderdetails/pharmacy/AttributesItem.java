package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class AttributesItem implements Parcelable, ValidItem {

	@SerializedName("isAddOn")
	@Expose
	private boolean isAddOn;

	@SerializedName("attrname")
	@Expose
	private String attrname;

	@SerializedName("value")
	@Expose
	private String value;

	protected AttributesItem(Parcel in) {
		isAddOn = in.readByte() != 0;
		attrname = in.readString();
		value = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (isAddOn ? 1 : 0));
		dest.writeString(attrname);
		dest.writeString(value);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<AttributesItem> CREATOR = new Creator<AttributesItem>() {
		@Override
		public AttributesItem createFromParcel(Parcel in) {
			return new AttributesItem(in);
		}

		@Override
		public AttributesItem[] newArray(int size) {
			return new AttributesItem[size];
		}
	};

	public void setIsAddOn(boolean isAddOn){
		this.isAddOn = isAddOn;
	}

	public boolean isIsAddOn(){
		return isAddOn;
	}

	public void setAttrname(String attrname){
		this.attrname = attrname;
	}

	public String getAttrname(){
		return attrname;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}