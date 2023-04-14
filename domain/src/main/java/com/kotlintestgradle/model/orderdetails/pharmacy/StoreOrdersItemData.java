package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.kotlintestgradle.model.orderhistory.PickingData;

import java.util.ArrayList;
import java.util.List;

public class StoreOrdersItemData implements Parcelable {
	private String requestedFor;
	private String deliverySlotId;
	private int orderType;
	private List<String> receciptURL;
	private String orderTypeMsg;
	private AccountingData accounting;
	private String customerPaymentTypeMsg;
	private int requestedForPickupTimeStamp;
	private String requestedForPickup;
	private String storeTaxId;
	private DeliveryAddressData deliveryAddress;
	private String poInvoiceLink;
	private int bookingType;
	private String storePhone;
	private List<Object> reminderPreference;
	private DeliverySlotDetailsData deliverySlotDetails;
	private String cartId;
	private String pickupSlotId;
	private String bookingTypeText;
	private String storeTypeMsg;
	private String storeOrderId;
	private String storeCategoryId;
	private boolean autoAcceptOrders;
	private PickupAddressData pickupAddress;
	private String storeEmail;
	private String id;
	private int sellerType;
	private StatusData status;
	private WalletDetailsData walletDetails;
	private String paymentTypeText;
	private int driverTypeId;
	private List<Object> bags;
	private TimestampsData timestamps;
	private String extraNote;
	private int createdTimeStamp;
	private boolean contactLessDelivery;
	private int customerPaymentType;
	private int paymentType;
	private ArrayList<ProductsItemData> products;
	private int requestedForTimeStamp;
	private List<String> orderImages;
	private boolean autoDispatch;
	private CardDetailsData cardDetails;
	private String customerId;
	private String storeName;
	private CustomerDetailsData customerDetails;
	private int freeDeliveryLimitPerStore;
	private boolean payByWallet;
	private int storeType;
	private String coupon;
	private String sellerTypeMsg;
	private String storeCategory;
	private String storeId;
	private String driverType;
	private String masterOrderId;
	private String createdDate;
	private String contactLessDeliveryReason;
	private StoreLogoData storeLogo;
	private BillingAddressData billingAddress;
	private PickingData pickingData;
	private PickupSlotDetailsData pickupSlotDetails;
	private ArrayList<PackingDetailItem> packingDetail;
	private StorePlanData storePlan;

