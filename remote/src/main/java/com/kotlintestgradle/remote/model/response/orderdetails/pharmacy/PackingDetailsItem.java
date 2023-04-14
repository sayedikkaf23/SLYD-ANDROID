package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.model.response.orderdetails.PackageBoxDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.PartnerDetails;

import java.util.ArrayList;
import java.util.List;

public class PackingDetailsItem implements Parcelable, ValidItem {

	@Expose
	@SerializedName("requestedFor")
	private String requestedFor;

	@Expose
	@SerializedName("deliverySlotId")
	private String deliverySlotId;

	@Expose
	@SerializedName("orderType")
	private int orderType;

	@Expose
	@SerializedName("receciptURL")
	private List<String> receciptURL;

	@Expose
	@SerializedName("orderTypeMsg")
	private String orderTypeMsg;

	@Expose
	@SerializedName("partnerDetails")
	private PartnerDetails partnerDetails;

	@Expose
	@SerializedName("accounting")
	private Accounting accounting;

	@Expose
	@SerializedName("requestedForPickupTimeStamp")
	private int requestedForPickupTimeStamp;

	@Expose
	@SerializedName("requestedForPickup")
	private String requestedForPickup;

	@Expose
	@SerializedName("deliveryAddress")
	private DeliveryAddress deliveryAddress;

	@Expose
	@SerializedName("bookingType")
	private int bookingType;

	@Expose
	@SerializedName("storePhone")
	private String storePhone;

	@Expose
	@SerializedName("packageBox")
	private PackageBoxDetails packageBox;

	@Expose
	@SerializedName("bookingTypeMsg")
	private String bookingTypeMsg;

	@Expose
	@SerializedName("deliverySlotDetails")
	private DeliverySlotDetails deliverySlotDetails;

	@Expose
	@SerializedName("pickupSlotId")
	private String pickupSlotId;

	@Expose
	@SerializedName("shippingLabel")
	private String shippingLabel;

	@Expose
	@SerializedName("driverDetails")
	private DriverDetails driverDetails;

	@Expose
	@SerializedName("storeTypeMsg")
	private String storeTypeMsg;

	@Expose
	@SerializedName("storeOrderId")
	private String storeOrderId;

	@Expose
	@SerializedName("storeCategoryId")
	private String storeCategoryId;

	@Expose
	@SerializedName("pickupAddress")
	private PickupAddress pickupAddress;

	@Expose
	@SerializedName("storeEmail")
	private String storeEmail;

	@Expose
	@SerializedName("_id")
	private String id;

	@Expose
	@SerializedName("sellerType")
	private int sellerType;

	@Expose
	@SerializedName("status")
	private Status status;

	@Expose
	@SerializedName("paymentTypeText")
	private String paymentTypeText;

	@Expose
	@SerializedName("driverTypeId")
	private int driverTypeId;

	@Expose
	@SerializedName("bags")
	private ArrayList<BagsItem> bags;

	@Expose
	@SerializedName("timestamps")
	private Timestamps timestamps;

	@Expose
	@SerializedName("productOrderIds")
	private List<String> productOrderIds;

	@Expose
	@SerializedName("createdTimeStamp")
	private int createdTimeStamp;

	@Expose
	@SerializedName("contactLessDelivery")
	private boolean contactLessDelivery;

	@Expose
	@SerializedName("paymentType")
	private int paymentType;

	@Expose
	@SerializedName("requestedForTimeStamp")
	private int requestedForTimeStamp;

	@Expose
	@SerializedName("autoDispatch")
	private boolean autoDispatch;

	@Expose
	@SerializedName("customerId")
	private String customerId;

	@Expose
	@SerializedName("storeName")
	private String storeName;

	@Expose
	@SerializedName("customerDetails")
	private CustomerDetails customerDetails;

	@Expose
	@SerializedName("invoiceLink")
	private String invoiceLink;

	@Expose
	@SerializedName("payByWallet")
	private boolean payByWallet;

	@Expose
	@SerializedName("storeType")
	private int storeType;

	@Expose
	@SerializedName("vehicleDetails")
	private VehicleDetails vehicleDetails;

	@Expose
	@SerializedName("sellerTypeMsg")
	private String sellerTypeMsg;

