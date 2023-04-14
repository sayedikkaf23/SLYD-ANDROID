package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersItem implements Parcelable, ValidItem {

	@Expose
	@SerializedName("storeOrderId")
	private String storeOrderId;

	@Expose
	@SerializedName("productOrderId")
	private List<String> productOrderId;

	protected OrdersItem(Parcel in) {
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

	public static final Creator<OrdersItem> CREATOR = new Creator<OrdersItem>() {
		@Override
		public OrdersItem createFromParcel(Parcel in) {
			return new OrdersItem(in);
		}

		@Override
		public OrdersItem[] newArray(int size) {
			return new OrdersItem[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}