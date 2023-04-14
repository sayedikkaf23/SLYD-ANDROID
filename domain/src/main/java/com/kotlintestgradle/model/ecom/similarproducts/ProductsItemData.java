package com.kotlintestgradle.model.ecom.similarproducts;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import java.util.ArrayList;
import java.util.List;

public class ProductsItemData implements Parcelable {
	private String moUnit;
	private int maxQuantity;
	private ArrayList<VariantsData> variantData;
	private int discountPrice;
	private int storeCount;
	private String productName;
	private int score;
	private ProductSeoData productSeo;
	private boolean allowOrderOutOfStock;
	private int price;
	private SupplierData supplier;
	private boolean outOfStock;
	private String unitId;
	private int discountType;
	private String currency;
	private MouDataData mouData;
	private OffersData offers;
	private String uploadProductDetails;
	private int availableQuantity;
	private String brandName;
	private List<ImagesItemData> images;
	private String parentProductId;
	private boolean addOnsCount;
	private boolean isShoppingList;
	private String childProductId;
	private String currencySymbol;
	private int totalStarRating;
	private boolean variantCount;
	private boolean prescriptionRequired;
	private String storeCategoryId;
	private boolean saleOnline;
	private String manufactureName;
	private String mouDataUnit;
	private FinalPriceListData finalPriceList;

	public ProductsItemData(String moUnit, int maxQuantity, ArrayList<VariantsData> variantData, int discountPrice, int storeCount, String productName, int score, ProductSeoData productSeo, boolean allowOrderOutOfStock, int price, SupplierData supplier, boolean outOfStock, String unitId, int discountType, String currency, MouDataData mouData, OffersData offers, String uploadProductDetails, int availableQuantity, String brandName, List<ImagesItemData> images, String parentProductId, boolean addOnsCount, boolean isShoppingList, String childProductId, String currencySymbol, int totalStarRating, boolean variantCount, boolean prescriptionRequired, String storeCategoryId, boolean saleOnline, String manufactureName, String mouDataUnit, FinalPriceListData finalPriceList) {
		this.moUnit = moUnit;
		this.maxQuantity = maxQuantity;
		this.variantData = variantData;
		this.discountPrice = discountPrice;
		this.storeCount = storeCount;
		this.productName = productName;
		this.score = score;
		this.productSeo = productSeo;
		this.allowOrderOutOfStock = allowOrderOutOfStock;
		this.price = price;
		this.supplier = supplier;
		this.outOfStock = outOfStock;
		this.unitId = unitId;
		this.discountType = discountType;
		this.currency = currency;
		this.mouData = mouData;
		this.offers = offers;
		this.uploadProductDetails = uploadProductDetails;
		this.availableQuantity = availableQuantity;
		this.brandName = brandName;
		this.images = images;
		this.parentProductId = parentProductId;
		this.addOnsCount = addOnsCount;
		this.isShoppingList = isShoppingList;
		this.childProductId = childProductId;
		this.currencySymbol = currencySymbol;
		this.totalStarRating = totalStarRating;
		this.variantCount = variantCount;
		this.prescriptionRequired = prescriptionRequired;
		this.storeCategoryId = storeCategoryId;
		this.saleOnline = saleOnline;
		this.manufactureName = manufactureName;
		this.mouDataUnit = mouDataUnit;
		this.finalPriceList = finalPriceList;
	}

	protected ProductsItemData(Parcel in) {
		moUnit = in.readString();
		maxQuantity = in.readInt();
		variantData = in.createTypedArrayList(VariantsData.CREATOR);
		discountPrice = in.readInt();
		storeCount = in.readInt();
		productName = in.readString();
		score = in.readInt();
		productSeo = in.readParcelable(ProductSeoData.class.getClassLoader());
		allowOrderOutOfStock = in.readByte() != 0;
		price = in.readInt();
		supplier = in.readParcelable(SupplierData.class.getClassLoader());
		outOfStock = in.readByte() != 0;
		unitId = in.readString();
		discountType = in.readInt();
		currency = in.readString();
		mouData = in.readParcelable(MouDataData.class.getClassLoader());
		uploadProductDetails = in.readString();
		availableQuantity = in.readInt();
		brandName = in.readString();
		images = in.createTypedArrayList(ImagesItemData.CREATOR);
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
		finalPriceList = in.readParcelable(FinalPriceListData.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(moUnit);
		dest.writeInt(maxQuantity);
		dest.writeTypedList(variantData);
		dest.writeInt(discountPrice);
		dest.writeInt(storeCount);
		dest.writeString(productName);
		dest.writeInt(score);
		dest.writeParcelable(productSeo, flags);
		dest.writeByte((byte) (allowOrderOutOfStock ? 1 : 0));
		dest.writeInt(price);
		dest.writeParcelable(supplier, flags);
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

	public static final Creator<ProductsItemData> CREATOR = new Creator<ProductsItemData>() {
		@Override
		public ProductsItemData createFromParcel(Parcel in) {
			return new ProductsItemData(in);
		}

		@Override
		public ProductsItemData[] newArray(int size) {
			return new ProductsItemData[size];
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

	public void setVariantData(ArrayList<VariantsData> variantData){
		this.variantData = variantData;
	}

	public ArrayList<VariantsData> getVariantData(){
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

	public void setProductSeo(ProductSeoData productSeo){
		this.productSeo = productSeo;
	}

	public ProductSeoData getProductSeo(){
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

	public void setSupplier(SupplierData supplier){
		this.supplier = supplier;
	}

	public SupplierData getSupplier(){
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

	public void setMouData(MouDataData mouData){
		this.mouData = mouData;
	}

	public MouDataData getMouData(){
		return mouData;
	}

	public void setOffers(OffersData offers){
		this.offers = offers;
	}

	public OffersData getOffers(){
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

	public void setImages(List<ImagesItemData> images){
		this.images = images;
	}

	public List<ImagesItemData> getImages(){
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

	public void setFinalPriceList(FinalPriceListData finalPriceList){
		this.finalPriceList = finalPriceList;
	}

	public FinalPriceListData getFinalPriceList(){
		return finalPriceList;
	}
}