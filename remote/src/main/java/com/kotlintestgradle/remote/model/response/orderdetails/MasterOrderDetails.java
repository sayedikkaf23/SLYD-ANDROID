package com.kotlintestgradle.remote.model.response.orderdetails;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.getaddress.AddressListItemDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.PackingDetailsItem;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistProductDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryAccounting;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderStatusDetails;
import com.kotlintestgradle.remote.model.response.tracking.TrackingItemDetails;

import java.util.ArrayList;

public class MasterOrderDetails implements Parcelable , ValidItem {

  @Expose
  @SerializedName("requestedFor")
  private String requestedFor;
  @Expose
  @SerializedName("orderType")
  private String orderType;
  @Expose
  @SerializedName("paymentTypeText")
  private String paymentTypeText;
  @Expose
  @SerializedName("orderId")
  private String orderId;
  @Expose
  @SerializedName("createdTimeStamp")
  private String createdTimeStamp;
  @Expose
  @SerializedName("orderTypeMsg")
  private String orderTypeMsg;
  @Expose
  @SerializedName("accounting")
  private OrderHistoryAccounting accounting;
  @Expose
  @SerializedName("paymentType")
  private String paymentType;
  @Expose
  @SerializedName("requestedForTimeStamp")
  private String requestedForTimeStamp;
  @Expose
  @SerializedName("deliveryAddress")
  private AddressListItemDetails deliveryAddress;
  @Expose
  @SerializedName("customerId")
  private String customerId;
  @Expose
  @SerializedName("payByWallet")
  private String payByWallet;
  @Expose
  @SerializedName("storeType")
  private String storeType;
  @Expose
  @SerializedName("coupon")
  private String coupon;
  @Expose
  @SerializedName("cartId")
  private String cartId;
  @Expose
  @SerializedName("storeName")
  private String storeName;
  @Expose
  @SerializedName("storeOrders")
  private ArrayList<OrderDetails> storeOrders;

  @Expose
  @SerializedName("orderImages")
  private ArrayList<String> prescriptions;

  @Expose
  @SerializedName("products")
  private ArrayList<OrderHistProductDetails> products;

@Expose
  @SerializedName("packingDetails")
  private ArrayList<PackingDetailsItem> packingDetails;

  @Expose
  @SerializedName("storeTypeMsg")
  private String storeTypeMsg;
  @Expose
  @SerializedName("createdDate")
  private String createdDate;
  @Expose
  @SerializedName("_id")
  private String _id;
  @Expose
  @SerializedName("tip")
  private String tip;
  @Expose
  @SerializedName("contactLessDelivery")
  private boolean contactLessDelivery;
  @Expose
  @SerializedName("contactLessDeliveryReason")
  private String contactLessDeliveryReason;
  @Expose
  @SerializedName("billingAddress")
  private AddressListItemDetails billingAddress;
  @Expose
  @SerializedName("status")
  private OrderStatusDetails status;
  @Expose
  @SerializedName("orderStatus")
  private ArrayList<TrackingItemDetails> orderStatus;
  public MasterOrderDetails(String requestedFor, String orderType, String paymentTypeText,
      String orderId, String createdTimeStamp, String orderTypeMsg,
      OrderHistoryAccounting accounting, String paymentType, String requestedForTimeStamp,
      AddressListItemDetails deliveryAddress, String customerId, String payByWallet,
      String storeType, String coupon, String cartId,
      ArrayList<OrderDetails> storeOrders,
      String storeTypeMsg, String createdDate, String _id,
      AddressListItemDetails billingAddress,
      OrderStatusDetails status,ArrayList<TrackingItemDetails> orderStatus) {
    this.requestedFor = requestedFor;
    this.orderType = orderType;
    this.paymentTypeText = paymentTypeText;
    this.orderId = orderId;
    this.createdTimeStamp = createdTimeStamp;
    this.orderTypeMsg = orderTypeMsg;
    this.accounting = accounting;
    this.paymentType = paymentType;
    this.requestedForTimeStamp = requestedForTimeStamp;
    this.deliveryAddress = deliveryAddress;
    this.customerId = customerId;
    this.payByWallet = payByWallet;
    this.storeType = storeType;
    this.coupon = coupon;
    this.cartId = cartId;
    this.storeOrders = storeOrders;
    this.packingDetails = packingDetails;


    this.storeTypeMsg = storeTypeMsg;
    this.createdDate = createdDate;
    this._id = _id;
    this.billingAddress = billingAddress;
    this.status = status;
    this.orderStatus=orderStatus;
  }

