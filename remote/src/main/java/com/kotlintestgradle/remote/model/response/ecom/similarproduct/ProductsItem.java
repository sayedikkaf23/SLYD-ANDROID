package com.kotlintestgradle.remote.model.response.ecom.similarproduct;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.pdp.VariantDetails;
import java.util.ArrayList;
import java.util.List;

public class ProductsItem implements Parcelable, ValidItem {

	@SerializedName("moUnit")
	private String moUnit;

	@SerializedName("maxQuantity")
	private int maxQuantity;

	@SerializedName("variantData")
	private ArrayList<VariantDetails> variantData;

	@SerializedName("discountPrice")
	private int discountPrice;

	@SerializedName("storeCount")
	private int storeCount;

	@SerializedName("productName")
	private String productName;

	@SerializedName("score")
	private int score;

	@SerializedName("productSeo")
	private ProductSeo productSeo;

	@SerializedName("allowOrderOutOfStock")
	private boolean allowOrderOutOfStock;

	@SerializedName("price")
	private int price;

	@SerializedName("supplier")
	private Supplier supplier;

	@SerializedName("outOfStock")
	private boolean outOfStock;

	@SerializedName("unitId")
	private String unitId;

	@SerializedName("discountType")
	private int discountType;

	@SerializedName("currency")
	private String currency;

	@SerializedName("mouData")
	private MouData mouData;

	@SerializedName("offers")
	private Offers offers;

	@SerializedName("uploadProductDetails")
	private String uploadProductDetails;

	@SerializedName("availableQuantity")
	private int availableQuantity;

	@SerializedName("brandName")
	private String brandName;

	@SerializedName("images")
	private List<ImagesItem> images;

	@SerializedName("parentProductId")
	private String parentProductId;

	@SerializedName("addOnsCount")
	private boolean addOnsCount;

	@SerializedName("isShoppingList")
	private boolean isShoppingList;

	@SerializedName("childProductId")
	private String childProductId;

	@SerializedName("currencySymbol")
	private String currencySymbol;

	@SerializedName("TotalStarRating")
	private int totalStarRating;

	@SerializedName("variantCount")
	private boolean variantCount;

	@SerializedName("prescriptionRequired")
	private boolean prescriptionRequired;

	@SerializedName("storeCategoryId")
	private String storeCategoryId;

	@SerializedName("saleOnline")
	private boolean saleOnline;

	@SerializedName("manufactureName")
	private String manufactureName;

	@SerializedName("mouDataUnit")
	private String mouDataUnit;

	@SerializedName("finalPriceList")
	private FinalPriceList finalPriceList;

