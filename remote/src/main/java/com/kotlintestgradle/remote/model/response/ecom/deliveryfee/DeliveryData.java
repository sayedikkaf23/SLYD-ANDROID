package com.kotlintestgradle.remote.model.response.ecom.deliveryfee;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class DeliveryData implements Parcelable, ValidItem {

	@Expose
	@SerializedName("deliveryByDeliveryPartner")
	private boolean deliveryByDeliveryPartner;

	@Expose
	@SerializedName("deliveryFee")
	private int deliveryFee;

	@Expose
	@SerializedName("deliveryByFleetDriver")
	private boolean deliveryByFleetDriver;

	protected DeliveryData(Parcel in) {
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

	public static final Creator<DeliveryData> CREATOR = new Creator<DeliveryData>() {
		@Override
		public DeliveryData createFromParcel(Parcel in) {
			return new DeliveryData(in);
		}

		@Override
		public DeliveryData[] newArray(int size) {
			return new DeliveryData[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}