  protected MasterOrderDetails(Parcel in) {
    requestedFor = in.readString();
    orderType = in.readString();
    paymentTypeText = in.readString();
    orderId = in.readString();
    createdTimeStamp = in.readString();
    orderTypeMsg = in.readString();
    accounting = in.readParcelable(OrderHistoryAccounting.class.getClassLoader());
    paymentType = in.readString();
    requestedForTimeStamp = in.readString();
    deliveryAddress = in.readParcelable(AddressListItemDetails.class.getClassLoader());
    customerId = in.readString();
    payByWallet = in.readString();
    storeType = in.readString();
    coupon = in.readString();
    cartId = in.readString();
    storeName = in.readString();
    storeOrders = in.createTypedArrayList(OrderDetails.CREATOR);
    prescriptions = in.createStringArrayList();
    products = in.createTypedArrayList(OrderHistProductDetails.CREATOR);
    storeTypeMsg = in.readString();
    createdDate = in.readString();
    _id = in.readString();
    billingAddress = in.readParcelable(AddressListItemDetails.class.getClassLoader());
    status = in.readParcelable(OrderStatusDetails.class.getClassLoader());
    orderStatus = in.createTypedArrayList(TrackingItemDetails.CREATOR);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(requestedFor);
    dest.writeString(orderType);
    dest.writeString(paymentTypeText);
    dest.writeString(orderId);
    dest.writeString(createdTimeStamp);
    dest.writeString(orderTypeMsg);
    dest.writeParcelable(accounting, flags);
    dest.writeString(paymentType);
    dest.writeString(requestedForTimeStamp);
    dest.writeParcelable(deliveryAddress, flags);
    dest.writeString(customerId);
    dest.writeString(payByWallet);
    dest.writeString(storeType);
    dest.writeString(coupon);
    dest.writeString(cartId);
    dest.writeString(storeName);
    dest.writeTypedList(storeOrders);
    dest.writeStringList(prescriptions);
    dest.writeTypedList(products);
    dest.writeString(storeTypeMsg);
    dest.writeString(createdDate);
    dest.writeString(_id);
    dest.writeParcelable(billingAddress, flags);
    dest.writeParcelable(status, flags);
    dest.writeTypedList(orderStatus);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MasterOrderDetails> CREATOR = new Creator<MasterOrderDetails>() {
    @Override
    public MasterOrderDetails createFromParcel(Parcel in) {
      return new MasterOrderDetails(in);
    }

    @Override
    public MasterOrderDetails[] newArray(int size) {
      return new MasterOrderDetails[size];
    }
  };

  public String getRequestedFor() {
    return requestedFor;
  }

  public void setRequestedFor(String requestedFor) {
    this.requestedFor = requestedFor;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getPaymentTypeText() {
    return paymentTypeText;
  }

  public void setPaymentTypeText(String paymentTypeText) {
    this.paymentTypeText = paymentTypeText;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getCreatedTimeStamp() {
    return createdTimeStamp;
  }

  public void setCreatedTimeStamp(String createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
  }

  public String getOrderTypeMsg() {
    return orderTypeMsg;
  }

  public void setOrderTypeMsg(String orderTypeMsg) {
    this.orderTypeMsg = orderTypeMsg;
  }

  public OrderHistoryAccounting getAccounting() {
    return accounting;
  }

  public void setAccounting(
      OrderHistoryAccounting accounting) {
    this.accounting = accounting;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public String getRequestedForTimeStamp() {
    return requestedForTimeStamp;
  }

  public void setRequestedForTimeStamp(String requestedForTimeStamp) {
    this.requestedForTimeStamp = requestedForTimeStamp;
  }

  public AddressListItemDetails getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(
      AddressListItemDetails deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getPayByWallet() {
    return payByWallet;
  }

  public void setPayByWallet(String payByWallet) {
    this.payByWallet = payByWallet;
  }

  public String getStoreType() {
    return storeType;
  }

  public void setStoreType(String storeType) {
    this.storeType = storeType;
  }

  public String getCoupon() {
    return coupon;
  }

  public void setCoupon(String coupon) {
    this.coupon = coupon;
  }

  public String getCartId() {
    return cartId;
  }

  public void setCartId(String cartId) {
    this.cartId = cartId;
  }

  public ArrayList<OrderDetails> getStoreOrders() {
    return storeOrders;
  }

  public void setStoreOrders(
      ArrayList<OrderDetails> storeOrders) {
    this.storeOrders = storeOrders;
  }

 public ArrayList<PackingDetailsItem> getPackingDetails() {
    return packingDetails;
  }

  public void setPackingDetails(ArrayList<PackingDetailsItem> packingDetails) {
    this.packingDetails = packingDetails;
  }


  public String getStoreTypeMsg() {
    return storeTypeMsg;
  }

  public void setStoreTypeMsg(String storeTypeMsg) {
    this.storeTypeMsg = storeTypeMsg;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public AddressListItemDetails getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(
      AddressListItemDetails billingAddress) {
    this.billingAddress = billingAddress;
  }

  public OrderStatusDetails getStatus() {
    return status;
  }

  public void setStatus(OrderStatusDetails status) {
    this.status = status;
  }

  public ArrayList<OrderHistProductDetails> getProducts() {
    return products;
  }

  public void setProducts(
      ArrayList<OrderHistProductDetails> products) {
    this.products = products;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public ArrayList<TrackingItemDetails> getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(
      ArrayList<TrackingItemDetails> orderStatus) {
    this.orderStatus = orderStatus;
  }

  public ArrayList<String> getPrescriptions() {
    return prescriptions;
  }

  public void setPrescriptions(ArrayList<String> prescriptions) {
    this.prescriptions = prescriptions;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public boolean isContactLessDelivery() {
    return contactLessDelivery;
  }

  public void setContactLessDelivery(boolean contactLessDelivery) {
    this.contactLessDelivery = contactLessDelivery;
  }

  public String getContactLessDeliveryReason() {
    return contactLessDeliveryReason;
  }

  public void setContactLessDeliveryReason(String contactLessDeliveryReason) {
    this.contactLessDeliveryReason = contactLessDeliveryReason;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
