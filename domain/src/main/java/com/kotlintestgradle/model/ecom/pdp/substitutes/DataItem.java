package com.kotlintestgradle.model.ecom.pdp.substitutes;

import android.os.Parcel;
import android.os.Parcelable;

public class DataItem implements Parcelable {
	private Offers offers;
	private Images images;
	private String parentProductId;
	private String childProductId;
	private String manufactureName;
	private Supplier supplier;
	private boolean outOfStock;
	private String currencySymbol;
	private String currency;
	private String brandTitle;
	private String productName;
	private FinalPriceList finalPriceList;

	public DataItem(Offers offers, Images images, String parentProductId, String childProductId,
					String manufactureName, Supplier supplier, boolean outOfStock, String currencySymbol,
					String currency, String brandTitle, String productName, FinalPriceList finalPriceList) {
		this.offers = offers;
		this.images = images;
		this.parentProductId = parentProductId;
		this.childProductId = childProductId;
		this.manufactureName = manufactureName;
		this.supplier = supplier;
		this.outOfStock = outOfStock;
		this.currencySymbol = currencySymbol;
		this.currency = currency;
		this.brandTitle = brandTitle;
		this.productName = productName;
		this.finalPriceList = finalPriceList;
	}

	protected DataItem(Parcel in) {
		offers = in.readParcelable(Offers.class.getClassLoader());
		images = in.readParcelable(Images.class.getClassLoader());
		parentProductId = in.readString();
		childProductId = in.readString();
		manufactureName = in.readString();
		supplier = in.readParcelable(Supplier.class.getClassLoader());
		outOfStock = in.readByte() != 0;
		currencySymbol = in.readString();
		currency = in.readString();
		brandTitle = in.readString();
		productName = in.readString();
		finalPriceList = in.readParcelable(FinalPriceList.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(offers, flags);
		dest.writeParcelable(images, flags);
		dest.writeString(parentProductId);
		dest.writeString(childProductId);
		dest.writeString(manufactureName);
		dest.writeParcelable(supplier, flags);
		dest.writeByte((byte) (outOfStock ? 1 : 0));
		dest.writeString(currencySymbol);
		dest.writeString(currency);
		dest.writeString(brandTitle);
		dest.writeString(productName);
		dest.writeParcelable(finalPriceList, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
		@Override
		public DataItem createFromParcel(Parcel in) {
			return new DataItem(in);
		}

		@Override
		public DataItem[] newArray(int size) {
			return new DataItem[size];
		}
	};

	public void setOffers(Offers offers){
		this.offers = offers;
	}

	public Offers getOffers(){
		return offers;
	}

	public void setImages(Images images){
		this.images = images;
	}

	public Images getImages(){
		return images;
	}

	public void setParentProductId(String parentProductId){
		this.parentProductId = parentProductId;
	}

	public String getParentProductId(){
		return parentProductId;
	}

	public void setChildProductId(String childProductId){
		this.childProductId = childProductId;
	}

	public String getChildProductId(){
		return childProductId;
	}

	public void setManufactureName(String manufactureName){
		this.manufactureName = manufactureName;
	}

	public String getManufactureName(){
		return manufactureName;
	}

	public void setSupplier(Supplier supplier){
		this.supplier = supplier;
	}

	public Supplier getSupplier(){
		return supplier;
	}

	public void setOutOfStock(boolean outOfStock){
		this.outOfStock = outOfStock;
	}

	public boolean isOutOfStock(){
		return outOfStock;
	}

	public void setCurrencySymbol(String currencySymbol){
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setBrandTitle(String brandTitle){
		this.brandTitle = brandTitle;
	}

	public String getBrandTitle(){
		return brandTitle;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setFinalPriceList(FinalPriceList finalPriceList){
		this.finalPriceList = finalPriceList;
	}

	public FinalPriceList getFinalPriceList(){
		return finalPriceList;
	}
}
