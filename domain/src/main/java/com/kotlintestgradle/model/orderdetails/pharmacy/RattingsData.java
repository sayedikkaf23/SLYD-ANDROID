package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class RattingsData implements Parcelable {
	private String reviewDescription;
	private boolean isRated;
	private int rating;
	private String reviewTitle;

	public RattingsData(String reviewDescription, boolean isRated, int rating, String reviewTitle) {
		this.reviewDescription = reviewDescription;
		this.isRated = isRated;
		this.rating = rating;
		this.reviewTitle = reviewTitle;
	}

	protected RattingsData(Parcel in) {
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

	public static final Creator<RattingsData> CREATOR = new Creator<RattingsData>() {
		@Override
		public RattingsData createFromParcel(Parcel in) {
			return new RattingsData(in);
		}

		@Override
		public RattingsData[] newArray(int size) {
			return new RattingsData[size];
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
}