	@Expose
	@SerializedName("packageId")
	private String packageId;

	@Expose
	@SerializedName("storeCategory")
	private String storeCategory;

	@Expose
	@SerializedName("storeId")
	private String storeId;

	@Expose
	@SerializedName("driverType")
	private String driverType;

	@Expose
	@SerializedName("masterOrderId")
	private String masterOrderId;

	@Expose
	@SerializedName("createdDate")
	private String createdDate;

	@Expose
	@SerializedName("contactLessDeliveryReason")
	private String contactLessDeliveryReason;

	@Expose
	@SerializedName("storeLogo")
	private StoreLogo storeLogo;

	@Expose
	@SerializedName("billingAddress")
	private BillingAddress billingAddress;

	@Expose
	@SerializedName("pickupSlotDetails")
	private PickupSlotDetails pickupSlotDetails;

	@Expose
	@SerializedName("storePlan")
	private StorePlan storePlan;

	protected PackingDetailsItem(Parcel in) {
		requestedFor = in.readString();
		deliverySlotId = in.readString();
		orderType = in.readInt();
		receciptURL = in.createStringArrayList();
		orderTypeMsg = in.readString();
		partnerDetails = in.readParcelable(PartnerDetails.class.getClassLoader());
		accounting = in.readParcelable(Accounting.class.getClassLoader());
		requestedForPickupTimeStamp = in.readInt();
		requestedForPickup = in.readString();
		deliveryAddress = in.readParcelable(DeliveryAddress.class.getClassLoader());
		bookingType = in.readInt();
		storePhone = in.readString();
		packageBox = in.readParcelable(PackageBoxDetails.class.getClassLoader());
		bookingTypeMsg = in.readString();
		deliverySlotDetails = in.readParcelable(DeliverySlotDetails.class.getClassLoader());
		pickupSlotId = in.readString();
		shippingLabel = in.readString();
		driverDetails = in.readParcelable(DriverDetails.class.getClassLoader());
		storeTypeMsg = in.readString();
		storeOrderId = in.readString();
		storeCategoryId = in.readString();
		pickupAddress = in.readParcelable(PickupAddress.class.getClassLoader());
		storeEmail = in.readString();
		id = in.readString();
		sellerType = in.readInt();
		status = in.readParcelable(Status.class.getClassLoader());
		paymentTypeText = in.readString();
		driverTypeId = in.readInt();
		bags = in.createTypedArrayList(BagsItem.CREATOR);
		timestamps = in.readParcelable(Timestamps.class.getClassLoader());
		productOrderIds = in.createStringArrayList();
		createdTimeStamp = in.readInt();
		contactLessDelivery = in.readByte() != 0;
		paymentType = in.readInt();
		requestedForTimeStamp = in.readInt();
		autoDispatch = in.readByte() != 0;
		customerId = in.readString();
		storeName = in.readString();
		customerDetails = in.readParcelable(CustomerDetails.class.getClassLoader());
		invoiceLink = in.readString();
		payByWallet = in.readByte() != 0;
		storeType = in.readInt();
		vehicleDetails = in.readParcelable(VehicleDetails.class.getClassLoader());
		sellerTypeMsg = in.readString();
		packageId = in.readString();
		storeCategory = in.readString();
		storeId = in.readString();
		driverType = in.readString();
		masterOrderId = in.readString();
		createdDate = in.readString();
		contactLessDeliveryReason = in.readString();
		storeLogo = in.readParcelable(StoreLogo.class.getClassLoader());
		billingAddress = in.readParcelable(BillingAddress.class.getClassLoader());
		pickupSlotDetails = in.readParcelable(PickupSlotDetails.class.getClassLoader());
		storePlan = in.readParcelable(StorePlan.class.getClassLoader());
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
		dest.writeParcelable(deliverySlotDetails, flags);
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
		dest.writeParcelable(pickupSlotDetails, flags);
		dest.writeParcelable(storePlan, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PackingDetailsItem> CREATOR = new Creator<PackingDetailsItem>() {
		@Override
		public PackingDetailsItem createFromParcel(Parcel in) {
			return new PackingDetailsItem(in);
		}

		@Override
		public PackingDetailsItem[] newArray(int size) {
			return new PackingDetailsItem[size];
		}
	};

	public void setRequestedFor(String requestedFor) {
		this.requestedFor = requestedFor;
	}

	public void setDeliverySlotId(String deliverySlotId) {
		this.deliverySlotId = deliverySlotId;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public void setRececiptURL(List<String> receciptURL) {
		this.receciptURL = receciptURL;
	}

	public void setOrderTypeMsg(String orderTypeMsg) {
		this.orderTypeMsg = orderTypeMsg;
	}

	public void setPartnerDetails(PartnerDetails partnerDetails) {
		this.partnerDetails = partnerDetails;
	}

	public void setAccounting(Accounting accounting) {
		this.accounting = accounting;
	}

	public void setRequestedForPickupTimeStamp(int requestedForPickupTimeStamp) {
		this.requestedForPickupTimeStamp = requestedForPickupTimeStamp;
	}

	public void setRequestedForPickup(String requestedForPickup) {
		this.requestedForPickup = requestedForPickup;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setBookingType(int bookingType) {
		this.bookingType = bookingType;
	}

	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}

	public void setPackageBox(PackageBoxDetails packageBox) {
		this.packageBox = packageBox;
	}

	public void setBookingTypeMsg(String bookingTypeMsg) {
		this.bookingTypeMsg = bookingTypeMsg;
	}

	public void setDeliverySlotDetails(DeliverySlotDetails deliverySlotDetails) {
		this.deliverySlotDetails = deliverySlotDetails;
	}

	public void setPickupSlotId(String pickupSlotId) {
		this.pickupSlotId = pickupSlotId;
	}

	public void setShippingLabel(String shippingLabel) {
		this.shippingLabel = shippingLabel;
	}

	public void setDriverDetails(DriverDetails driverDetails) {
		this.driverDetails = driverDetails;
	}

	public void setStoreTypeMsg(String storeTypeMsg) {
		this.storeTypeMsg = storeTypeMsg;
	}

	public void setStoreOrderId(String storeOrderId) {
		this.storeOrderId = storeOrderId;
	}

	public void setStoreCategoryId(String storeCategoryId) {
		this.storeCategoryId = storeCategoryId;
	}

	public void setPickupAddress(PickupAddress pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public void setStoreEmail(String storeEmail) {
		this.storeEmail = storeEmail;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSellerType(int sellerType) {
		this.sellerType = sellerType;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setPaymentTypeText(String paymentTypeText) {
		this.paymentTypeText = paymentTypeText;
	}

	public void setDriverTypeId(int driverTypeId) {
		this.driverTypeId = driverTypeId;
	}

	public void setBags(ArrayList<BagsItem> bags) {
		this.bags = bags;
	}

	public void setTimestamps(Timestamps timestamps) {
		this.timestamps = timestamps;
	}

	public void setProductOrderIds(List<String> productOrderIds) {
		this.productOrderIds = productOrderIds;
	}

	public void setCreatedTimeStamp(int createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public void setContactLessDelivery(boolean contactLessDelivery) {
		this.contactLessDelivery = contactLessDelivery;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public void setRequestedForTimeStamp(int requestedForTimeStamp) {
		this.requestedForTimeStamp = requestedForTimeStamp;
	}

	public void setAutoDispatch(boolean autoDispatch) {
		this.autoDispatch = autoDispatch;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	public void setInvoiceLink(String invoiceLink) {
		this.invoiceLink = invoiceLink;
	}

	public void setPayByWallet(boolean payByWallet) {
		this.payByWallet = payByWallet;
	}

	public void setStoreType(int storeType) {
		this.storeType = storeType;
	}

	public void setVehicleDetails(VehicleDetails vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}

	public void setSellerTypeMsg(String sellerTypeMsg) {
		this.sellerTypeMsg = sellerTypeMsg;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public void setStoreCategory(String storeCategory) {
		this.storeCategory = storeCategory;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public void setMasterOrderId(String masterOrderId) {
		this.masterOrderId = masterOrderId;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setContactLessDeliveryReason(String contactLessDeliveryReason) {
		this.contactLessDeliveryReason = contactLessDeliveryReason;
	}

	public void setStoreLogo(StoreLogo storeLogo) {
		this.storeLogo = storeLogo;
	}

	public void setBillingAddress(BillingAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	public void setPickupSlotDetails(PickupSlotDetails pickupSlotDetails) {
		this.pickupSlotDetails = pickupSlotDetails;
	}

	public void setStorePlan(StorePlan storePlan) {
		this.storePlan = storePlan;
	}

	public String getRequestedFor(){
		return requestedFor;
	}

	public String getDeliverySlotId(){
		return deliverySlotId;
	}

	public int getOrderType(){
		return orderType;
	}

	public List<String> getRececiptURL(){
		return receciptURL;
	}

	public String getOrderTypeMsg(){
		return orderTypeMsg;
	}

	public PartnerDetails getPartnerDetails(){
		return partnerDetails;
	}

	public Accounting getAccounting(){
		return accounting;
	}

	public int getRequestedForPickupTimeStamp(){
		return requestedForPickupTimeStamp;
	}

	public String getRequestedForPickup(){
		return requestedForPickup;
	}

	public DeliveryAddress getDeliveryAddress(){
		return deliveryAddress;
	}

	public int getBookingType(){
		return bookingType;
	}

	public String getStorePhone(){
		return storePhone;
	}

	public PackageBoxDetails getPackageBox(){
		return packageBox;
	}

	public String getBookingTypeMsg(){
		return bookingTypeMsg;
	}

	public DeliverySlotDetails getDeliverySlotDetails(){
		return deliverySlotDetails;
	}

	public String getPickupSlotId(){
		return pickupSlotId;
	}

	public String getShippingLabel(){
		return shippingLabel;
	}

	public DriverDetails getDriverDetails(){
		return driverDetails;
	}

	public String getStoreTypeMsg(){
		return storeTypeMsg;
	}

	public String getStoreOrderId(){
		return storeOrderId;
	}

	public String getStoreCategoryId(){
		return storeCategoryId;
	}

	public PickupAddress getPickupAddress(){
		return pickupAddress;
	}

	public String getStoreEmail(){
		return storeEmail;
	}

	public String getId(){
		return id;
	}

	public int getSellerType(){
		return sellerType;
	}

	public Status getStatus(){
		return status;
	}

	public String getPaymentTypeText(){
		return paymentTypeText;
	}

	public int getDriverTypeId(){
		return driverTypeId;
	}

	public ArrayList<BagsItem> getBags(){
		return bags;
	}

	public Timestamps getTimestamps(){
		return timestamps;
	}

	public List<String> getProductOrderIds(){
		return productOrderIds;
	}

	public int getCreatedTimeStamp(){
		return createdTimeStamp;
	}

	public boolean isContactLessDelivery(){
		return contactLessDelivery;
	}

	public int getPaymentType(){
		return paymentType;
	}

	public int getRequestedForTimeStamp(){
		return requestedForTimeStamp;
	}

	public boolean isAutoDispatch(){
		return autoDispatch;
	}

	public String getCustomerId(){
		return customerId;
	}

	public String getStoreName(){
		return storeName;
	}

	public CustomerDetails getCustomerDetails(){
		return customerDetails;
	}

	public String getInvoiceLink(){
		return invoiceLink;
	}

	public boolean isPayByWallet(){
		return payByWallet;
	}

	public int getStoreType(){
		return storeType;
	}

	public VehicleDetails getVehicleDetails(){
		return vehicleDetails;
	}

	public String getSellerTypeMsg(){
		return sellerTypeMsg;
	}

	public String getPackageId(){
		return packageId;
	}

	public String getStoreCategory(){
		return storeCategory;
	}

	public String getStoreId(){
		return storeId;
	}

	public String getDriverType(){
		return driverType;
	}

	public String getMasterOrderId(){
		return masterOrderId;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public String getContactLessDeliveryReason(){
		return contactLessDeliveryReason;
	}

	public StoreLogo getStoreLogo(){
		return storeLogo;
	}

	public BillingAddress getBillingAddress(){
		return billingAddress;
	}

	public PickupSlotDetails getPickupSlotDetails(){
		return pickupSlotDetails;
	}

	public StorePlan getStorePlan(){
		return storePlan;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}