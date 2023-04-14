package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProductsItemData implements Parcelable {
	private String upcNumber;
	private String color;
	private String forcePickImage;
	private TimestampsData timestamps;
	private SingleUnitPriceData singleUnitPrice;
	private OfferDetailsData offerDetails;
	private int noofunits;
	private int preparationTime;
	private String productOrderId;
	private AccountingData accounting;
	private String aisle;
	private String packageType;
	private String centralProductId;
	private boolean allowOrderOutOfStock;
	private ShippingDetailsData shippingDetails;
	private String unitId;
	private String invoiceLink;
	private String barcode;
	private int productDeliveryFee;
	private MousData mouData;
	private ImagesData images;
	private String brandName;
	private QuantityData quantity;
	private String productId;
	private String coupon;
	private List<Object> addOns;
	private String packageId;
	private String shippingLabel;
	private boolean isSplitProduct;
	private String currencySymbol;
	private PackagingData packaging;
	private String shelf;
	private boolean isParentProduct;
	private boolean prescriptionRequired;
	private boolean isCentral;
	private String directions;
	private String manufactureName;
	private String name;
	private String reason;
	private RattingsData rattingData;
	private List<AttributesItemData> attributes;
	private String currencyCode;
	private StatusData status;
	private int productType;
	private boolean hasSubstitute;
	private SubstituteWith substituteWith;
	private boolean isRemoved= false;

	public ProductsItemData(int productType) {
		this.productType = productType;
	}

	public ProductsItemData(String upcNumber, String color, String forcePickImage, TimestampsData timestamps, SingleUnitPriceData singleUnitPrice, OfferDetailsData offerDetails, int noofunits, int preparationTime, String productOrderId, AccountingData accounting, String aisle, String packageType, String centralProductId, boolean allowOrderOutOfStock, ShippingDetailsData shippingDetails, String unitId, String invoiceLink, String barcode, int productDeliveryFee, MousData mouData, ImagesData images, String brandName, QuantityData quantity, String productId, String coupon, List<Object> addOns, String packageId, String shippingLabel, boolean isSplitProduct, String currencySymbol, PackagingData packaging, String shelf, boolean isParentProduct, boolean prescriptionRequired, boolean isCentral, String directions, String manufactureName, String name, RattingsData rattingData, List<AttributesItemData> attributes, String currencyCode, StatusData status, String reason) {
		this.upcNumber = upcNumber;
		this.color = color;
		this.forcePickImage = forcePickImage;
		this.timestamps = timestamps;
		this.singleUnitPrice = singleUnitPrice;
		this.offerDetails = offerDetails;
		this.noofunits = noofunits;
		this.preparationTime = preparationTime;
		this.productOrderId = productOrderId;
		this.accounting = accounting;
		this.aisle = aisle;
		this.packageType = packageType;
		this.centralProductId = centralProductId;
		this.allowOrderOutOfStock = allowOrderOutOfStock;
		this.shippingDetails = shippingDetails;
		this.unitId = unitId;
		this.invoiceLink = invoiceLink;
		this.barcode = barcode;
		this.productDeliveryFee = productDeliveryFee;
		this.mouData = mouData;
		this.images = images;
		this.brandName = brandName;
		this.quantity = quantity;
		this.productId = productId;
		this.coupon = coupon;
		this.addOns = addOns;
		this.packageId = packageId;
		this.shippingLabel = shippingLabel;
		this.isSplitProduct = isSplitProduct;
		this.currencySymbol = currencySymbol;
		this.packaging = packaging;
		this.shelf = shelf;
		this.isParentProduct = isParentProduct;
		this.prescriptionRequired = prescriptionRequired;
		this.isCentral = isCentral;
		this.directions = directions;
		this.manufactureName = manufactureName;
		this.name = name;
		this.rattingData = rattingData;
		this.attributes = attributes;
		this.currencyCode = currencyCode;
		this.status = status;
		this.reason = reason;
	}

	public ProductsItemData(String upcNumber, String color, String forcePickImage, TimestampsData timestamps, SingleUnitPriceData singleUnitPrice, OfferDetailsData offerDetails, int noofunits, int preparationTime, String productOrderId, AccountingData accounting, String aisle, String packageType, String centralProductId, boolean allowOrderOutOfStock, ShippingDetailsData shippingDetails, String unitId, String invoiceLink, String barcode, int productDeliveryFee, MousData mouData, ImagesData images, String brandName, QuantityData quantity, String productId, String coupon, List<Object> addOns, String packageId, String shippingLabel, boolean isSplitProduct, String currencySymbol, PackagingData packaging, String shelf, boolean isParentProduct, boolean prescriptionRequired, boolean isCentral, String directions, String manufactureName, String name, RattingsData rattingData, List<AttributesItemData> attributes, String currencyCode, StatusData status, String reason, boolean hasSubstitute, SubstituteWith substituteWith) {
		this.upcNumber = upcNumber;
		this.color = color;
		this.forcePickImage = forcePickImage;
		this.timestamps = timestamps;
		this.singleUnitPrice = singleUnitPrice;
		this.offerDetails = offerDetails;
		this.noofunits = noofunits;
		this.preparationTime = preparationTime;
		this.productOrderId = productOrderId;
		this.accounting = accounting;
		this.aisle = aisle;
		this.packageType = packageType;
		this.centralProductId = centralProductId;
		this.allowOrderOutOfStock = allowOrderOutOfStock;
		this.shippingDetails = shippingDetails;
		this.unitId = unitId;
		this.invoiceLink = invoiceLink;
		this.barcode = barcode;
		this.productDeliveryFee = productDeliveryFee;
		this.mouData = mouData;
		this.images = images;
		this.brandName = brandName;
		this.quantity = quantity;
		this.productId = productId;
		this.coupon = coupon;
		this.addOns = addOns;
		this.packageId = packageId;
		this.shippingLabel = shippingLabel;
		this.isSplitProduct = isSplitProduct;
		this.currencySymbol = currencySymbol;
		this.packaging = packaging;
		this.shelf = shelf;
		this.isParentProduct = isParentProduct;
		this.prescriptionRequired = prescriptionRequired;
		this.isCentral = isCentral;
		this.directions = directions;
		this.manufactureName = manufactureName;
		this.name = name;
		this.rattingData = rattingData;
		this.attributes = attributes;
		this.currencyCode = currencyCode;
		this.status = status;
		this.reason = reason;
		this.hasSubstitute = hasSubstitute;
		this.substituteWith = substituteWith;
	}

	protected ProductsItemData(Parcel in) {
		upcNumber = in.readString();
		color = in.readString();
		forcePickImage = in.readString();
		timestamps = in.readParcelable(TimestampsData.class.getClassLoader());
		singleUnitPrice = in.readParcelable(SingleUnitPriceData.class.getClassLoader());
		offerDetails = in.readParcelable(OfferDetailsData.class.getClassLoader());
		noofunits = in.readInt();
		preparationTime = in.readInt();
		productOrderId = in.readString();
		accounting = in.readParcelable(AccountingData.class.getClassLoader());
		aisle = in.readString();
		packageType = in.readString();
		centralProductId = in.readString();
		allowOrderOutOfStock = in.readByte() != 0;
		shippingDetails = in.readParcelable(ShippingDetailsData.class.getClassLoader());
		unitId = in.readString();
		invoiceLink = in.readString();
		barcode = in.readString();
		productDeliveryFee = in.readInt();
		images = in.readParcelable(ImagesData.class.getClassLoader());
		brandName = in.readString();
		quantity = in.readParcelable(QuantityData.class.getClassLoader());
		productId = in.readString();
		coupon = in.readString();
		packageId = in.readString();
		shippingLabel = in.readString();
		isSplitProduct = in.readByte() != 0;
		currencySymbol = in.readString();
		packaging = in.readParcelable(PackagingData.class.getClassLoader());
		shelf = in.readString();
		isParentProduct = in.readByte() != 0;
		prescriptionRequired = in.readByte() != 0;
		isCentral = in.readByte() != 0;
		directions = in.readString();
		manufactureName = in.readString();
		name = in.readString();
		reason = in.readString();
		rattingData = in.readParcelable(RattingsData.class.getClassLoader());
		attributes = in.createTypedArrayList(AttributesItemData.CREATOR);
		currencyCode = in.readString();
		status = in.readParcelable(StatusData.class.getClassLoader());
		productType = in.readInt();
		hasSubstitute = in.readByte() != 0;
		substituteWith = in.readParcelable(SubstituteWith.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(upcNumber);
		dest.writeString(color);
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
		dest.writeInt(productType);
		dest.writeByte((byte) (hasSubstitute ? 1 : 0));
		dest.writeParcelable(substituteWith, flags);
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

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
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

	public void setTimestamps(TimestampsData timestamps){
		this.timestamps = timestamps;
	}

	public TimestampsData getTimestamps(){
		return timestamps;
	}

	public void setSingleUnitPrice(SingleUnitPriceData singleUnitPrice){
		this.singleUnitPrice = singleUnitPrice;
	}

	public SingleUnitPriceData getSingleUnitPrice(){
		return singleUnitPrice;
	}

	public void setOfferDetails(OfferDetailsData offerDetails){
		this.offerDetails = offerDetails;
	}

	public OfferDetailsData getOfferDetails(){
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

	public void setAccounting(AccountingData accounting){
		this.accounting = accounting;
	}

	public AccountingData getAccounting(){
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

	public void setShippingDetails(ShippingDetailsData shippingDetails){
		this.shippingDetails = shippingDetails;
	}

	public ShippingDetailsData getShippingDetails(){
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

	public void setMouData(MousData mouData){
		this.mouData = mouData;
	}

	public MousData getMouData(){
		return mouData;
	}

	public void setImages(ImagesData images){
		this.images = images;
	}

	public ImagesData getImages(){
		return images;
	}

	public void setBrandName(String brandName){
		this.brandName = brandName;
	}

	public String getBrandName(){
		return brandName;
	}

	public void setQuantity(QuantityData quantity){
		this.quantity = quantity;
	}

	public QuantityData getQuantity(){
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

	public void setPackaging(PackagingData packaging){
		this.packaging = packaging;
	}

	public PackagingData getPackaging(){
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

	public void setRattingData(RattingsData rattingData){
		this.rattingData = rattingData;
	}

	public RattingsData getRattingData(){
		return rattingData;
	}

	public void setAttributes(List<AttributesItemData> attributes){
		this.attributes = attributes;
	}

	public List<AttributesItemData> getAttributes(){
		return attributes;
	}

	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public void setStatus(StatusData status){
		this.status = status;
	}

	public StatusData getStatus(){
		return status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isHasSubstitute() {
		return hasSubstitute;
	}

	public void setHasSubstitute(boolean hasSubstitute) {
		this.hasSubstitute = hasSubstitute;
	}

	public SubstituteWith getSubstituteWith() {
		return substituteWith;
	}

	public void setSubstituteWith(SubstituteWith substituteWith) {
		this.substituteWith = substituteWith;
	}
}