package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;


import com.kotlintestgradle.model.orderdetails.PackageBoxData;
import com.kotlintestgradle.model.orderdetails.PartnerData;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;

import java.util.List;

public class PackingDetailItem implements Parcelable {
	private String requestedFor;
	private String deliverySlotId;
	private int orderType;
	private List<String> receciptURL;
	private String orderTypeMsg;
	private PartnerData partnerDetails;
	private AccountingData accounting;
	private int requestedForPickupTimeStamp;
	private String requestedForPickup;
	private DeliveryAddressData deliveryAddress;
	private int bookingType;
	private String storePhone;
	private PackageBoxData packageBox;
	private String bookingTypeMsg;
	private Object deliverySlotDetails;
	private String pickupSlotId;
	private String shippingLabel;
	private DriverDetailsData driverDetails;
	private String storeTypeMsg;
	private String storeOrderId;
	private String storeCategoryId;
	private PickupAddressData pickupAddress;
	private String storeEmail;
	private String id;
	private int sellerType;
	private StatusData status;
	private String paymentTypeText;
	private int driverTypeId;
	private List<BagsItemData> bags;
	private TimestampsData timestamps;
	private List<String> productOrderIds;
	private int createdTimeStamp;
	private boolean contactLessDelivery;
	private int paymentType;
	private int requestedForTimeStamp;
	private boolean autoDispatch;
	private String customerId;
	private String storeName;
	private CustomerDetailsData customerDetails;
	private String invoiceLink;
	private boolean payByWallet;
	private int storeType;
	private VehicleDetailsData vehicleDetails;
	private String sellerTypeMsg;
	private String packageId;
	private String storeCategory;
	private String storeId;
	private String driverType;
	private String masterOrderId;
	private String createdDate;
	private String contactLessDeliveryReason;
	private StoreLogoData storeLogo;
	private BillingAddressData billingAddress;
	private Object pickupSlotDetails;
	private StorePlanData storePlan;
	private List<OrderHistProductData> products;
	public PackingDetailItem(String requestedFor, String deliverySlotId, int orderType, List<String> receciptURL, String orderTypeMsg, PartnerData partnerDetails, AccountingData accounting, int requestedForPickupTimeStamp, String requestedForPickup, DeliveryAddressData deliveryAddress, int bookingType, String storePhone, PackageBoxData packageBox, String bookingTypeMsg, Object deliverySlotDetails, String pickupSlotId, String shippingLabel, DriverDetailsData driverDetails, String storeTypeMsg, String storeOrderId, String storeCategoryId, PickupAddressData pickupAddress, String storeEmail, String id, int sellerType, StatusData status, String paymentTypeText, int driverTypeId, List<BagsItemData> bags, TimestampsData timestamps, List<String> productOrderIds, int createdTimeStamp, boolean contactLessDelivery, int paymentType, int requestedForTimeStamp, boolean autoDispatch, String customerId, String storeName, CustomerDetailsData customerDetails, String invoiceLink, boolean payByWallet, int storeType, VehicleDetailsData vehicleDetails, String sellerTypeMsg, String packageId, String storeCategory, String storeId, String driverType, String masterOrderId, String createdDate, String contactLessDeliveryReason, StoreLogoData storeLogo, BillingAddressData billingAddress, Object pickupSlotDetails, StorePlanData storePlan) {
		this.requestedFor = requestedFor;
		this.deliverySlotId = deliverySlotId;
		this.orderType = orderType;
		this.receciptURL = receciptURL;
		this.orderTypeMsg = orderTypeMsg;
		this.partnerDetails = partnerDetails;
		this.accounting = accounting;
		this.requestedForPickupTimeStamp = requestedForPickupTimeStamp;
		this.requestedForPickup = requestedForPickup;
		this.deliveryAddress = deliveryAddress;
		this.bookingType = bookingType;
		this.storePhone = storePhone;
		this.packageBox = packageBox;
		this.bookingTypeMsg = bookingTypeMsg;
		this.deliverySlotDetails = deliverySlotDetails;
		this.pickupSlotId = pickupSlotId;
		this.shippingLabel = shippingLabel;
		this.driverDetails = driverDetails;
		this.storeTypeMsg = storeTypeMsg;
		this.storeOrderId = storeOrderId;
		this.storeCategoryId = storeCategoryId;
		this.pickupAddress = pickupAddress;
		this.storeEmail = storeEmail;
		this.id = id;
		this.sellerType = sellerType;
		this.status = status;
		this.paymentTypeText = paymentTypeText;
		this.driverTypeId = driverTypeId;
		this.bags = bags;
		this.timestamps = timestamps;
		this.productOrderIds = productOrderIds;
		this.createdTimeStamp = createdTimeStamp;
		this.contactLessDelivery = contactLessDelivery;
		this.paymentType = paymentType;
		this.requestedForTimeStamp = requestedForTimeStamp;
		this.autoDispatch = autoDispatch;
		this.customerId = customerId;
		this.storeName = storeName;
		this.customerDetails = customerDetails;
		this.invoiceLink = invoiceLink;
		this.payByWallet = payByWallet;
		this.storeType = storeType;
		this.vehicleDetails = vehicleDetails;
		this.sellerTypeMsg = sellerTypeMsg;
		this.packageId = packageId;
		this.storeCategory = storeCategory;
		this.storeId = storeId;
		this.driverType = driverType;
		this.masterOrderId = masterOrderId;
		this.createdDate = createdDate;
		this.contactLessDeliveryReason = contactLessDeliveryReason;
		this.storeLogo = storeLogo;
		this.billingAddress = billingAddress;
		this.pickupSlotDetails = pickupSlotDetails;
		this.storePlan = storePlan;
	}

