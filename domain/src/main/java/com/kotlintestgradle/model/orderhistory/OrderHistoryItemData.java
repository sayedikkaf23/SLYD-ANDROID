package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;


import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;

import java.util.ArrayList;

public class OrderHistoryItemData implements Parcelable {

  private String requestedFor;
  private String orderType;
  private String paymentTypeText;
  private String orderId;
  private OrderTimeStampData timestamps;
  private String createdTimeStamp;
  private String orderTypeMsg;
  private OrderHistoryAccountingData accounting;
  private String paymentType;
  private String requestedForTimeStamp;
  private AddressListItemData deliveryAddress;
  private String customerId;
  private OrderHistoryCustomerData customerDetails;
  private String payByWallet;
  private String storeType;
  private String coupon;
  private String cartId;
  private ArrayList<OrderHistStoreOrderData> storeOrders;
  private String storeTypeMsg;
  private String createdDate;
  private ArrayList<OrderHistOrderData> orders;
  private String id;
  private AddressListItemData billingAddress;
  private ArrayList<OrdersHistorySellerData> sellers;
  private OrderStatusData status;

  public OrderHistoryItemData(String requestedFor, String orderType, String paymentTypeText, String orderId, OrderTimeStampData timestamps, String createdTimeStamp, String orderTypeMsg, OrderHistoryAccountingData accounting, String paymentType, String requestedForTimeStamp, AddressListItemData deliveryAddress, String customerId, OrderHistoryCustomerData customerDetails, String payByWallet, String storeType, String coupon, String cartId, ArrayList<OrderHistStoreOrderData> storeOrders, String storeTypeMsg, String createdDate, ArrayList<OrderHistOrderData> orders, String id, AddressListItemData billingAddress, ArrayList<OrdersHistorySellerData> sellers, OrderStatusData status) {
    this.requestedFor = requestedFor;
    this.orderType = orderType;
    this.paymentTypeText = paymentTypeText;
    this.orderId = orderId;
    this.timestamps = timestamps;
    this.createdTimeStamp = createdTimeStamp;
    this.orderTypeMsg = orderTypeMsg;
    this.accounting = accounting;
    this.paymentType = paymentType;
    this.requestedForTimeStamp = requestedForTimeStamp;
    this.deliveryAddress = deliveryAddress;
    this.customerId = customerId;
    this.customerDetails = customerDetails;
    this.payByWallet = payByWallet;
    this.storeType = storeType;
    this.coupon = coupon;
    this.cartId = cartId;
    this.storeOrders = storeOrders;
    this.storeTypeMsg = storeTypeMsg;
    this.createdDate = createdDate;
    this.orders = orders;
    this.id = id;
    this.billingAddress = billingAddress;
    this.sellers = sellers;
    this.status = status;
  }

  protected OrderHistoryItemData(Parcel in) {
    requestedFor = in.readString();
    orderType = in.readString();
    paymentTypeText = in.readString();
    orderId = in.readString();
    timestamps = in.readParcelable(OrderTimeStampData.class.getClassLoader());
    createdTimeStamp = in.readString();
    orderTypeMsg = in.readString();
    accounting = in.readParcelable(OrderHistoryAccountingData.class.getClassLoader());
    paymentType = in.readString();
    requestedForTimeStamp = in.readString();
    deliveryAddress = in.readParcelable(AddressListItemData.class.getClassLoader());
    customerId = in.readString();
    customerDetails = in.readParcelable(OrderHistoryCustomerData.class.getClassLoader());
    payByWallet = in.readString();
    storeType = in.readString();
    coupon = in.readString();
    cartId = in.readString();
    storeOrders = in.createTypedArrayList(OrderHistStoreOrderData.CREATOR);
    storeTypeMsg = in.readString();
    createdDate = in.readString();
    orders = in.createTypedArrayList(OrderHistOrderData.CREATOR);
    id = in.readString();
    billingAddress = in.readParcelable(AddressListItemData.class.getClassLoader());
    sellers = in.createTypedArrayList(OrdersHistorySellerData.CREATOR);
    status = in.readParcelable(OrderStatusData.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(requestedFor);
    dest.writeString(orderType);
    dest.writeString(paymentTypeText);
    dest.writeString(orderId);
    dest.writeParcelable(timestamps, flags);
    dest.writeString(createdTimeStamp);
    dest.writeString(orderTypeMsg);
    dest.writeParcelable(accounting, flags);
    dest.writeString(paymentType);
    dest.writeString(requestedForTimeStamp);
    dest.writeParcelable(deliveryAddress, flags);
    dest.writeString(customerId);
    dest.writeParcelable(customerDetails, flags);
    dest.writeString(payByWallet);
    dest.writeString(storeType);
    dest.writeString(coupon);
    dest.writeString(cartId);
    dest.writeTypedList(storeOrders);
    dest.writeString(storeTypeMsg);
    dest.writeString(createdDate);
    dest.writeTypedList(orders);
    dest.writeString(id);
    dest.writeParcelable(billingAddress, flags);
    dest.writeTypedList(sellers);
    dest.writeParcelable(status, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistoryItemData> CREATOR = new Creator<OrderHistoryItemData>() {
    @Override
    public OrderHistoryItemData createFromParcel(Parcel in) {
      return new OrderHistoryItemData(in);
    }

    @Override
    public OrderHistoryItemData[] newArray(int size) {
      return new OrderHistoryItemData[size];
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

  public OrderTimeStampData getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(OrderTimeStampData timestamps) {
    this.timestamps = timestamps;
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

  public OrderHistoryAccountingData getAccounting() {
    return accounting;
  }

  public void setAccounting(OrderHistoryAccountingData accounting) {
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

  public AddressListItemData getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(AddressListItemData deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public OrderHistoryCustomerData getCustomerDetails() {
    return customerDetails;
  }

  public void setCustomerDetails(OrderHistoryCustomerData customerDetails) {
    this.customerDetails = customerDetails;
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

  public ArrayList<OrderHistStoreOrderData> getStoreOrders() {
    return storeOrders;
  }

  public void setStoreOrders(ArrayList<OrderHistStoreOrderData> storeOrders) {
    this.storeOrders = storeOrders;
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

  public ArrayList<OrderHistOrderData> getOrders() {
    return orders;
  }

  public void setOrders(ArrayList<OrderHistOrderData> orders) {
    this.orders = orders;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AddressListItemData getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(AddressListItemData billingAddress) {
    this.billingAddress = billingAddress;
  }

  public ArrayList<OrdersHistorySellerData> getSellers() {
    return sellers;
  }

  public void setSellers(ArrayList<OrdersHistorySellerData> sellers) {
    this.sellers = sellers;
  }

  public OrderStatusData getStatus() {
    return status;
  }

  public void setStatus(OrderStatusData status) {
    this.status = status;
  }
}
