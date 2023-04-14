package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrdersData implements Parcelable {
	private String requestedFor;
	private int orderType;
	private WalletDetailsData walletDetails;
	private String paymentTypeText;
	private String orderId;
	private String extraNote;
	private TimestampsData timestamps;
	private int createdTimeStamp;
	private boolean contactLessDelivery;
	private String orderTypeMsg;
	private AccountingData accounting;
	private int customerPaymentType;
	private String customerPaymentTypeMsg;
	private int paymentType;
	private int requestedForPickupTimeStamp;
	private String requestedForPickup;
	private ArrayList<String> orderImages;
	private int requestedForTimeStamp;
	private DeliveryAddressData deliveryAddress;
	private int bookingType;
	private CardDetailsData cardDetails;
	private String customerId;
	private CustomerDetailsData customerDetails;
	private List<Object> reminderPreference;
	private boolean payByWallet;
	private int storeType;
	private String coupon;
	private String cartId;
	private String bookingTypeText;
	private CountsData count;
	private ArrayList<StoreOrdersItemData> storeOrders;
	private String storeCategory;
	private List<Object> packingDetails;
	private String storeTypeMsg;
	private String createdDate;
	private String storeCategoryId;
	private String contactLessDeliveryReason;
	private ArrayList<OrdersItemData> orders;
	private String id;
	private BillingAddressData billingAddress;
	private ArrayList<SellersItemData> sellers;
	private StatusData status;
	private String storeName;

	public OrdersData(String requestedFor, int orderType, WalletDetailsData walletDetails, String paymentTypeText, String orderId, String extraNote, TimestampsData timestamps, int createdTimeStamp, boolean contactLessDelivery, String orderTypeMsg, AccountingData accounting, int customerPaymentType, String customerPaymentTypeMsg, int paymentType, int requestedForPickupTimeStamp, String requestedForPickup, ArrayList<String> orderImages, int requestedForTimeStamp, DeliveryAddressData deliveryAddress, int bookingType, CardDetailsData cardDetails, String customerId, CustomerDetailsData customerDetails, List<Object> reminderPreference, boolean payByWallet, int storeType, String coupon, String cartId, String bookingTypeText, CountsData count, ArrayList<StoreOrdersItemData> storeOrders, String storeCategory, List<Object> packingDetails, String storeTypeMsg, String createdDate, String storeCategoryId, String contactLessDeliveryReason, ArrayList<OrdersItemData> orders, String id, BillingAddressData billingAddress, ArrayList<SellersItemData> sellers, StatusData status,String storeName) {
		this.requestedFor = requestedFor;
		this.orderType = orderType;
		this.walletDetails = walletDetails;
		this.paymentTypeText = paymentTypeText;
		this.orderId = orderId;
		this.extraNote = extraNote;
		this.timestamps = timestamps;
		this.createdTimeStamp = createdTimeStamp;
		this.contactLessDelivery = contactLessDelivery;
		this.orderTypeMsg = orderTypeMsg;
		this.accounting = accounting;
		this.storeName = storeName;
		this.customerPaymentType = customerPaymentType;
		this.customerPaymentTypeMsg = customerPaymentTypeMsg;
		this.paymentType = paymentType;
		this.requestedForPickupTimeStamp = requestedForPickupTimeStamp;
		this.requestedForPickup = requestedForPickup;
		this.orderImages = orderImages;
		this.requestedForTimeStamp = requestedForTimeStamp;
		this.deliveryAddress = deliveryAddress;
		this.bookingType = bookingType;
		this.cardDetails = cardDetails;
		this.customerId = customerId;
		this.customerDetails = customerDetails;
		this.reminderPreference = reminderPreference;
		this.payByWallet = payByWallet;
		this.storeType = storeType;
		this.coupon = coupon;
		this.cartId = cartId;
		this.bookingTypeText = bookingTypeText;
		this.count = count;
		this.storeOrders = storeOrders;
		this.storeCategory = storeCategory;
		this.packingDetails = packingDetails;
		this.storeTypeMsg = storeTypeMsg;
		this.createdDate = createdDate;
		this.storeCategoryId = storeCategoryId;
		this.contactLessDeliveryReason = contactLessDeliveryReason;
		this.orders = orders;
		this.id = id;
		this.billingAddress = billingAddress;
		this.sellers = sellers;
		this.status = status;
	}

	protected OrdersData(Parcel in) {
		requestedFor = in.readString();
		orderType = in.readInt();
		paymentTypeText = in.readString();
		orderId = in.readString();
		extraNote = in.readString();
		createdTimeStamp = in.readInt();
		contactLessDelivery = in.readByte() != 0;
		orderTypeMsg = in.readString();
		accounting = in.readParcelable(AccountingData.class.getClassLoader());
		customerPaymentType = in.readInt();
		customerPaymentTypeMsg = in.readString();
		paymentType = in.readInt();
		requestedForPickupTimeStamp = in.readInt();
		requestedForPickup = in.readString();
		requestedForTimeStamp = in.readInt();
		deliveryAddress = in.readParcelable(DeliveryAddressData.class.getClassLoader());
		bookingType = in.readInt();
		cardDetails = in.readParcelable(CardDetailsData.class.getClassLoader());
		customerId = in.readString();
		customerDetails = in.readParcelable(CustomerDetailsData.class.getClassLoader());
		payByWallet = in.readByte() != 0;
		storeType = in.readInt();
		coupon = in.readString();
		cartId = in.readString();
		bookingTypeText = in.readString();
		count = in.readParcelable(CountsData.class.getClassLoader());
		storeCategory = in.readString();
		storeTypeMsg = in.readString();
		createdDate = in.readString();
		storeCategoryId = in.readString();
		contactLessDeliveryReason = in.readString();
		id = in.readString();
		billingAddress = in.readParcelable(BillingAddressData.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(requestedFor);
		dest.writeInt(orderType);
		dest.writeString(paymentTypeText);
		dest.writeString(orderId);
		dest.writeString(extraNote);
		dest.writeInt(createdTimeStamp);
		dest.writeByte((byte) (contactLessDelivery ? 1 : 0));
		dest.writeString(orderTypeMsg);
		dest.writeParcelable(accounting, flags);
		dest.writeInt(customerPaymentType);
		dest.writeString(customerPaymentTypeMsg);
		dest.writeInt(paymentType);
		dest.writeInt(requestedForPickupTimeStamp);
		dest.writeString(requestedForPickup);
		dest.writeInt(requestedForTimeStamp);
		dest.writeParcelable(deliveryAddress, flags);
		dest.writeInt(bookingType);
		dest.writeParcelable(cardDetails, flags);
		dest.writeString(customerId);
		dest.writeParcelable(customerDetails, flags);
		dest.writeByte((byte) (payByWallet ? 1 : 0));
		dest.writeInt(storeType);
		dest.writeString(coupon);
		dest.writeString(cartId);
		dest.writeString(bookingTypeText);
		dest.writeParcelable(count, flags);
		dest.writeString(storeCategory);
		dest.writeString(storeTypeMsg);
		dest.writeString(createdDate);
		dest.writeString(storeCategoryId);
		dest.writeString(contactLessDeliveryReason);
		dest.writeString(id);
		dest.writeString(storeName);
		dest.writeParcelable(billingAddress, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<OrdersData> CREATOR = new Creator<OrdersData>() {
		@Override
		public OrdersData createFromParcel(Parcel in) {
			return new OrdersData(in);
		}

		@Override
		public OrdersData[] newArray(int size) {
			return new OrdersData[size];
		}
	};

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setRequestedFor(String requestedFor){
		this.requestedFor = requestedFor;
	}

	public String getRequestedFor(){
		return requestedFor;
	}

	public void setOrderType(int orderType){
		this.orderType = orderType;
	}

	public int getOrderType(){
		return orderType;
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

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setExtraNote(String extraNote){
		this.extraNote = extraNote;
	}

	public String getExtraNote(){
		return extraNote;
	}

	public void setTimestamps(TimestampsData timestamps){
		this.timestamps = timestamps;
	}

	public TimestampsData getTimestamps(){
		return timestamps;
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

	public void setCustomerPaymentType(int customerPaymentType){
		this.customerPaymentType = customerPaymentType;
	}

	public int getCustomerPaymentType(){
		return customerPaymentType;
	}

	public void setCustomerPaymentTypeMsg(String customerPaymentTypeMsg){
		this.customerPaymentTypeMsg = customerPaymentTypeMsg;
	}

	public String getCustomerPaymentTypeMsg(){
		return customerPaymentTypeMsg;
	}

	public void setPaymentType(int paymentType){
		this.paymentType = paymentType;
	}

	public int getPaymentType(){
		return paymentType;
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

	public void setOrderImages(ArrayList<String> orderImages){
		this.orderImages = orderImages;
	}

	public ArrayList<String> getOrderImages(){
		return orderImages;
	}

	public void setRequestedForTimeStamp(int requestedForTimeStamp){
		this.requestedForTimeStamp = requestedForTimeStamp;
	}

	public int getRequestedForTimeStamp(){
		return requestedForTimeStamp;
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

	public void setCustomerDetails(CustomerDetailsData customerDetails){
		this.customerDetails = customerDetails;
	}

	public CustomerDetailsData getCustomerDetails(){
		return customerDetails;
	}

	public void setReminderPreference(List<Object> reminderPreference){
		this.reminderPreference = reminderPreference;
	}

	public List<Object> getReminderPreference(){
		return reminderPreference;
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

	public void setCartId(String cartId){
		this.cartId = cartId;
	}

	public String getCartId(){
		return cartId;
	}

	public void setBookingTypeText(String bookingTypeText){
		this.bookingTypeText = bookingTypeText;
	}

	public String getBookingTypeText(){
		return bookingTypeText;
	}

	public void setCount(CountsData count){
		this.count = count;
	}

	public CountsData getCount(){
		return count;
	}

	public void setStoreOrders(ArrayList<StoreOrdersItemData> storeOrders){
		this.storeOrders = storeOrders;
	}

	public ArrayList<StoreOrdersItemData> getStoreOrders(){
		return storeOrders;
	}

	public void setStoreCategory(String storeCategory){
		this.storeCategory = storeCategory;
	}

	public String getStoreCategory(){
		return storeCategory;
	}

	public void setPackingDetails(List<Object> packingDetails){
		this.packingDetails = packingDetails;
	}

	public List<Object> getPackingDetails(){
		return packingDetails;
	}

	public void setStoreTypeMsg(String storeTypeMsg){
		this.storeTypeMsg = storeTypeMsg;
	}

	public String getStoreTypeMsg(){
		return storeTypeMsg;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setStoreCategoryId(String storeCategoryId){
		this.storeCategoryId = storeCategoryId;
	}

	public String getStoreCategoryId(){
		return storeCategoryId;
	}

	public void setContactLessDeliveryReason(String contactLessDeliveryReason){
		this.contactLessDeliveryReason = contactLessDeliveryReason;
	}

	public String getContactLessDeliveryReason(){
		return contactLessDeliveryReason;
	}

	public void setOrders(ArrayList<OrdersItemData> orders){
		this.orders = orders;
	}

	public ArrayList<OrdersItemData> getOrders(){
		return orders;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setBillingAddress(BillingAddressData billingAddress){
		this.billingAddress = billingAddress;
	}

	public BillingAddressData getBillingAddress(){
		return billingAddress;
	}

	public void setSellers(ArrayList<SellersItemData> sellers){
		this.sellers = sellers;
	}

	public ArrayList<SellersItemData> getSellers(){
		return sellers;
	}

	public void setStatus(StatusData status){
		this.status = status;
	}

	public StatusData getStatus(){
		return status;
	}
}