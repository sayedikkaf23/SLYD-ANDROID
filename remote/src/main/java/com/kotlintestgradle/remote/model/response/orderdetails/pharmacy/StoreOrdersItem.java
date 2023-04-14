package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.remote.core.ValidItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StoreOrdersItem implements Parcelable, ValidItem {

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
	@SerializedName("accounting")
	private Accounting accounting;

	@Expose
	@SerializedName("customerPaymentTypeMsg")
	private String customerPaymentTypeMsg;

	@Expose
	@SerializedName("requestedForPickupTimeStamp")
	private int requestedForPickupTimeStamp;

	@Expose
	@SerializedName("requestedForPickup")
	private String requestedForPickup;

	@Expose
	@SerializedName("storeTaxId")
	private String storeTaxId;

	@Expose
	@SerializedName("deliveryAddress")
	private DeliveryAddress deliveryAddress;

	@Expose
	@SerializedName("poInvoiceLink")
	private String poInvoiceLink;

	@Expose
	@SerializedName("bookingType")
	private int bookingType;

	@Expose
	@SerializedName("count")
	private Count count;

	@SerializedName("storePhone")
	@Expose
	private String storePhone;

	@Expose
	@SerializedName("reminderPreference")
	private List<Object> reminderPreference;

	@Expose
	@SerializedName("deliverySlotDetails")
	private DeliverySlotDetails deliverySlotDetails;

	@Expose
	@SerializedName("cartId")
	private String cartId;

	@Expose
	@SerializedName("pickupSlotId")
	private String pickupSlotId;

	@Expose
	@SerializedName("bookingTypeText")
	private String bookingTypeText;

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
	@SerializedName("autoAcceptOrders")
	private boolean autoAcceptOrders;

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
	@SerializedName("walletDetails")
	private WalletDetails walletDetails;

	@Expose
	@SerializedName("paymentTypeText")
	private String paymentTypeText;

	@Expose
	@SerializedName("driverTypeId")
	private int driverTypeId;

	@Expose
	@SerializedName("bags")
	private List<Object> bags;

	@Expose
	@SerializedName("timestamps")
	private Timestamps timestamps;

	@Expose
	@SerializedName("extraNote")
	private String extraNote;

	@Expose
	@SerializedName("createdTimeStamp")
	private int createdTimeStamp;

	@Expose
	@SerializedName("contactLessDelivery")
	private boolean contactLessDelivery;

	@Expose
	@SerializedName("customerPaymentType")
	private int customerPaymentType;

	@Expose
	@SerializedName("paymentType")
	private int paymentType;

	@Expose
	@SerializedName("products")
	private List<ProductsItem> products;

	@Expose
	@SerializedName("requestedForTimeStamp")
	private int requestedForTimeStamp;

	@Expose
	@SerializedName("orderImages")
	private List<String> orderImages;

	@Expose
	@SerializedName("autoDispatch")
	private boolean autoDispatch;

	@Expose
	@SerializedName("cardDetails")
	private CardDetails cardDetails;

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
	@SerializedName("freeDeliveryLimitPerStore")
	private int freeDeliveryLimitPerStore;

	@Expose
	@SerializedName("payByWallet")
	private boolean payByWallet;

	@Expose
	@SerializedName("storeType")
	private int storeType;

	@Expose
	@SerializedName("coupon")
	private String coupon;

	@Expose
	@SerializedName("sellerTypeMsg")
	private String sellerTypeMsg;

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
	@SerializedName("packingDetails")
	private ArrayList<PackingDetailsItem> packingDetails;

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

	@Expose
	@SerializedName("pickerDetails")
	private PickerDetaisl pickerDetaisl;

	protected StoreOrdersItem(Parcel in) {
		requestedFor = in.readString();
		deliverySlotId = in.readString();
		orderType = in.readInt();
		receciptURL = in.createStringArrayList();
		orderTypeMsg = in.readString();
		accounting = in.readParcelable(Accounting.class.getClassLoader());
		customerPaymentTypeMsg = in.readString();
		requestedForPickupTimeStamp = in.readInt();
		requestedForPickup = in.readString();
		storeTaxId = in.readString();
		deliveryAddress = in.readParcelable(DeliveryAddress.class.getClassLoader());
		poInvoiceLink = in.readString();
		bookingType = in.readInt();
		count = in.readParcelable(Count.class.getClassLoader());
		storePhone = in.readString();
		deliverySlotDetails = in.readParcelable(DeliverySlotDetails.class.getClassLoader());
		cartId = in.readString();
		pickupSlotId = in.readString();
		bookingTypeText = in.readString();
		storeTypeMsg = in.readString();
		storeOrderId = in.readString();
		storeCategoryId = in.readString();
		autoAcceptOrders = in.readByte() != 0;
		pickupAddress = in.readParcelable(PickupAddress.class.getClassLoader());
		storeEmail = in.readString();
		id = in.readString();
		sellerType = in.readInt();
		status = in.readParcelable(Status.class.getClassLoader());
		walletDetails = in.readParcelable(WalletDetails.class.getClassLoader());
		paymentTypeText = in.readString();
		driverTypeId = in.readInt();
		timestamps = in.readParcelable(Timestamps.class.getClassLoader());
		extraNote = in.readString();
		createdTimeStamp = in.readInt();
		contactLessDelivery = in.readByte() != 0;
		customerPaymentType = in.readInt();
		paymentType = in.readInt();
		products = in.createTypedArrayList(ProductsItem.CREATOR);
		requestedForTimeStamp = in.readInt();
		orderImages = in.createStringArrayList();
		autoDispatch = in.readByte() != 0;
		cardDetails = in.readParcelable(CardDetails.class.getClassLoader());
		customerId = in.readString();
		storeName = in.readString();
		customerDetails = in.readParcelable(CustomerDetails.class.getClassLoader());
		freeDeliveryLimitPerStore = in.readInt();
		payByWallet = in.readByte() != 0;
		storeType = in.readInt();
		coupon = in.readString();
		sellerTypeMsg = in.readString();
		storeCategory = in.readString();
		storeId = in.readString();
		driverType = in.readString();
		masterOrderId = in.readString();
		packingDetails = in.createTypedArrayList(PackingDetailsItem.CREATOR);
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
		dest.writeParcelable(accounting, flags);
		dest.writeString(customerPaymentTypeMsg);
		dest.writeInt(requestedForPickupTimeStamp);
		dest.writeString(requestedForPickup);
		dest.writeString(storeTaxId);
		dest.writeParcelable(deliveryAddress, flags);
		dest.writeString(poInvoiceLink);
		dest.writeInt(bookingType);
		dest.writeParcelable(count, flags);
		dest.writeString(storePhone);
		dest.writeParcelable(deliverySlotDetails, flags);
		dest.writeString(cartId);
		dest.writeString(pickupSlotId);
		dest.writeString(bookingTypeText);
		dest.writeString(storeTypeMsg);
		dest.writeString(storeOrderId);
		dest.writeString(storeCategoryId);
		dest.writeByte((byte) (autoAcceptOrders ? 1 : 0));
		dest.writeParcelable(pickupAddress, flags);
		dest.writeString(storeEmail);
		dest.writeString(id);
		dest.writeInt(sellerType);
		dest.writeParcelable(status, flags);
		dest.writeParcelable(walletDetails, flags);
		dest.writeString(paymentTypeText);
		dest.writeInt(driverTypeId);
		dest.writeParcelable(timestamps, flags);
		dest.writeString(extraNote);
		dest.writeInt(createdTimeStamp);
		dest.writeByte((byte) (contactLessDelivery ? 1 : 0));
		dest.writeInt(customerPaymentType);
		dest.writeInt(paymentType);
		dest.writeTypedList(products);
		dest.writeInt(requestedForTimeStamp);
		dest.writeStringList(orderImages);
		dest.writeByte((byte) (autoDispatch ? 1 : 0));
		dest.writeParcelable(cardDetails, flags);
		dest.writeString(customerId);
		dest.writeString(storeName);
		dest.writeParcelable(customerDetails, flags);
		dest.writeInt(freeDeliveryLimitPerStore);
		dest.writeByte((byte) (payByWallet ? 1 : 0));
		dest.writeInt(storeType);
		dest.writeString(coupon);
		dest.writeString(sellerTypeMsg);
		dest.writeString(storeCategory);
		dest.writeString(storeId);
		dest.writeString(driverType);
		dest.writeString(masterOrderId);
		dest.writeTypedList(packingDetails);
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

	public static final Creator<StoreOrdersItem> CREATOR = new Creator<StoreOrdersItem>() {
		@Override
		public StoreOrdersItem createFromParcel(Parcel in) {
			return new StoreOrdersItem(in);
		}

		@Override
		public StoreOrdersItem[] newArray(int size) {
			return new StoreOrdersItem[size];
		}
	};

	public ArrayList<PackingDetailsItem> getPackingDetails() {
		return packingDetails;
	}

	public void setPackingDetails(ArrayList<PackingDetailsItem> packingDetails) {
		this.packingDetails = packingDetails;
	}

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

	public void setAccounting(Accounting accounting){
		this.accounting = accounting;
	}

	public Accounting getAccounting(){
		return accounting;
	}

	public void setCustomerPaymentTypeMsg(String customerPaymentTypeMsg){
		this.customerPaymentTypeMsg = customerPaymentTypeMsg;
	}

	public String getCustomerPaymentTypeMsg(){
		return customerPaymentTypeMsg;
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

	public void setStoreTaxId(String storeTaxId){
		this.storeTaxId = storeTaxId;
	}

	public String getStoreTaxId(){
		return storeTaxId;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress){
		this.deliveryAddress = deliveryAddress;
	}

	public DeliveryAddress getDeliveryAddress(){
		return deliveryAddress;
	}

	public void setPoInvoiceLink(String poInvoiceLink){
		this.poInvoiceLink = poInvoiceLink;
	}

	public String getPoInvoiceLink(){
		return poInvoiceLink;
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

	public void setReminderPreference(List<Object> reminderPreference){
		this.reminderPreference = reminderPreference;
	}

	public List<Object> getReminderPreference(){
		return reminderPreference;
	}

	public void setDeliverySlotDetails(DeliverySlotDetails deliverySlotDetails){
		this.deliverySlotDetails = deliverySlotDetails;
	}

	public DeliverySlotDetails getDeliverySlotDetails(){
		return deliverySlotDetails;
	}

	public void setCartId(String cartId){
		this.cartId = cartId;
	}

	public String getCartId(){
		return cartId;
	}

	public void setPickupSlotId(String pickupSlotId){
		this.pickupSlotId = pickupSlotId;
	}

	public String getPickupSlotId(){
		return pickupSlotId;
	}

	public void setBookingTypeText(String bookingTypeText){
		this.bookingTypeText = bookingTypeText;
	}

	public String getBookingTypeText(){
		return bookingTypeText;
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

	public void setAutoAcceptOrders(boolean autoAcceptOrders){
		this.autoAcceptOrders = autoAcceptOrders;
	}

	public boolean isAutoAcceptOrders(){
		return autoAcceptOrders;
	}

	public void setPickupAddress(PickupAddress pickupAddress){
		this.pickupAddress = pickupAddress;
	}

	public PickupAddress getPickupAddress(){
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

	public void setStatus(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	public void setWalletDetails(WalletDetails walletDetails){
		this.walletDetails = walletDetails;
	}

	public WalletDetails getWalletDetails(){
		return walletDetails;
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

	public void setBags(List<Object> bags){
		this.bags = bags;
	}

	public List<Object> getBags(){
		return bags;
	}

	public void setTimestamps(Timestamps timestamps){
		this.timestamps = timestamps;
	}

	public Timestamps getTimestamps(){
		return timestamps;
	}

	public void setExtraNote(String extraNote){
		this.extraNote = extraNote;
	}

	public String getExtraNote(){
		return extraNote;
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

	public void setCustomerPaymentType(int customerPaymentType){
		this.customerPaymentType = customerPaymentType;
	}

	public int getCustomerPaymentType(){
		return customerPaymentType;
	}

	public void setPaymentType(int paymentType){
		this.paymentType = paymentType;
	}

	public int getPaymentType(){
		return paymentType;
	}

	public void setProducts(List<ProductsItem> products){
		this.products = products;
	}

	public List<ProductsItem> getProducts(){
		return products;
	}

	public void setRequestedForTimeStamp(int requestedForTimeStamp){
		this.requestedForTimeStamp = requestedForTimeStamp;
	}

	public int getRequestedForTimeStamp(){
		return requestedForTimeStamp;
	}

	public void setOrderImages(List<String> orderImages){
		this.orderImages = orderImages;
	}

	public List<String> getOrderImages(){
		return orderImages;
	}

	public void setAutoDispatch(boolean autoDispatch){
		this.autoDispatch = autoDispatch;
	}

	public boolean getAutoDispatch(){
		return autoDispatch;
	}

	public void setCardDetails(CardDetails cardDetails){
		this.cardDetails = cardDetails;
	}

	public CardDetails getCardDetails(){
		return cardDetails;
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

	public void setCustomerDetails(CustomerDetails customerDetails){
		this.customerDetails = customerDetails;
	}

	public CustomerDetails getCustomerDetails(){
		return customerDetails;
	}

	public void setFreeDeliveryLimitPerStore(int freeDeliveryLimitPerStore){
		this.freeDeliveryLimitPerStore = freeDeliveryLimitPerStore;
	}

	public int getFreeDeliveryLimitPerStore(){
		return freeDeliveryLimitPerStore;
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

	public void setCoupon(String coupon){
		this.coupon = coupon;
	}

	public String getCoupon(){
		return coupon;
	}

	public void setSellerTypeMsg(String sellerTypeMsg){
		this.sellerTypeMsg = sellerTypeMsg;
	}

	public String getSellerTypeMsg(){
		return sellerTypeMsg;
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

	public void setStoreLogo(StoreLogo storeLogo){
		this.storeLogo = storeLogo;
	}

	public StoreLogo getStoreLogo(){
		return storeLogo;
	}

	public void setBillingAddress(BillingAddress billingAddress){
		this.billingAddress = billingAddress;
	}

	public BillingAddress getBillingAddress(){
		return billingAddress;
	}

	public void setPickupSlotDetails(PickupSlotDetails pickupSlotDetails){
		this.pickupSlotDetails = pickupSlotDetails;
	}

	public Count getCount() {
		return count;
	}

	public void setCount(Count count) {
		this.count = count;
	}

	public boolean isAutoDispatch() {
		return autoDispatch;
	}

	public PickupSlotDetails getPickupSlotDetails(){
		return pickupSlotDetails;
	}

	public void setStorePlan(StorePlan storePlan){
		this.storePlan = storePlan;
	}

	public StorePlan getStorePlan(){
		return storePlan;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public PickerDetaisl getPickerDetaisl() {
		return pickerDetaisl;
	}

	public void setPickerDetaisl(PickerDetaisl pickerDetaisl) {
		this.pickerDetaisl = pickerDetaisl;
	}
}