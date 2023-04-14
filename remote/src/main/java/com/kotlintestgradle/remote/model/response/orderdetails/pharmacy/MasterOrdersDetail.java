package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

import java.util.ArrayList;
import java.util.List;

public class MasterOrdersDetail implements Parcelable, ValidItem {

	@Expose
	@SerializedName("requestedFor")
	private String requestedFor;

	@Expose
	@SerializedName("orderType")
	private int orderType;

	@Expose
	@SerializedName("walletDetails")
	private WalletDetails walletDetails;

	@Expose
	@SerializedName("paymentTypeText")
	private String paymentTypeText;

	@Expose
	@SerializedName("orderId")
	private String orderId;

	@Expose
	@SerializedName("extraNote")
	private String extraNote;

	@Expose
	@SerializedName("timestamps")
	private Timestamps timestamps;

	@Expose
	@SerializedName("createdTimeStamp")
	private int createdTimeStamp;

	@Expose
	@SerializedName("contactLessDelivery")
	private boolean contactLessDelivery;

	@Expose
	@SerializedName("orderTypeMsg")
	private String orderTypeMsg;

	@Expose
	@SerializedName("accounting")
	private Accounting accounting;

	@Expose
	@SerializedName("customerPaymentType")
	private int customerPaymentType;

	@Expose
	@SerializedName("customerPaymentTypeMsg")
	private String customerPaymentTypeMsg;

	@Expose
	@SerializedName("paymentType")
	private int paymentType;

	@Expose
	@SerializedName("requestedForPickupTimeStamp")
	private int requestedForPickupTimeStamp;

	@Expose
	@SerializedName("requestedForPickup")
	private String requestedForPickup;

	@Expose
	@SerializedName("orderImages")
	private ArrayList<String> orderImages;

	@Expose
	@SerializedName("requestedForTimeStamp")
	private int requestedForTimeStamp;

	@Expose
	@SerializedName("deliveryAddress")
	private DeliveryAddress deliveryAddress;

	@Expose
	@SerializedName("bookingType")
	private int bookingType;

	@Expose
	@SerializedName("cardDetails")
	private CardDetails cardDetails;

	@Expose
	@SerializedName("customerId")
	private String customerId;

	@Expose
	@SerializedName("customerDetails")
	private CustomerDetails customerDetails;

	@Expose
	@SerializedName("reminderPreference")
	private List<Object> reminderPreference;

	@Expose
	@SerializedName("payByWallet")
	private boolean payByWallet;

	@Expose
	@SerializedName("storeType")
	private int storeType;

	@SerializedName("coupon")
	@Expose
	private String coupon;

	@Expose
	@SerializedName("cartId")
	private String cartId;

	@Expose
	@SerializedName("bookingTypeText")
	private String bookingTypeText;

	@Expose
	@SerializedName("count")
	private Count count;

	@Expose
	@SerializedName("storeOrders")
	private List<StoreOrdersItem> storeOrders;

	@Expose
	@SerializedName("storeCategory")
	private String storeCategory;

	@Expose
	@SerializedName("packingDetails")
	private List<Object> packingDetails;

	@SerializedName("storeTypeMsg")
	@Expose
	private String storeTypeMsg;

	@SerializedName("createdDate")
	@Expose
	private String createdDate;

	@SerializedName("storeCategoryId")
	@Expose
	private String storeCategoryId;

	@SerializedName("contactLessDeliveryReason")
	@Expose
	private String contactLessDeliveryReason;

	@SerializedName("orders")
	@Expose
	private List<OrdersItem> orders;

	@SerializedName("_id")
	@Expose
	private String id;

	@SerializedName("billingAddress")
	@Expose
	private BillingAddress billingAddress;

	@SerializedName("sellers")
	@Expose
	private List<SellersItem> sellers;

	@SerializedName("status")
	@Expose
	private Status status;

	@SerializedName("storeName")
	@Expose
	private String storeName;

	protected MasterOrdersDetail(Parcel in) {
		requestedFor = in.readString();
		orderType = in.readInt();
		paymentTypeText = in.readString();
		orderId = in.readString();
		extraNote = in.readString();
		createdTimeStamp = in.readInt();
		contactLessDelivery = in.readByte() != 0;
		orderTypeMsg = in.readString();
		accounting = in.readParcelable(Accounting.class.getClassLoader());
		customerPaymentType = in.readInt();
		customerPaymentTypeMsg = in.readString();
		paymentType = in.readInt();
		requestedForPickupTimeStamp = in.readInt();
		requestedForPickup = in.readString();
		requestedForTimeStamp = in.readInt();
		bookingType = in.readInt();
		cardDetails = in.readParcelable(CardDetails.class.getClassLoader());
		customerId = in.readString();
		customerDetails = in.readParcelable(CustomerDetails.class.getClassLoader());
		payByWallet = in.readByte() != 0;
		storeType = in.readInt();
		coupon = in.readString();
		cartId = in.readString();
		bookingTypeText = in.readString();
		count = in.readParcelable(Count.class.getClassLoader());
		storeCategory = in.readString();
		storeTypeMsg = in.readString();
		createdDate = in.readString();
		storeCategoryId = in.readString();
		contactLessDeliveryReason = in.readString();
		id = in.readString();
		storeName = in.readString();
		billingAddress = in.readParcelable(BillingAddress.class.getClassLoader());
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

	public static final Creator<MasterOrdersDetail> CREATOR = new Creator<MasterOrdersDetail>() {
		@Override
		public MasterOrdersDetail createFromParcel(Parcel in) {
			return new MasterOrdersDetail(in);
		}

		@Override
		public MasterOrdersDetail[] newArray(int size) {
			return new MasterOrdersDetail[size];
		}
	};

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

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setExtraNote(String extraNote){
		this.extraNote = extraNote;
	}

	public String getExtraNote(){
		return extraNote;
	}

	public void setTimestamps(Timestamps timestamps){
		this.timestamps = timestamps;
	}

	public Timestamps getTimestamps(){
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

	public void setAccounting(Accounting accounting){
		this.accounting = accounting;
	}

	public Accounting getAccounting(){
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

	public void setDeliveryAddress(DeliveryAddress deliveryAddress){
		this.deliveryAddress = deliveryAddress;
	}

	public DeliveryAddress getDeliveryAddress(){
		return deliveryAddress;
	}

	public void setBookingType(int bookingType){
		this.bookingType = bookingType;
	}

	public int getBookingType(){
		return bookingType;
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

	public void setCustomerDetails(CustomerDetails customerDetails){
		this.customerDetails = customerDetails;
	}

	public CustomerDetails getCustomerDetails(){
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

	public void setCount(Count count){
		this.count = count;
	}

	public Count getCount(){
		return count;
	}

	public void setStoreOrders(List<StoreOrdersItem> storeOrders){
		this.storeOrders = storeOrders;
	}

	public List<StoreOrdersItem> getStoreOrders(){
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

	public void setOrders(List<OrdersItem> orders){
		this.orders = orders;
	}

	public List<OrdersItem> getOrders(){
		return orders;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setBillingAddress(BillingAddress billingAddress){
		this.billingAddress = billingAddress;
	}

	public BillingAddress getBillingAddress(){
		return billingAddress;
	}

	public void setSellers(List<SellersItem> sellers){
		this.sellers = sellers;
	}

	public List<SellersItem> getSellers(){
		return sellers;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	@Override
	public boolean isValid() {
		return true;
	}
}