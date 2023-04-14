package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status implements Parcelable, ValidItem {

	@Expose
	@SerializedName("statusText")
	private String statusText;

	@Expose
	@SerializedName("updatedOnTimeStamp")
	private int updatedOnTimeStamp;

	@Expose
	@SerializedName("updatedOn")
	private String updatedOn;

	@Expose
	@SerializedName("status")
	private int status;

	@Expose
	@SerializedName("expiryTimeForConfirmation")
	private int expiryTimeForConfirmation;

	@Expose
	@SerializedName("customerConfirm")
	private boolean customerConfirm;

	@Expose
	@SerializedName("rejectedForPrescription")
	private boolean rejectedForPrescription;

	protected Status(Parcel in) {
		statusText = in.readString();
		updatedOnTimeStamp = in.readInt();
		updatedOn = in.readString();
		status = in.readInt();
		expiryTimeForConfirmation = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(statusText);
		dest.writeInt(updatedOnTimeStamp);
		dest.writeString(updatedOn);
		dest.writeInt(status);
		dest.writeInt(expiryTimeForConfirmation);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Status> CREATOR = new Creator<Status>() {
		@Override
		public Status createFromParcel(Parcel in) {
			return new Status(in);
		}

		@Override
		public Status[] newArray(int size) {
			return new Status[size];
		}
	};

	public void setStatusText(String statusText){
		this.statusText = statusText;
	}

	public String getStatusText(){
		return statusText;
	}

	public void setUpdatedOnTimeStamp(int updatedOnTimeStamp){
		this.updatedOnTimeStamp = updatedOnTimeStamp;
	}

	public boolean isRejectedForPrescription() {
		return rejectedForPrescription;
	}

	public void setRejectedForPrescription(boolean rejectedForPrescription) {
		this.rejectedForPrescription = rejectedForPrescription;
	}

	public int getUpdatedOnTimeStamp(){
		return updatedOnTimeStamp;
	}

	public void setUpdatedOn(String updatedOn){
		this.updatedOn = updatedOn;
	}

	public String getUpdatedOn(){
		return updatedOn;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public boolean isCustomerConfirm() {
		return customerConfirm;
	}

	public void setCustomerConfirm(boolean customerConfirm) {
		this.customerConfirm = customerConfirm;
	}

	public int getExpiryTimeForConfirmation() {
		return expiryTimeForConfirmation;
	}

	public void setExpiryTimeForConfirmation(int expiryTimeForConfirmation) {
		this.expiryTimeForConfirmation = expiryTimeForConfirmation;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}