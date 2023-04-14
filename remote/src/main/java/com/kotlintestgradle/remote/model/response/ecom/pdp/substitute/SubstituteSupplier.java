package com.kotlintestgradle.remote.model.response.ecom.pdp.substitute;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class SubstituteSupplier implements ValidItem, Parcelable {

	@Expose
	@SerializedName("productId")
	private String productId;

	@Expose
	@SerializedName("retailerQty")
	private int retailerQty;

	@Expose
	@SerializedName("retailerPrice")
	private int retailerPrice;

	@Expose
	@SerializedName("currencySymbol")
	private String currencySymbol;

	@Expose
	@SerializedName("distributorPrice")
	private int distributorPrice;

	@Expose
	@SerializedName("currency")
	private String currency;

	@Expose
	@SerializedName("id")
	private String id;

	@Expose
	@SerializedName("distributorQty")
	private int distributorQty;

	protected SubstituteSupplier(Parcel in) {
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

	public static final Creator<SubstituteSupplier> CREATOR = new Creator<SubstituteSupplier>() {
		@Override
		public SubstituteSupplier createFromParcel(Parcel in) {
			return new SubstituteSupplier(in);
		}

		@Override
		public SubstituteSupplier[] newArray(int size) {
			return new SubstituteSupplier[size];
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

	@Override
	public boolean isValid() {
		return true;
	}
}