	protected ProductsItem(Parcel in) {
		moUnit = in.readString();
		maxQuantity = in.readInt();
		discountPrice = in.readInt();
		storeCount = in.readInt();
		productName = in.readString();
		score = in.readInt();
		productSeo = in.readParcelable(ProductSeo.class.getClassLoader());
		allowOrderOutOfStock = in.readByte() != 0;
		price = in.readInt();
		outOfStock = in.readByte() != 0;
		unitId = in.readString();
		discountType = in.readInt();
		currency = in.readString();
		mouData = in.readParcelable(MouData.class.getClassLoader());
		uploadProductDetails = in.readString();
		availableQuantity = in.readInt();
		brandName = in.readString();
		images = in.createTypedArrayList(ImagesItem.CREATOR);
		parentProductId = in.readString();
		addOnsCount = in.readByte() != 0;
		isShoppingList = in.readByte() != 0;
		childProductId = in.readString();
		currencySymbol = in.readString();
		totalStarRating = in.readInt();
		variantCount = in.readByte() != 0;
		prescriptionRequired = in.readByte() != 0;
		storeCategoryId = in.readString();
		saleOnline = in.readByte() != 0;
		manufactureName = in.readString();
		mouDataUnit = in.readString();
		finalPriceList = in.readParcelable(FinalPriceList.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(moUnit);
		dest.writeInt(maxQuantity);
		dest.writeInt(discountPrice);
		dest.writeInt(storeCount);
		dest.writeString(productName);
		dest.writeInt(score);
		dest.writeParcelable(productSeo, flags);
		dest.writeByte((byte) (allowOrderOutOfStock ? 1 : 0));
		dest.writeInt(price);
		dest.writeByte((byte) (outOfStock ? 1 : 0));
		dest.writeString(unitId);
		dest.writeInt(discountType);
		dest.writeString(currency);
		dest.writeParcelable(mouData, flags);
		dest.writeString(uploadProductDetails);
		dest.writeInt(availableQuantity);
		dest.writeString(brandName);
		dest.writeTypedList(images);
		dest.writeString(parentProductId);
		dest.writeByte((byte) (addOnsCount ? 1 : 0));
		dest.writeByte((byte) (isShoppingList ? 1 : 0));
		dest.writeString(childProductId);
		dest.writeString(currencySymbol);
		dest.writeInt(totalStarRating);
		dest.writeByte((byte) (variantCount ? 1 : 0));
		dest.writeByte((byte) (prescriptionRequired ? 1 : 0));
		dest.writeString(storeCategoryId);
		dest.writeByte((byte) (saleOnline ? 1 : 0));
		dest.writeString(manufactureName);
		dest.writeString(mouDataUnit);
		dest.writeParcelable(finalPriceList, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ProductsItem> CREATOR = new Creator<ProductsItem>() {
		@Override
		public ProductsItem createFromParcel(Parcel in) {
			return new ProductsItem(in);
		}

		@Override
		public ProductsItem[] newArray(int size) {
			return new ProductsItem[size];
		}
	};

	public void setMoUnit(String moUnit){
		this.moUnit = moUnit;
	}

	public String getMoUnit(){
		return moUnit;
	}

	public void setMaxQuantity(int maxQuantity){
		this.maxQuantity = maxQuantity;
	}

	public int getMaxQuantity(){
		return maxQuantity;
	}

	public void setVariantData(ArrayList<VariantDetails> variantData){
		this.variantData = variantData;
	}

	public ArrayList<VariantDetails> getVariantData(){
		return variantData;
	}

	public void setDiscountPrice(int discountPrice){
		this.discountPrice = discountPrice;
	}

	public int getDiscountPrice(){
		return discountPrice;
	}

	public void setStoreCount(int storeCount){
		this.storeCount = storeCount;
	}

	public int getStoreCount(){
		return storeCount;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setScore(int score){
		this.score = score;
	}

	public int getScore(){
		return score;
	}

	public void setProductSeo(ProductSeo productSeo){
		this.productSeo = productSeo;
	}

	public ProductSeo getProductSeo(){
		return productSeo;
	}

	public void setAllowOrderOutOfStock(boolean allowOrderOutOfStock){
		this.allowOrderOutOfStock = allowOrderOutOfStock;
	}

	public boolean isAllowOrderOutOfStock(){
		return allowOrderOutOfStock;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
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

	public void setUnitId(String unitId){
		this.unitId = unitId;
	}

	public String getUnitId(){
		return unitId;
	}

	public void setDiscountType(int discountType){
		this.discountType = discountType;
	}

	public int getDiscountType(){
		return discountType;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	public void setMouData(MouData mouData){
		this.mouData = mouData;
	}

	public MouData getMouData(){
		return mouData;
	}

	public void setOffers(Offers offers){
		this.offers = offers;
	}

	public Offers getOffers(){
		return offers;
	}

	public void setUploadProductDetails(String uploadProductDetails){
		this.uploadProductDetails = uploadProductDetails;
	}

	public String getUploadProductDetails(){
		return uploadProductDetails;
	}

	public void setAvailableQuantity(int availableQuantity){
		this.availableQuantity = availableQuantity;
	}

	public int getAvailableQuantity(){
		return availableQuantity;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

	public String getBrandName(){
		return brandName;
	}

	public void setImages(List<ImagesItem> images){
		this.images = images;
	}

	public List<ImagesItem> getImages(){
		return images;
	}

	public void setParentProductId(String parentProductId){
		this.parentProductId = parentProductId;
	}

	public String getParentProductId(){
		return parentProductId;
	}

	public void setAddOnsCount(boolean addOnsCount){
		this.addOnsCount = addOnsCount;
	}

	public boolean isAddOnsCount(){
		return addOnsCount;
	}

	public void setIsShoppingList(boolean isShoppingList){
		this.isShoppingList = isShoppingList;
	}

	public boolean isIsShoppingList(){
		return isShoppingList;
	}

	public void setChildProductId(String childProductId){
		this.childProductId = childProductId;
	}

	public String getChildProductId(){
		return childProductId;
	}

	public void setCurrencySymbol(String currencySymbol){
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public void setTotalStarRating(int totalStarRating){
		this.totalStarRating = totalStarRating;
	}

	public int getTotalStarRating(){
		return totalStarRating;
	}

	public void setVariantCount(boolean variantCount){
		this.variantCount = variantCount;
	}

	public boolean isVariantCount(){
		return variantCount;
	}

	public void setPrescriptionRequired(boolean prescriptionRequired){
		this.prescriptionRequired = prescriptionRequired;
	}

	public boolean isPrescriptionRequired(){
		return prescriptionRequired;
	}

	public void setStoreCategoryId(String storeCategoryId){
		this.storeCategoryId = storeCategoryId;
	}

	public String getStoreCategoryId(){
		return storeCategoryId;
	}

	public void setSaleOnline(boolean saleOnline){
		this.saleOnline = saleOnline;
	}

	public boolean isSaleOnline(){
		return saleOnline;
	}

	public void setManufactureName(String manufactureName){
		this.manufactureName = manufactureName;
	}

	public String getManufactureName(){
		return manufactureName;
	}

	public void setMouDataUnit(String mouDataUnit){
		this.mouDataUnit = mouDataUnit;
	}

	public String getMouDataUnit(){
		return mouDataUnit;
	}

	public void setFinalPriceList(FinalPriceList finalPriceList){
		this.finalPriceList = finalPriceList;
	}

	public FinalPriceList getFinalPriceList(){
		return finalPriceList;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}