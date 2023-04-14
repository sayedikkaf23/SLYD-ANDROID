package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class Supplier implements ValidItem {

	@SerializedName("sellerTypeId")
	private int sellerTypeId;

	@SerializedName("storeType")
	private String storeType;

	@SerializedName("storeFrontTypeId")
	private int storeFrontTypeId;

	@SerializedName("productId")
	private String productId;

	@SerializedName("retailerQty")
	private int retailerQty;

	@SerializedName("rating")
	private int rating;

	@SerializedName("currencySymbol")
	private String currencySymbol;

	@SerializedName("storeTypeId")
	private int storeTypeId;

	@SerializedName("distributorPrice")
	private int distributorPrice;

	@SerializedName("storeFrontType")
	private String storeFrontType;

	@SerializedName("distributorQty")
	private int distributorQty;

	@SerializedName("cityName")
	private String cityName;

	@SerializedName("areaName")
	private String areaName;

	@SerializedName("retailerPrice")
	private int retailerPrice;

	@SerializedName("currency")
	private String currency;

	@SerializedName("storeName")
	private String storeName;

	@SerializedName("id")
	private String id;

	@SerializedName("sellerType")
	private String sellerType;

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

	@Override
	public boolean isValid() {
		return true;
	}
}