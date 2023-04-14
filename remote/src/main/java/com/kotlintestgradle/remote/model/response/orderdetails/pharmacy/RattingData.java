package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RattingData implements Parcelable, ValidItem {

	@Expose
	@SerializedName("reviewDescription")
	private String reviewDescription;

	@Expose
	@SerializedName("isRated")
	private boolean isRated;

	@Expose
	@SerializedName("rating")
	private int rating;

	@Expose
	@SerializedName("reviewTitle")
	private String reviewTitle;

	protected RattingData(Parcel in) {
		reviewDescription = in.readString();
		isRated = in.readByte() != 0;
		rating = in.readInt();
		reviewTitle = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(reviewDescription);
		dest.writeByte((byte) (isRated ? 1 : 0));
		dest.writeInt(rating);
		dest.writeString(reviewTitle);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<RattingData> CREATOR = new Creator<RattingData>() {
		@Override
		public RattingData createFromParcel(Parcel in) {
			return new RattingData(in);
		}

		@Override
		public RattingData[] newArray(int size) {
			return new RattingData[size];
		}
	};

	public void setReviewDescription(String reviewDescription){
		this.reviewDescription = reviewDescription;
	}

	public String getReviewDescription(){
		return reviewDescription;
	}

	public void setIsRated(boolean isRated){
		this.isRated = isRated;
	}

	public boolean isIsRated(){
		return isRated;
	}

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	public void setReviewTitle(String reviewTitle){
		this.reviewTitle = reviewTitle;
	}

	public String getReviewTitle(){
		return reviewTitle;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}