	public StoreOrdersItemData(String requestedFor, String deliverySlotId, int orderType, List<String> receciptURL, String orderTypeMsg, AccountingData accounting, String customerPaymentTypeMsg, int requestedForPickupTimeStamp, String requestedForPickup, String storeTaxId, DeliveryAddressData deliveryAddress, String poInvoiceLink, int bookingType, String storePhone, List<Object> reminderPreference, DeliverySlotDetailsData deliverySlotDetails, String cartId, String pickupSlotId, String bookingTypeText, String storeTypeMsg, String storeOrderId, String storeCategoryId, boolean autoAcceptOrders, PickupAddressData pickupAddress, String storeEmail, String id, int sellerType, StatusData status, WalletDetailsData walletDetails, String paymentTypeText, int driverTypeId, List<Object> bags, TimestampsData timestamps, String extraNote, int createdTimeStamp, boolean contactLessDelivery, int customerPaymentType, int paymentType, ArrayList<ProductsItemData> products, int requestedForTimeStamp, List<String> orderImages, boolean autoDispatch, CardDetailsData cardDetails, String customerId, String storeName, CustomerDetailsData customerDetails, int freeDeliveryLimitPerStore, boolean payByWallet, int storeType, String coupon, String sellerTypeMsg, String storeCategory, String storeId, String driverType, String masterOrderId, String createdDate, String contactLessDeliveryReason, StoreLogoData storeLogo, BillingAddressData billingAddress, PickupSlotDetailsData pickupSlotDetails, StorePlanData storePlan, ArrayList<PackingDetailItem> packingDetail,PickingData pickingData) {
		this.requestedFor = requestedFor;
		this.pickingData = pickingData;
		this.deliverySlotId = deliverySlotId;
		this.orderType = orderType;
		this.receciptURL = receciptURL;
		this.orderTypeMsg = orderTypeMsg;
		this.accounting = accounting;
		this.customerPaymentTypeMsg = customerPaymentTypeMsg;
		this.requestedForPickupTimeStamp = requestedForPickupTimeStamp;
		this.requestedForPickup = requestedForPickup;
		this.storeTaxId = storeTaxId;
		this.deliveryAddress = deliveryAddress;
		this.poInvoiceLink = poInvoiceLink;
		this.bookingType = bookingType;
		this.storePhone = storePhone;
		this.reminderPreference = reminderPreference;
		this.deliverySlotDetails = deliverySlotDetails;
		this.cartId = cartId;
		this.pickupSlotId = pickupSlotId;
		this.bookingTypeText = bookingTypeText;
		this.storeTypeMsg = storeTypeMsg;
		this.storeOrderId = storeOrderId;
		this.storeCategoryId = storeCategoryId;
		this.autoAcceptOrders = autoAcceptOrders;
		this.pickupAddress = pickupAddress;
		this.storeEmail = storeEmail;
		this.id = id;
		this.sellerType = sellerType;
		this.packingDetail = packingDetail;
		this.status = status;
		this.walletDetails = walletDetails;
		this.paymentTypeText = paymentTypeText;
		this.driverTypeId = driverTypeId;
		this.bags = bags;
		this.timestamps = timestamps;
		this.extraNote = extraNote;
		this.createdTimeStamp = createdTimeStamp;
		this.contactLessDelivery = contactLessDelivery;
		this.customerPaymentType = customerPaymentType;
		this.paymentType = paymentType;
		this.products = products;
		this.requestedForTimeStamp = requestedForTimeStamp;
		this.orderImages = orderImages;
		this.autoDispatch = autoDispatch;
		this.cardDetails = cardDetails;
		this.customerId = customerId;
		this.storeName = storeName;
		this.customerDetails = customerDetails;
		this.freeDeliveryLimitPerStore = freeDeliveryLimitPerStore;
		this.payByWallet = payByWallet;
		this.storeType = storeType;
		this.coupon = coupon;
		this.sellerTypeMsg = sellerTypeMsg;
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

	protected StoreOrdersItemData(Parcel in) {
		requestedFor = in.readString();
		deliverySlotId = in.readString();
		orderType = in.readInt();
		receciptURL = in.createStringArrayList();
		orderTypeMsg = in.readString();
		accounting = in.readParcelable(AccountingData.class.getClassLoader());
		customerPaymentTypeMsg = in.readString();
		requestedForPickupTimeStamp = in.readInt();
		requestedForPickup = in.readString();
		storeTaxId = in.readString();
		deliveryAddress = in.readParcelable(DeliveryAddressData.class.getClassLoader());
		poInvoiceLink = in.readString();
		bookingType = in.readInt();
		storePhone = in.readString();
		deliverySlotDetails = in.readParcelable(DeliverySlotDetailsData.class.getClassLoader());
		cartId = in.readString();
		pickupSlotId = in.readString();
		bookingTypeText = in.readString();
		storeTypeMsg = in.readString();
		storeOrderId = in.readString();
		storeCategoryId = in.readString();
		autoAcceptOrders = in.readByte() != 0;
		pickupAddress = in.readParcelable(PickupAddressData.class.getClassLoader());
		storeEmail = in.readString();
		id = in.readString();
		sellerType = in.readInt();
		status = in.readParcelable(StatusData.class.getClassLoader());
		walletDetails = in.readParcelable(WalletDetailsData.class.getClassLoader());
		paymentTypeText = in.readString();
		driverTypeId = in.readInt();
		timestamps = in.readParcelable(TimestampsData.class.getClassLoader());
		extraNote = in.readString();
		createdTimeStamp = in.readInt();
		contactLessDelivery = in.readByte() != 0;
		customerPaymentType = in.readInt();
		paymentType = in.readInt();
		products = in.createTypedArrayList(ProductsItemData.CREATOR);
		requestedForTimeStamp = in.readInt();
		orderImages = in.createStringArrayList();
		autoDispatch = in.readByte() != 0;
		cardDetails = in.readParcelable(CardDetailsData.class.getClassLoader());
		customerId = in.readString();
		storeName = in.readString();
		customerDetails = in.readParcelable(CustomerDetailsData.class.getClassLoader());
		freeDeliveryLimitPerStore = in.readInt();
		payByWallet = in.readByte() != 0;
		storeType = in.readInt();
		coupon = in.readString();
		sellerTypeMsg = in.readString();
		storeCategory = in.readString();
		storeId = in.readString();
		driverType = in.readString();
		masterOrderId = in.readString();
		createdDate = in.readString();
		contactLessDeliveryReason = in.readString();
		storeLogo = in.readParcelable(StoreLogoData.class.getClassLoader());
		billingAddress = in.readParcelable(BillingAddressData.class.getClassLoader());
		pickingData = in.readParcelable(PickingData.class.getClassLoader());
		pickupSlotDetails = in.readParcelable(PickupSlotDetailsData.class.getClassLoader());
		packingDetail = in.createTypedArrayList(PackingDetailItem.CREATOR);
		storePlan = in.readParcelable(StorePlanData.class.getClassLoader());
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
		dest.writeString(createdDate);
		dest.writeString(contactLessDeliveryReason);
		dest.writeParcelable(storeLogo, flags);
		dest.writeParcelable(billingAddress, flags);
		dest.writeParcelable(pickingData, flags);
		dest.writeParcelable(pickupSlotDetails, flags);
		dest.writeTypedList(packingDetail);
		dest.writeParcelable(storePlan, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<StoreOrdersItemData> CREATOR = new Creator<StoreOrdersItemData>() {
		@Override
		public StoreOrdersItemData createFromParcel(Parcel in) {
			return new StoreOrdersItemData(in);
		}

		@Override
		public StoreOrdersItemData[] newArray(int size) {
			return new StoreOrdersItemData[size];
		}
	};

	public boolean isAutoDispatch() {
		return autoDispatch;
	}

	public ArrayList<PackingDetailItem> getPackingDetail() {
		return packingDetail;
	}

	public void setPackingDetail(ArrayList<PackingDetailItem> packingDetail) {
		this.packingDetail = packingDetail;
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

	public void setAccounting(AccountingData accounting){
		this.accounting = accounting;
	}

	public AccountingData getAccounting(){
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

	public void setDeliveryAddress(DeliveryAddressData deliveryAddress){
		this.deliveryAddress = deliveryAddress;
	}

	public DeliveryAddressData getDeliveryAddress(){
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

	public void setDeliverySlotDetails(DeliverySlotDetailsData deliverySlotDetails){
		this.deliverySlotDetails = deliverySlotDetails;
	}

	public DeliverySlotDetailsData getDeliverySlotDetails(){
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

	public void setWalletDetails(WalletDetailsData walletDetails){
		this.walletDetails = walletDetails;
	}

	public WalletDetailsData getWalletDetails(){
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

	public void setTimestamps(TimestampsData timestamps){
		this.timestamps = timestamps;
	}

	public TimestampsData getTimestamps(){
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

	public void setProducts(ArrayList<ProductsItemData> products){
		this.products = products;
	}

	public ArrayList<ProductsItemData> getProducts(){
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

	public void setCardDetails(CardDetailsData cardDetails){
		this.cardDetails = cardDetails;
	}

	public CardDetailsData getCardDetails(){
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

	public void setCustomerDetails(CustomerDetailsData customerDetails){
		this.customerDetails = customerDetails;
	}

	public CustomerDetailsData getCustomerDetails(){
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

	public void setPickupSlotDetails(PickupSlotDetailsData pickupSlotDetails){
		this.pickupSlotDetails = pickupSlotDetails;
	}

	public PickupSlotDetailsData getPickupSlotDetails(){
		return pickupSlotDetails;
	}

	public void setStorePlan(StorePlanData storePlan){
		this.storePlan = storePlan;
	}

	public StorePlanData getStorePlan(){
		return storePlan;
	}

	public PickingData getPickingData() {
		return pickingData;
	}

	public void setPickingData(PickingData pickingData) {
		this.pickingData = pickingData;
	}
}