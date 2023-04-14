package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;

public class SupplierData implements Parcelable {
	private int sellerTypeId;
	private String storeType;
	private int storeFrontTypeId;
	private String productId;
	private int retailerQty;
	private int rating;
	private String currencySymbol;
	private int storeTypeId;
	private int distributorPrice;
	private String storeFrontType;
	private int distributorQty;
	private String cityName;
	private String areaName;
	private int retailerPrice;
	private String currency;
	private String storeName;
	private String id;
	private String sellerType;

	public SupplierData(int sellerTypeId, String storeType, int storeFrontTypeId, String productId, int retailerQty, int rating, String currencySymbol, int storeTypeId, int distributorPrice, String storeFrontType, int distributorQty, String cityName, String areaName, int retailerPrice, String currency, String storeName, String id, String sellerType) {
		this.sellerTypeId = sellerTypeId;
		this.storeType = storeType;
		this.storeFrontTypeId = storeFrontTypeId;
		this.productId = productId;
		this.retailerQty = retailerQty;
		this.rating = rating;
		this.currencySymbol = currencySymbol;
		this.storeTypeId = storeTypeId;
		this.distributorPrice = distributorPrice;
		this.storeFrontType = storeFrontType;
		this.distributorQty = distributorQty;
		this.cityName = cityName;
		this.areaName = areaName;
		this.retailerPrice = retailerPrice;
		this.currency = currency;
		this.storeName = storeName;
		this.id = id;
		this.sellerType = sellerType;
	}

	protected SupplierData(Parcel in) {
		sellerTypeId = in.readInt();
		storeType = in.readString();
		storeFrontTypeId = in.readInt();
		productId = in.readString();
		retailerQty = in.readInt();
		rating = in.readInt();
		currencySymbol = in.readString();
		storeTypeId = in.readInt();
		distributorPrice = in.readInt();
		storeFrontType = in.readString();
		distributorQty = in.readInt();
		cityName = in.readString();
		areaName = in.readString();
		retailerPrice = in.readInt();
		currency = in.readString();
		storeName = in.readString();
		id = in.readString();
		sellerType = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(sellerTypeId);
		dest.writeString(storeType);
		dest.writeInt(storeFrontTypeId);
		dest.writeString(productId);
		dest.writeInt(retailerQty);
		dest.writeInt(rating);
		dest.writeString(currencySymbol);
		dest.writeInt(storeTypeId);
		dest.writeInt(distributorPrice);
		dest.writeString(storeFrontType);
		dest.writeInt(distributorQty);
		dest.writeString(cityName);
		dest.writeString(areaName);
		dest.writeInt(retailerPrice);
		dest.writeString(currency);
		dest.writeString(storeName);
		dest.writeString(id);
		dest.writeString(sellerType);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SupplierData> CREATOR = new Creator<SupplierData>() {
		@Override
		public SupplierData createFromParcel(Parcel in) {
			return new SupplierData(in);
		}

		@Override
		public SupplierData[] newArray(int size) {
			return new SupplierData[size];
		}
	};

	public void setSellerTypeId(int sellerTypeId){
		this.sellerTypeId = sellerTypeId;
	}

	public int getSellerTypeId(){
		return sellerTypeId;
	}

	public void setStoreType(String storeType){
		this.storeType = storeType;
	}

	public String getStoreType(){
		return storeType;
	}

	public void setStoreFrontTypeId(int storeFrontTypeId){
		this.storeFrontTypeId = storeFrontTypeId;
	}

	public int getStoreFrontTypeId(){
		return storeFrontTypeId;
	}

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

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	public void setCurrencySymbol(String currencySymbol){
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public void setStoreTypeId(int storeTypeId){
		this.storeTypeId = storeTypeId;
	}

	public int getStoreTypeId(){
		return storeTypeId;
	}

	public void setDistributorPrice(int distributorPrice){
		this.distributorPrice = distributorPrice;
	}

	public int getDistributorPrice(){
		return distributorPrice;
	}

	public void setStoreFrontType(String storeFrontType){
		this.storeFrontType = storeFrontType;
	}

	public String getStoreFrontType(){
		return storeFrontType;
	}

	public void setDistributorQty(int distributorQty){
		this.distributorQty = distributorQty;
	}

	public int getDistributorQty(){
		return distributorQty;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setAreaName(String areaName){
		this.areaName = areaName;
	}

	public String getAreaName(){
		return areaName;
	}

	public void setRetailerPrice(int retailerPrice){
		this.retailerPrice = retailerPrice;
	}

	public int getRetailerPrice(){
		return retailerPrice;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setStoreName(String storeName){
		this.storeName = storeName;
	}

	public String getStoreName(){
		return storeName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSellerType(String sellerType){
		this.sellerType = sellerType;
	}

	public String getSellerType(){
		return sellerType;
	}
}
