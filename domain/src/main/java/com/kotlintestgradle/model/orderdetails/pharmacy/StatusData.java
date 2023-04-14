package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusData implements Parcelable {
	private String statusText;
	private int updatedOnTimeStamp;
	private String updatedOn;
	private int status;
	private boolean customerConfirm;
	private boolean rejectedForPrescription;
	private int expiryTimeForConfirmation;

	public StatusData(String statusText, int updatedOnTimeStamp, String updatedOn, int status,
					  int expiryTimeForConfirmation, boolean customerConfirm,boolean rejectedForPrescription) {
		this.statusText = statusText;
		this.updatedOnTimeStamp = updatedOnTimeStamp;
		this.updatedOn = updatedOn;
		this.status = status;
		this.customerConfirm = customerConfirm;
		this.rejectedForPrescription = rejectedForPrescription;
		this.expiryTimeForConfirmation = expiryTimeForConfirmation;
	}

	protected StatusData(Parcel in) {
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

	public static final Creator<StatusData> CREATOR = new Creator<StatusData>() {
		@Override
		public StatusData createFromParcel(Parcel in) {
			return new StatusData(in);
		}

		@Override
		public StatusData[] newArray(int size) {
			return new StatusData[size];
		}
	};

	public boolean isRejectedForPrescription() {
		return rejectedForPrescription;
	}

	public void setRejectedForPrescription(boolean rejectedForPrescription) {
		this.rejectedForPrescription = rejectedForPrescription;
	}

	public void setStatusText(String statusText){
		this.statusText = statusText;
	}

	public String getStatusText(){
		return statusText;
	}

	public void setUpdatedOnTimeStamp(int updatedOnTimeStamp){
		this.updatedOnTimeStamp = updatedOnTimeStamp;
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
}
