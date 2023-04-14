package com.kotlintestgradle.remote.model.response.ecom.pdp.substitute;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.List;

public class SubstitutesData implements ValidItem, Parcelable {

	@Expose
	@SerializedName("data")
	private List<DataItems> data;

	@Expose
	@SerializedName("totalCount")
	private int totalCount;

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

	@Override
	public boolean isValid() {
		return true;
	}
}