	protected PackingDetailItem(Parcel in) {
		requestedFor = in.readString();
		deliverySlotId = in.readString();
		orderType = in.readInt();
		receciptURL = in.createStringArrayList();
		orderTypeMsg = in.readString();
		partnerDetails = in.readParcelable(PartnerDetailsData.class.getClassLoader());
		accounting = in.readParcelable(AccountingData.class.getClassLoader());
		requestedForPickupTimeStamp = in.readInt();
		requestedForPickup = in.readString();
		deliveryAddress = in.readParcelable(DeliveryAddressData.class.getClassLoader());
		bookingType = in.readInt();
		storePhone = in.readString();
		packageBox = in.readParcelable(PackageBoxData.class.getClassLoader());
		bookingTypeMsg = in.readString();
		pickupSlotId = in.readString();
		shippingLabel = in.readString();
		driverDetails = in.readParcelable(DriverDetailsData.class.getClassLoader());
		storeTypeMsg = in.readString();
		storeOrderId = in.readString();
		storeCategoryId = in.readString();
		pickupAddress = in.readParcelable(PickupAddressData.class.getClassLoader());
		storeEmail = in.readString();
		id = in.readString();
		sellerType = in.readInt();
		status = in.readParcelable(StatusData.class.getClassLoader());
		paymentTypeText = in.readString();
		driverTypeId = in.readInt();
		bags = in.createTypedArrayList(BagsItemData.CREATOR);
		timestamps = in.readParcelable(TimestampsData.class.getClassLoader());
		productOrderIds = in.createStringArrayList();
		createdTimeStamp = in.readInt();
		contactLessDelivery = in.readByte() != 0;
		paymentType = in.readInt();
		requestedForTimeStamp = in.readInt();
		autoDispatch = in.readByte() != 0;
		customerId = in.readString();
		storeName = in.readString();
		customerDetails = in.readParcelable(CustomerDetailsData.class.getClassLoader());
		invoiceLink = in.readString();
		payByWallet = in.readByte() != 0;
		storeType = in.readInt();
		vehicleDetails = in.readParcelable(VehicleDetailsData.class.getClassLoader());
		sellerTypeMsg = in.readString();
		packageId = in.readString();
		storeCategory = in.readString();
		storeId = in.readString();
		driverType = in.readString();
		masterOrderId = in.readString();
		createdDate = in.readString();
		contactLessDeliveryReason = in.readString();
		storeLogo = in.readParcelable(StoreLogoData.class.getClassLoader());
		billingAddress = in.readParcelable(BillingAddressData.class.getClassLoader());
		storePlan = in.readParcelable(StorePlanData.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(requestedFor);
		dest.writeString(deliverySlotId);
		dest.writeInt(orderType);
		dest.writeStringList(receciptURL);
		dest.writeString(orderTypeMsg);
		dest.writeParcelable(partnerDetails, flags);
		dest.writeParcelable(accounting, flags);
		dest.writeInt(requestedForPickupTimeStamp);
		dest.writeString(requestedForPickup);
		dest.writeParcelable(deliveryAddress, flags);
		dest.writeInt(bookingType);
		dest.writeString(storePhone);
		dest.writeParcelable(packageBox, flags);
		dest.writeString(bookingTypeMsg);
		dest.writeString(pickupSlotId);
		dest.writeString(shippingLabel);
		dest.writeParcelable(driverDetails, flags);
		dest.writeString(storeTypeMsg);
		dest.writeString(storeOrderId);
		dest.writeString(storeCategoryId);
		dest.writeParcelable(pickupAddress, flags);
		dest.writeString(storeEmail);
		dest.writeString(id);
		dest.writeInt(sellerType);
		dest.writeParcelable(status, flags);
		dest.writeString(paymentTypeText);
		dest.writeInt(driverTypeId);
		dest.writeTypedList(bags);
		dest.writeParcelable(timestamps, flags);
		dest.writeStringList(productOrderIds);
		dest.writeInt(createdTimeStamp);
		dest.writeByte((byte) (contactLessDelivery ? 1 : 0));
		dest.writeInt(paymentType);
		dest.writeInt(requestedForTimeStamp);
		dest.writeByte((byte) (autoDispatch ? 1 : 0));
		dest.writeString(customerId);
		dest.writeString(storeName);
		dest.writeParcelable(customerDetails, flags);
		dest.writeString(invoiceLink);
		dest.writeByte((byte) (payByWallet ? 1 : 0));
		dest.writeInt(storeType);
		dest.writeParcelable(vehicleDetails, flags);
		dest.writeString(sellerTypeMsg);
		dest.writeString(packageId);
		dest.writeString(storeCategory);
		dest.writeString(storeId);
		dest.writeString(driverType);
		dest.writeString(masterOrderId);
		dest.writeString(createdDate);
		dest.writeString(contactLessDeliveryReason);
		dest.writeParcelable(storeLogo, flags);
		dest.writeParcelable(billingAddress, flags);
		dest.writeParcelable(storePlan, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PackingDetailItem> CREATOR = new Creator<PackingDetailItem>() {
		@Override
		public PackingDetailItem createFromParcel(Parcel in) {
			return new PackingDetailItem(in);
		}

		@Override
		public PackingDetailItem[] newArray(int size) {
			return new PackingDetailItem[size];
		}
	};

	public void setRequestedFor(String requestedFor){
		this.requestedFor = requestedFor;
	}

	public String getRequestedFor(){
		return requestedFor;
	}

	public void setDeliverySlotId(String deliverySlotId){
		this.deliverySlotId = deliverySlotId;
	}

	public String getDeliverySlotId(){
		return deliverySlotId;
	}

	public void setOrderType(int orderType){
		this.orderType = orderType;
	}

	public int getOrderType(){
		return orderType;
	}

	public void setRececiptURL(List<String> receciptURL){
		this.receciptURL = receciptURL;
	}

	public List<String> getRececiptURL(){
		return receciptURL;
	}

	public void setOrderTypeMsg(String orderTypeMsg){
		this.orderTypeMsg = orderTypeMsg;
	}

	public String getOrderTypeMsg(){
		return orderTypeMsg;
	}

	public void setPartnerDetails(PartnerData partnerDetails){
		this.partnerDetails = partnerDetails;
	}

	public PartnerData getPartnerDetails(){
		return partnerDetails;
	}

	public void setAccounting(AccountingData accounting){
		this.accounting = accounting;
	}

	public AccountingData getAccounting(){
		return accounting;
	}

	public void setRequestedForPickupTimeStamp(int requestedForPickupTimeStamp){
		this.requestedForPickupTimeStamp = requestedForPickupTimeStamp;
	}

	public int getRequestedForPickupTimeStamp(){
		return requestedForPickupTimeStamp;
	}

	public void setRequestedForPickup(String requestedForPickup){
		this.requestedForPickup = requestedForPickup;
	}

	public String getRequestedForPickup(){
		return requestedForPickup;
	}

	public void setDeliveryAddress(DeliveryAddressData deliveryAddress){
		this.deliveryAddress = deliveryAddress;
	}

	public DeliveryAddressData getDeliveryAddress(){
		return deliveryAddress;
	}

	public void setBookingType(int bookingType){
		this.bookingType = bookingType;
	}

	public int getBookingType(){
		return bookingType;
	}

	public void setStorePhone(String storePhone){
		this.storePhone = storePhone;
	}

	public String getStorePhone(){
		return storePhone;
	}

	public void setPackageBox(PackageBoxData packageBox){
		this.packageBox = packageBox;
	}

	public PackageBoxData getPackageBox(){
		return packageBox;
	}

	public void setBookingTypeMsg(String bookingTypeMsg){
		this.bookingTypeMsg = bookingTypeMsg;
	}

	public String getBookingTypeMsg(){
		return bookingTypeMsg;
	}

	public void setDeliverySlotDetails(Object deliverySlotDetails){
		this.deliverySlotDetails = deliverySlotDetails;
	}

	public Object getDeliverySlotDetails(){
		return deliverySlotDetails;
	}

	public void setPickupSlotId(String pickupSlotId){
		this.pickupSlotId = pickupSlotId;
	}

	public String getPickupSlotId(){
		return pickupSlotId;
	}

	public void setShippingLabel(String shippingLabel){
		this.shippingLabel = shippingLabel;
	}

	public String getShippingLabel(){
		return shippingLabel;
	}

	public void setDriverDetails(DriverDetailsData driverDetails){
		this.driverDetails = driverDetails;
	}

	public DriverDetailsData getDriverDetails(){
		return driverDetails;
	}

	public void setStoreTypeMsg(String storeTypeMsg){
		this.storeTypeMsg = storeTypeMsg;
	}

	public String getStoreTypeMsg(){
		return storeTypeMsg;
	}

	public void setStoreOrderId(String storeOrderId){
		this.storeOrderId = storeOrderId;
	}

	public String getStoreOrderId(){
		return storeOrderId;
	}

	public void setStoreCategoryId(String storeCategoryId){
		this.storeCategoryId = storeCategoryId;
	}

	public String getStoreCategoryId(){
		return storeCategoryId;
	}

	public void setPickupAddress(PickupAddressData pickupAddress){
		this.pickupAddress = pickupAddress;
	}

	public PickupAddressData getPickupAddress(){
		return pickupAddress;
	}

	public void setStoreEmail(String storeEmail){
		this.storeEmail = storeEmail;
	}

	public String getStoreEmail(){
		return storeEmail;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSellerType(int sellerType){
		this.sellerType = sellerType;
	}

	public int getSellerType(){
		return sellerType;
	}

	public void setStatus(StatusData status){
		this.status = status;
	}

	public StatusData getStatus(){
		return status;
	}

	public void setPaymentTypeText(String paymentTypeText){
		this.paymentTypeText = paymentTypeText;
	}

	public String getPaymentTypeText(){
		return paymentTypeText;
	}

	public void setDriverTypeId(int driverTypeId){
		this.driverTypeId = driverTypeId;
	}

	public int getDriverTypeId(){
		return driverTypeId;
	}

	public void setBags(List<BagsItemData> bags){
		this.bags = bags;
	}

	public List<BagsItemData> getBags(){
		return bags;
	}

	public void setTimestamps(TimestampsData timestamps){
		this.timestamps = timestamps;
	}

	public TimestampsData getTimestamps(){
		return timestamps;
	}

	public void setProductOrderIds(List<String> productOrderIds){
		this.productOrderIds = productOrderIds;
	}

	public List<String> getProductOrderIds(){
		return productOrderIds;
	}

	public void setCreatedTimeStamp(int createdTimeStamp){
		this.createdTimeStamp = createdTimeStamp;
	}

	public int getCreatedTimeStamp(){
		return createdTimeStamp;
	}

	public void setContactLessDelivery(boolean contactLessDelivery){
		this.contactLessDelivery = contactLessDelivery;
	}

	public boolean isContactLessDelivery(){
		return contactLessDelivery;
	}

	public void setPaymentType(int paymentType){
		this.paymentType = paymentType;
	}

	public int getPaymentType(){
		return paymentType;
	}

	public void setRequestedForTimeStamp(int requestedForTimeStamp){
		this.requestedForTimeStamp = requestedForTimeStamp;
	}

	public int getRequestedForTimeStamp(){
		return requestedForTimeStamp;
	}

	public void setAutoDispatch(boolean autoDispatch){
		this.autoDispatch = autoDispatch;
	}

	public boolean isAutoDispatch(){
		return autoDispatch;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public void setStoreName(String storeName){
		this.storeName = storeName;
	}

	public String getStoreName(){
		return storeName;
	}

	public void setCustomerDetails(CustomerDetailsData customerDetails){
		this.customerDetails = customerDetails;
	}

	public CustomerDetailsData getCustomerDetails(){
		return customerDetails;
	}

	public void setInvoiceLink(String invoiceLink){
		this.invoiceLink = invoiceLink;
	}

	public String getInvoiceLink(){
		return invoiceLink;
	}

	public void setPayByWallet(boolean payByWallet){
		this.payByWallet = payByWallet;
	}

	public boolean isPayByWallet(){
		return payByWallet;
	}

	public void setStoreType(int storeType){
		this.storeType = storeType;
	}

	public int getStoreType(){
		return storeType;
	}

	public void setVehicleDetails(VehicleDetailsData vehicleDetails){
		this.vehicleDetails = vehicleDetails;
	}

	public VehicleDetailsData getVehicleDetails(){
		return vehicleDetails;
	}

	public void setSellerTypeMsg(String sellerTypeMsg){
		this.sellerTypeMsg = sellerTypeMsg;
	}

	public String getSellerTypeMsg(){
		return sellerTypeMsg;
	}

	public void setPackageId(String packageId){
		this.packageId = packageId;
	}

	public String getPackageId(){
		return packageId;
	}

	public void setStoreCategory(String storeCategory){
		this.storeCategory = storeCategory;
	}

	public String getStoreCategory(){
		return storeCategory;
	}

	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return storeId;
	}

	public void setDriverType(String driverType){
		this.driverType = driverType;
	}

	public String getDriverType(){
		return driverType;
	}

	public void setMasterOrderId(String masterOrderId){
		this.masterOrderId = masterOrderId;
	}

	public String getMasterOrderId(){
		return masterOrderId;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setContactLessDeliveryReason(String contactLessDeliveryReason){
		this.contactLessDeliveryReason = contactLessDeliveryReason;
	}

	public String getContactLessDeliveryReason(){
		return contactLessDeliveryReason;
	}

	public void setStoreLogo(StoreLogoData storeLogo){
		this.storeLogo = storeLogo;
	}

	public StoreLogoData getStoreLogo(){
		return storeLogo;
	}

	public void setBillingAddress(BillingAddressData billingAddress){
		this.billingAddress = billingAddress;
	}

	public BillingAddressData getBillingAddress(){
		return billingAddress;
	}

	public void setPickupSlotDetails(Object pickupSlotDetails){
		this.pickupSlotDetails = pickupSlotDetails;
	}

	public Object getPickupSlotDetails(){
		return pickupSlotDetails;
	}

	public void setStorePlan(StorePlanData storePlan){
		this.storePlan = storePlan;
	}

	public StorePlanData getStorePlan(){
		return storePlan;
	}

	public List<OrderHistProductData> getProducts() {
		return products;
	}

	public void setProducts(List<OrderHistProductData> products) {
		this.products = products;
	}
}