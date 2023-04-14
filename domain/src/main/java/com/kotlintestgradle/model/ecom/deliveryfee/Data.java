package com.kotlintestgradle.model.ecom.deliveryfee;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
	private boolean deliveryByDeliveryPartner;
	private int deliveryFee;
	private boolean deliveryByFleetDriver;

	public Data(boolean deliveryByDeliveryPartner, int deliveryFee, boolean deliveryByFleetDriver) {
		this.deliveryByDeliveryPartner = deliveryByDeliveryPartner;
		this.deliveryFee = deliveryFee;
		this.deliveryByFleetDriver = deliveryByFleetDriver;
	}

	protected Data(Parcel in) {
		deliveryByDeliveryPartner = in.readByte() != 0;
		deliveryFee = in.readInt();
		deliveryByFleetDriver = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (deliveryByDeliveryPartner ? 1 : 0));
		dest.writeInt(deliveryFee);
		dest.writeByte((byte) (deliveryByFleetDriver ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Data> CREATOR = new Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};

	public void setDeliveryByDeliveryPartner(boolean deliveryByDeliveryPartner){
		this.deliveryByDeliveryPartner = deliveryByDeliveryPartner;
	}

	public boolean isDeliveryByDeliveryPartner(){
		return deliveryByDeliveryPartner;
	}

	public void setDeliveryFee(int deliveryFee){
		this.deliveryFee = deliveryFee;
	}

	public int getDeliveryFee(){
		return deliveryFee;
	}

	public void setDeliveryByFleetDriver(boolean deliveryByFleetDriver){
		this.deliveryByFleetDriver = deliveryByFleetDriver;
	}

	public boolean isDeliveryByFleetDriver(){
		return deliveryByFleetDriver;
	}
}
