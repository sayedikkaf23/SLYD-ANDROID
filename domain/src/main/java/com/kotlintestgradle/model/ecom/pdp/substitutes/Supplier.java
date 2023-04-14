package com.kotlintestgradle.model.ecom.pdp.substitutes;

import android.os.Parcel;
import android.os.Parcelable;

public class Supplier implements Parcelable {
	private String productId;
	private int retailerQty;
	private int retailerPrice;
	private String currencySymbol;
	private int distributorPrice;
	private String currency;
	private String id;
	private int distributorQty;


	public Supplier(String productId, int retailerQty, int retailerPrice, String currencySymbol,
					int distributorPrice, String currency, String id, int distributorQty) {
		this.productId = productId;
		this.retailerQty = retailerQty;
		this.retailerPrice = retailerPrice;
		this.currencySymbol = currencySymbol;
		this.distributorPrice = distributorPrice;
		this.currency = currency;
		this.id = id;
		this.distributorQty = distributorQty;
	}

	protected Supplier(Parcel in) {
		productId = in.readString();
		retailerQty = in.readInt();
		retailerPrice = in.readInt();
		currencySymbol = in.readString();
		distributorPrice = in.readInt();
		currency = in.readString();
		id = in.readString();
		distributorQty = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(productId);
		dest.writeInt(retailerQty);
		dest.writeInt(retailerPrice);
		dest.writeString(currencySymbol);
		dest.writeInt(distributorPrice);
		dest.writeString(currency);
		dest.writeString(id);
		dest.writeInt(distributorQty);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Supplier> CREATOR = new Creator<Supplier>() {
		@Override
		public Supplier createFromParcel(Parcel in) {
			return new Supplier(in);
		}

		@Override
		public Supplier[] newArray(int size) {
			return new Supplier[size];
		}
	};

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setRetailerQty(int retailerQty){
		this.retailerQty = retailerQty;
	}

	public int getRetailerQty(){
		return retailerQty;
	}

	public void setRetailerPrice(int retailerPrice){
		this.retailerPrice = retailerPrice;
	}

	public int getRetailerPrice(){
		return retailerPrice;
	}

	public void setCurrencySymbol(String currencySymbol){
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public void setDistributorPrice(int distributorPrice){
		this.distributorPrice = distributorPrice;
	}

	public int getDistributorPrice(){
		return distributorPrice;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setDistributorQty(int distributorQty){
		this.distributorQty = distributorQty;
	}

	public int getDistributorQty(){
		return distributorQty;
	}
}
