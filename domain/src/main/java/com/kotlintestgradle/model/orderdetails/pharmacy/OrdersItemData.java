package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrdersItemData implements Parcelable {
	private String storeOrderId;
	private List<String> productOrderId;

	public OrdersItemData(String storeOrderId, List<String> productOrderId) {
		this.storeOrderId = storeOrderId;
		this.productOrderId = productOrderId;
	}

	protected OrdersItemData(Parcel in) {
		storeOrderId = in.readString();
		productOrderId = in.createStringArrayList();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(storeOrderId);
		dest.writeStringList(productOrderId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<OrdersItemData> CREATOR = new Creator<OrdersItemData>() {
		@Override
		public OrdersItemData createFromParcel(Parcel in) {
			return new OrdersItemData(in);
		}

		@Override
		public OrdersItemData[] newArray(int size) {
			return new OrdersItemData[size];
		}
	};

	public void setStoreOrderId(String storeOrderId){
		this.storeOrderId = storeOrderId;
	}

	public String getStoreOrderId(){
		return storeOrderId;
	}

	public void setProductOrderId(List<String> productOrderId){
		this.productOrderId = productOrderId;
	}

	public List<String> getProductOrderId(){
		return productOrderId;
	}
}