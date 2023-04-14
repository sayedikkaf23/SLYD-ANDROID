package com.kotlintestgradle.model.ecom.pdp.substitutes;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class SubstitutesData implements Parcelable {
	private List<DataItems> data;
	private int totalCount;

	public SubstitutesData(List<DataItems> data, int totalCount) {
		this.data = data;
		this.totalCount = totalCount;
	}

	protected SubstitutesData(Parcel in) {
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

	public static final Creator<SubstitutesData> CREATOR = new Creator<SubstitutesData>() {
		@Override
		public SubstitutesData createFromParcel(Parcel in) {
			return new SubstitutesData(in);
		}

		@Override
		public SubstitutesData[] newArray(int size) {
			return new SubstitutesData[size];
		}
	};

	public void setData(List<DataItems> data){
		this.data = data;
	}

	public List<DataItems> getData(){
		return data;
	}

	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}

	public int getTotalCount(){
		return totalCount;
	}
}