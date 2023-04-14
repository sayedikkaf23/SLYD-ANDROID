package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class AttributesItemData implements Parcelable {
	private boolean isAddOn;
	private String attrname;
	private String value;

	public AttributesItemData(boolean isAddOn, String attrname, String value) {
		this.isAddOn = isAddOn;
		this.attrname = attrname;
		this.value = value;
	}

	protected AttributesItemData(Parcel in) {
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

	public static final Creator<AttributesItemData> CREATOR = new Creator<AttributesItemData>() {
		@Override
		public AttributesItemData createFromParcel(Parcel in) {
			return new AttributesItemData(in);
		}

		@Override
		public AttributesItemData[] newArray(int size) {
			return new AttributesItemData[size];
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
}
