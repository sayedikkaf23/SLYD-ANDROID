package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class DeliveryDetails implements Parcelable, ValidItem {

	@Expose
	@SerializedName("deliveryFee")
	private int deliveryFee;

	@Expose
	@SerializedName("deliveryByDeliveryPartner")
	private boolean deliveryByDeliveryPartner;

	@Expose
	@SerializedName("deliveryByFleetDriver")
	private boolean deliveryByFleetDriver;

	@Expose
	@SerializedName("time")
	private String time;

	protected DeliveryDetails(Parcel in) {
		deliveryFee = in.readInt();
		deliveryByDeliveryPartner = in.readByte() != 0;
		deliveryByFleetDriver = in.readByte() != 0;
		time = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(deliveryFee);
		dest.writeByte((byte) (deliveryByDeliveryPartner ? 1 : 0));
		dest.writeByte((byte) (deliveryByFleetDriver ? 1 : 0));
		dest.writeString(time);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DeliveryDetails> CREATOR = new Creator<DeliveryDetails>() {
		@Override
		public DeliveryDetails createFromParcel(Parcel in) {
			return new DeliveryDetails(in);
		}

		@Override
		public DeliveryDetails[] newArray(int size) {
			return new DeliveryDetails[size];
		}
	};

	public void setDeliveryFee(int deliveryFee){
		this.deliveryFee = deliveryFee;
	}

	public int getDeliveryFee(){
		return deliveryFee;
	}

	public void setDeliveryByDeliveryPartner(boolean deliveryByDeliveryPartner){
		this.deliveryByDeliveryPartner = deliveryByDeliveryPartner;
	}

	public boolean isDeliveryByDeliveryPartner(){
		return deliveryByDeliveryPartner;
	}

	public void setDeliveryByFleetDriver(boolean deliveryByFleetDriver){
		this.deliveryByFleetDriver = deliveryByFleetDriver;
	}

	public boolean isDeliveryByFleetDriver(){
		return deliveryByFleetDriver;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}