package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsItem implements Parcelable, ValidItem {

	@Expose
	@SerializedName("upcNumber")
	private String upcNumber;

	@Expose
	@SerializedName("color")
	private String color;

	@SerializedName("needsWeighed")
	private boolean needsWeighed;

	@Expose
	@SerializedName("forcePickImage")
	private String forcePickImage;

	@Expose
	@SerializedName("timestamps")
	private Timestamps timestamps;

	@Expose
	@SerializedName("singleUnitPrice")
	private SingleUnitPrice singleUnitPrice;

	@Expose
	@SerializedName("offerDetails")
	private OfferDetails offerDetails;

	@Expose
	@SerializedName("noofunits")
	private int noofunits;

	@Expose
	@SerializedName("preparationTime")
	private int preparationTime;

	@Expose
	@SerializedName("productOrderId")
	private String productOrderId;

	@Expose
	@SerializedName("accounting")
	private Accounting accounting;

	@Expose
	@SerializedName("aisle")
	private String aisle;

	@Expose
	@SerializedName("packageType")
	private String packageType;

	@Expose
	@SerializedName("centralProductId")
	private String centralProductId;

	@Expose
	@SerializedName("allowOrderOutOfStock")
	private boolean allowOrderOutOfStock;

	@Expose
	@SerializedName("shippingDetails")
	private ShippingDetails shippingDetails;

	@Expose
	@SerializedName("unitId")
	private String unitId;

	@Expose
	@SerializedName("invoiceLink")
	private String invoiceLink;

	@Expose
	@SerializedName("barcode")
	private String barcode;

	@Expose
	@SerializedName("productDeliveryFee")
	private int productDeliveryFee;

	@Expose
	@SerializedName("mouData")
	private MouData mouData;

	@Expose
	@SerializedName("images")
	private Images images;

	@Expose
	@SerializedName("brandName")
	private String brandName;

	@Expose
	@SerializedName("quantity")
	private Quantity quantity;

	@Expose
	@SerializedName("productId")
	private String productId;

	@Expose
	@SerializedName("coupon")
	private String coupon;

	@Expose
	@SerializedName("addOns")
	private List<Object> addOns;

	@Expose
	@SerializedName("packageId")
	private String packageId;

	@Expose
	@SerializedName("shippingLabel")
	private String shippingLabel;

	@Expose
	@SerializedName("isSplitProduct")
	private boolean isSplitProduct;

	@Expose
	@SerializedName("currencySymbol")
	private String currencySymbol;

	@Expose
	@SerializedName("packaging")
	private Packaging packaging;

	@Expose
	@SerializedName("shelf")
	private String shelf;

	@Expose
	@SerializedName("isParentProduct")
	private boolean isParentProduct;

	@Expose
	@SerializedName("prescriptionRequired")
	private boolean prescriptionRequired;

	@Expose
	@SerializedName("isCentral")
	private boolean isCentral;

	@Expose
	@SerializedName("directions")
	private String directions;

	@Expose
	@SerializedName("manufactureName")
	private String manufactureName;

	@Expose
	@SerializedName("name")
	private String name;

	@Expose
	@SerializedName("reason")
	private String reason;

	@Expose
	@SerializedName("rattingData")
	private RattingData rattingData;

	@Expose
	@SerializedName("attributes")
	private List<AttributesItem> attributes;

	@Expose
	@SerializedName("currencyCode")
	private String currencyCode;

	@Expose
	@SerializedName("status")
	private Status status;

	@Expose
	@SerializedName("subsitute")
	private boolean hasSubstitute;

	@Expose
	@SerializedName("subsituteWith")
	private SubstitutesWith substitutesWith;

	protected ProductsItem(Parcel in) {
		upcNumber = in.readString();
		color = in.readString();
		needsWeighed = in.readByte() != 0;
		forcePickImage = in.readString();
		timestamps = in.readParcelable(Timestamps.class.getClassLoader());
		singleUnitPrice = in.readParcelable(SingleUnitPrice.class.getClassLoader());
		offerDetails = in.readParcelable(OfferDetails.class.getClassLoader());
		noofunits = in.readInt();
		preparationTime = in.readInt();
		productOrderId = in.readString();
		accounting = in.readParcelable(Accounting.class.getClassLoader());
		aisle = in.readString();
		packageType = in.readString();
		centralProductId = in.readString();
		allowOrderOutOfStock = in.readByte() != 0;
		shippingDetails = in.readParcelable(ShippingDetails.class.getClassLoader());
		unitId = in.readString();
		invoiceLink = in.readString();
		barcode = in.readString();
		productDeliveryFee = in.readInt();
		mouData = in.readParcelable(MouData.class.getClassLoader());
		images = in.readParcelable(Images.class.getClassLoader());
		brandName = in.readString();
		quantity = in.readParcelable(Quantity.class.getClassLoader());
		productId = in.readString();
		coupon = in.readString();
		packageId = in.readString();
		shippingLabel = in.readString();
		isSplitProduct = in.readByte() != 0;
		currencySymbol = in.readString();
		packaging = in.readParcelable(Packaging.class.getClassLoader());
		shelf = in.readString();
		isParentProduct = in.readByte() != 0;
		prescriptionRequired = in.readByte() != 0;
		isCentral = in.readByte() != 0;
		directions = in.readString();
		manufactureName = in.readString();
		name = in.readString();
		reason = in.readString();
		rattingData = in.readParcelable(RattingData.class.getClassLoader());
		attributes = in.createTypedArrayList(AttributesItem.CREATOR);
		currencyCode = in.readString();
		status = in.readParcelable(Status.class.getClassLoader());
		hasSubstitute = in.readByte() != 0;
		substitutesWith = in.readParcelable(SubstitutesWith.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(upcNumber);
		dest.writeString(color);
		dest.writeByte((byte) (needsWeighed ? 1 : 0));
		dest.writeString(forcePickImage);
		dest.writeParcelable(timestamps, flags);
		dest.writeParcelable(singleUnitPrice, flags);
		dest.writeParcelable(offerDetails, flags);
		dest.writeInt(noofunits);
		dest.writeInt(preparationTime);
		dest.writeString(productOrderId);
		dest.writeParcelable(accounting, flags);
		dest.writeString(aisle);
		dest.writeString(packageType);
		dest.writeString(centralProductId);
		dest.writeByte((byte) (allowOrderOutOfStock ? 1 : 0));
		dest.writeParcelable(shippingDetails, flags);
		dest.writeString(unitId);
		dest.writeString(invoiceLink);
		dest.writeString(barcode);
		dest.writeInt(productDeliveryFee);
		dest.writeParcelable(mouData, flags);
		dest.writeParcelable(images, flags);
		dest.writeString(brandName);
		dest.writeParcelable(quantity, flags);
		dest.writeString(productId);
		dest.writeString(coupon);
		dest.writeString(packageId);
		dest.writeString(shippingLabel);
		dest.writeByte((byte) (isSplitProduct ? 1 : 0));
		dest.writeString(currencySymbol);
		dest.writeParcelable(packaging, flags);
		dest.writeString(shelf);
		dest.writeByte((byte) (isParentProduct ? 1 : 0));
		dest.writeByte((byte) (prescriptionRequired ? 1 : 0));
		dest.writeByte((byte) (isCentral ? 1 : 0));
		dest.writeString(directions);
		dest.writeString(manufactureName);
		dest.writeString(name);
		dest.writeString(reason);
		dest.writeParcelable(rattingData, flags);
		dest.writeTypedList(attributes);
		dest.writeString(currencyCode);
		dest.writeParcelable(status, flags);
		dest.writeByte((byte) (hasSubstitute ? 1 : 0));
		dest.writeParcelable(substitutesWith, flags);
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

	public void setUpcNumber(String upcNumber){
		this.upcNumber = upcNumber;
	}

	public String getUpcNumber(){
		return upcNumber;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setForcePickImage(String forcePickImage){
		this.forcePickImage = forcePickImage;
	}

	public String getForcePickImage(){
		return forcePickImage;
	}

	public void setTimestamps(Timestamps timestamps){
		this.timestamps = timestamps;
	}

	public Timestamps getTimestamps(){
		return timestamps;
	}

	public void setSingleUnitPrice(SingleUnitPrice singleUnitPrice){
		this.singleUnitPrice = singleUnitPrice;
	}

	public boolean isNeedsWeighed() {
		return needsWeighed;
	}

	public void setNeedsWeighed(boolean needsWeighed) {
		this.needsWeighed = needsWeighed;
	}

	public boolean isSplitProduct() {
		return isSplitProduct;
	}

	public void setSplitProduct(boolean splitProduct) {
		isSplitProduct = splitProduct;
	}

	public boolean isParentProduct() {
		return isParentProduct;
	}

	public void setParentProduct(boolean parentProduct) {
		isParentProduct = parentProduct;
	}

	public boolean isCentral() {
		return isCentral;
	}

	public void setCentral(boolean central) {
		isCentral = central;
	}

	public SingleUnitPrice getSingleUnitPrice(){
		return singleUnitPrice;
	}

	public void setOfferDetails(OfferDetails offerDetails){
		this.offerDetails = offerDetails;
	}

	public OfferDetails getOfferDetails(){
		return offerDetails;
	}

	public void setNoofunits(int noofunits){
		this.noofunits = noofunits;
	}

	public int getNoofunits(){
		return noofunits;
	}

	public void setPreparationTime(int preparationTime){
		this.preparationTime = preparationTime;
	}

	public int getPreparationTime(){
		return preparationTime;
	}

	public void setProductOrderId(String productOrderId){
		this.productOrderId = productOrderId;
	}

	public String getProductOrderId(){
		return productOrderId;
	}

	public void setAccounting(Accounting accounting){
		this.accounting = accounting;
	}

	public Accounting getAccounting(){
		return accounting;
	}

	public void setAisle(String aisle){
		this.aisle = aisle;
	}

	public String getAisle(){
		return aisle;
	}

	public void setPackageType(String packageType){
		this.packageType = packageType;
	}

	public String getPackageType(){
		return packageType;
	}

	public void setCentralProductId(String centralProductId){
		this.centralProductId = centralProductId;
	}

	public String getCentralProductId(){
		return centralProductId;
	}

	public void setAllowOrderOutOfStock(boolean allowOrderOutOfStock){
		this.allowOrderOutOfStock = allowOrderOutOfStock;
	}

	public boolean isAllowOrderOutOfStock(){
		return allowOrderOutOfStock;
	}

	public void setShippingDetails(ShippingDetails shippingDetails){
		this.shippingDetails = shippingDetails;
	}

	public ShippingDetails getShippingDetails(){
		return shippingDetails;
	}

	public void setUnitId(String unitId){
		this.unitId = unitId;
	}

	public String getUnitId(){
		return unitId;
	}

	public void setInvoiceLink(String invoiceLink){
		this.invoiceLink = invoiceLink;
	}

	public String getInvoiceLink(){
		return invoiceLink;
	}

	public void setBarcode(String barcode){
		this.barcode = barcode;
	}

	public String getBarcode(){
		return barcode;
	}

	public void setProductDeliveryFee(int productDeliveryFee){
		this.productDeliveryFee = productDeliveryFee;
	}

	public int getProductDeliveryFee(){
		return productDeliveryFee;
	}

	public void setMouData(MouData mouData){
		this.mouData = mouData;
	}

	public MouData getMouData(){
		return mouData;
	}

	public void setImages(Images images){
		this.images = images;
	}

	public Images getImages(){
		return images;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

	public String getBrandName(){
		return brandName;
	}

	public void setQuantity(Quantity quantity){
		this.quantity = quantity;
	}

	public Quantity getQuantity(){
		return quantity;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setCoupon(String coupon){
		this.coupon = coupon;
	}

	public String getCoupon(){
		return coupon;
	}

	public void setAddOns(List<Object> addOns){
		this.addOns = addOns;
	}

	public List<Object> getAddOns(){
		return addOns;
	}

	public void setPackageId(String packageId){
		this.packageId = packageId;
	}

	public String getPackageId(){
		return packageId;
	}

	public void setShippingLabel(String shippingLabel){
		this.shippingLabel = shippingLabel;
	}

	public String getShippingLabel(){
		return shippingLabel;
	}

	public void setIsSplitProduct(boolean isSplitProduct){
		this.isSplitProduct = isSplitProduct;
	}

	public boolean isIsSplitProduct(){
		return isSplitProduct;
	}

	public void setCurrencySymbol(String currencySymbol){
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public void setPackaging(Packaging packaging){
		this.packaging = packaging;
	}

	public Packaging getPackaging(){
		return packaging;
	}

	public void setShelf(String shelf){
		this.shelf = shelf;
	}

	public String getShelf(){
		return shelf;
	}

	public void setIsParentProduct(boolean isParentProduct){
		this.isParentProduct = isParentProduct;
	}

	public boolean isIsParentProduct(){
		return isParentProduct;
	}

	public void setPrescriptionRequired(boolean prescriptionRequired){
		this.prescriptionRequired = prescriptionRequired;
	}

	public boolean isPrescriptionRequired(){
		return prescriptionRequired;
	}

	public void setIsCentral(boolean isCentral){
		this.isCentral = isCentral;
	}

	public boolean isIsCentral(){
		return isCentral;
	}

	public void setDirections(String directions){
		this.directions = directions;
	}

	public String getDirections(){
		return directions;
	}

	public void setManufactureName(String manufactureName){
		this.manufactureName = manufactureName;
	}

	public String getManufactureName(){
		return manufactureName;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setRattingData(RattingData rattingData){
		this.rattingData = rattingData;
	}

	public RattingData getRattingData(){
		return rattingData;
	}

	public void setAttributes(List<AttributesItem> attributes){
		this.attributes = attributes;
	}

	public List<AttributesItem> getAttributes(){
		return attributes;
	}

	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean hasSubstitute() {
		return hasSubstitute;
	}

	public void setHasSubstitute(boolean hasSubstitute) {
		this.hasSubstitute = hasSubstitute;
	}

	public SubstitutesWith getSubstitutesWith() {
		return substitutesWith;
	}

	public void setSubstitutesWith(SubstitutesWith substitutesWith) {
		this.substitutesWith = substitutesWith;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}