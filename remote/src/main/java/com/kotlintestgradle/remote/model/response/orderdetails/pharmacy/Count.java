package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class Count implements Parcelable, ValidItem {

	@Expose
	@SerializedName("unavailable")
	private int unavailable;

	@Expose
	@SerializedName("picked")
	private int picked;

	@Expose
	@SerializedName("substitutes")
	private int substitutes;

	@Expose
	@SerializedName("review")
	private int review;

	@Expose
	@SerializedName("pending")
	private int pending;

	@Expose
	@SerializedName("packed")
	private int packed;


	protected Count(Parcel in) {
		unavailable = in.readInt();
		picked = in.readInt();
		substitutes = in.readInt();
		review = in.readInt();
		pending = in.readInt();
		packed = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(unavailable);
		dest.writeInt(picked);
		dest.writeInt(substitutes);
		dest.writeInt(review);
		dest.writeInt(pending);
		dest.writeInt(packed);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Count> CREATOR = new Creator<Count>() {
		@Override
		public Count createFromParcel(Parcel in) {
			return new Count(in);
		}

		@Override
		public Count[] newArray(int size) {
			return new Count[size];
		}
	};

	public int getReview() {
		return review;
	}

	public void setReview(int review) {
		this.review = review;
	}

	public void setUnavailable(int unavailable){
		this.unavailable = unavailable;
	}

	public int getUnavailable(){
		return unavailable;
	}

	public void setPicked(int picked){
		this.picked = picked;
	}

	public int getPicked(){
		return picked;
	}

	public void setSubstitutes(int substitutes){
		this.substitutes = substitutes;
	}

	public int getSubstitutes(){
		return substitutes;
	}

	public void setPending(int pending){
		this.pending = pending;
	}

	public int getPending(){
		return pending;
	}

	public void setPacked(int packed){
		this.packed = packed;
	}

	public int getPacked(){
		return packed;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}