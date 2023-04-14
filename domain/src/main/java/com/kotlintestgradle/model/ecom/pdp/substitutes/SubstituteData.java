package com.kotlintestgradle.model.ecom.pdp.substitutes;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class SubstituteData implements Parcelable {
	private List<DataItem> data;
	private int totalCount;

	public SubstituteData(List<DataItem> data, int totalCount) {
		this.data = data;
		this.totalCount = totalCount;
	}

	protected SubstituteData(Parcel in) {
		totalCount = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(totalCount);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SubstituteData> CREATOR = new Creator<SubstituteData>() {
		@Override
		public SubstituteData createFromParcel(Parcel in) {
			return new SubstituteData(in);
		}

		@Override
		public SubstituteData[] newArray(int size) {
			return new SubstituteData[size];
		}
	};

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}

	public int getTotalCount(){
		return totalCount;
	}
}