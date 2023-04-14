package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryDetailsData implements Parcelable {
	private int deliveryFee;
	private boolean deliveryByDeliveryPartner;
	private boolean deliveryByFleetDriver;
	private String time;

	public DeliveryDetailsData() {
	}

	public DeliveryDetailsData(int deliveryFee, boolean deliveryByDeliveryPartner, boolean deliveryByFleetDriver, String time) {
		this.deliveryFee = deliveryFee;
		this.deliveryByDeliveryPartner = deliveryByDeliveryPartner;
		this.deliveryByFleetDriver = deliveryByFleetDriver;
		this.time = time;
	}

	protected DeliveryDetailsData(Parcel in) {
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

	public static final Creator<DeliveryDetailsData> CREATOR = new Creator<DeliveryDetailsData>() {
		@Override
		public DeliveryDetailsData createFromParcel(Parcel in) {
			return new DeliveryDetailsData(in);
		}

		@Override
		public DeliveryDetailsData[] newArray(int size) {
			return new DeliveryDetailsData[size];
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
}
