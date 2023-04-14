package com.kotlintestgradle.remote.model.response.ecom.pdp.substitute;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class DataItems implements ValidItem, Parcelable {

	@Expose
	@SerializedName("offers")
	private SubstituteOffers substituteOffers;

	@Expose
	@SerializedName("images")
	private SubstituteImage substituteImage;

	@Expose
	@SerializedName("parentProductId")
	private String parentProductId;

	@Expose
	@SerializedName("childProductId")
	private String childProductId;

	@Expose
	@SerializedName("manufactureName")
	private String manufactureName;

	@Expose
	@SerializedName("supplier")
	private SubstituteSupplier substituteSupplier;

	@Expose
	@SerializedName("outOfStock")
	private boolean outOfStock;

	@SerializedName("currencySymbol")
	@Expose
	private String currencySymbol;

	@Expose
	@SerializedName("currency")
	private String currency;

	@Expose
	@SerializedName("brandTitle")
	private String brandTitle;

	@Expose
	@SerializedName("productName")
	private String productName;

	@SerializedName("finalPriceList")
	@Expose
	private FinalPriceLists finalPriceLists;

	protected DataItems(Parcel in) {
		parentProductId = in.readString();
		childProductId = in.readString();
		manufactureName = in.readString();
		substituteSupplier = in.readParcelable(SubstituteSupplier.class.getClassLoader());
		outOfStock = in.readByte() != 0;
		currencySymbol = in.readString();
		currency = in.readString();
		brandTitle = in.readString();
		productName = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(parentProductId);
		dest.writeString(childProductId);
		dest.writeString(manufactureName);
		dest.writeParcelable(substituteSupplier, flags);
		dest.writeByte((byte) (outOfStock ? 1 : 0));
		dest.writeString(currencySymbol);
		dest.writeString(currency);
		dest.writeString(brandTitle);
		dest.writeString(productName);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<DataItems> CREATOR = new Creator<DataItems>() {
		@Override
		public DataItems createFromParcel(Parcel in) {
			return new DataItems(in);
		}

		@Override
		public DataItems[] newArray(int size) {
			return new DataItems[size];
		}
	};

	public void setSubstituteOffers(SubstituteOffers substituteOffers){
		this.substituteOffers = substituteOffers;
	}

	public SubstituteOffers getSubstituteOffers(){
		return substituteOffers;
	}

	public void setSubstituteImage(SubstituteImage substituteImage){
		this.substituteImage = substituteImage;
	}

	public SubstituteImage getSubstituteImage(){
		return substituteImage;
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

	public void setSubstituteSupplier(SubstituteSupplier substituteSupplier){
		this.substituteSupplier = substituteSupplier;
	}

	public SubstituteSupplier getSubstituteSupplier(){
		return substituteSupplier;
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

	public void setFinalPriceLists(FinalPriceLists finalPriceLists){
		this.finalPriceLists = finalPriceLists;
	}

	public FinalPriceLists getFinalPriceLists(){
		return finalPriceLists;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}