package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.getaddress.AddressListItemDetails;

import java.util.ArrayList;

public class OrderHistoryItemDetails implements Parcelable, ValidItem {

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
  @SerializedName("timestamps")
  private OrderTimeStampDetails timestamps;
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
  @SerializedName("customerDetails")
  private OrderHistoryCustomerDetails customerDetails;
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
  @SerializedName("storeOrders")
  private ArrayList<OrderHistStoreOrderDetails> storeOrders;
  @Expose
  @SerializedName("storeTypeMsg")
  private String storeTypeMsg;
  @Expose
  @SerializedName("createdDate")
  private String createdDate;
  @Expose
  @SerializedName("orders")
  private ArrayList<OrderHistOrderDetails> orders;
  @Expose
  @SerializedName("_id")
  private String _id;
  @Expose
  @SerializedName("billingAddress")
  private AddressListItemDetails billingAddress;
  @Expose
  @SerializedName("sellers")
  private ArrayList<OrdersHistorySellerDetails> sellers;
  @Expose
  @SerializedName("status")
  private OrderStatusDetails status;

  public OrderHistoryItemDetails(String requestedFor, String orderType,
      String paymentTypeText, String orderId,
      OrderTimeStampDetails timestamps, String createdTimeStamp, String orderTypeMsg,
      OrderHistoryAccounting accounting, String paymentType, String requestedForTimeStamp,
      AddressListItemDetails deliveryAddress, String customerId,
      OrderHistoryCustomerDetails customerDetails, String payByWallet, String storeType,
      String coupon, String cartId,
      ArrayList<OrderHistStoreOrderDetails> storeOrders, String storeTypeMsg,
      String createdDate,
      ArrayList<OrderHistOrderDetails> orders, String _id,
      AddressListItemDetails billingAddress,
      ArrayList<OrdersHistorySellerDetails> sellers,
      OrderStatusDetails status) {
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
    this._id = _id;
    this.billingAddress = billingAddress;
    this.sellers = sellers;
    this.status = status;
  }

  protected OrderHistoryItemDetails(Parcel in) {
    requestedFor = in.readString();
    orderType = in.readString();
    paymentTypeText = in.readString();
    orderId = in.readString();
    createdTimeStamp = in.readString();
    orderTypeMsg = in.readString();
    accounting = in.readParcelable(OrderHistoryAccounting.class.getClassLoader());
    paymentType = in.readString();
    requestedForTimeStamp = in.readString();
    customerId = in.readString();
    customerDetails = in.readParcelable(OrderHistoryCustomerDetails.class.getClassLoader());
    payByWallet = in.readString();
    storeType = in.readString();
    coupon = in.readString();
    cartId = in.readString();
    storeTypeMsg = in.readString();
    createdDate = in.readString();
    orders = in.createTypedArrayList(OrderHistOrderDetails.CREATOR);
    _id = in.readString();
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
    dest.writeString(customerId);
    dest.writeParcelable(customerDetails, flags);
    dest.writeString(payByWallet);
    dest.writeString(storeType);
    dest.writeString(coupon);
    dest.writeString(cartId);
    dest.writeString(storeTypeMsg);
    dest.writeString(createdDate);
    dest.writeTypedList(orders);
    dest.writeString(_id);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistoryItemDetails> CREATOR =
      new Creator<OrderHistoryItemDetails>() {
        @Override
        public OrderHistoryItemDetails createFromParcel(Parcel in) {
          return new OrderHistoryItemDetails(in);
        }

        @Override
        public OrderHistoryItemDetails[] newArray(int size) {
          return new OrderHistoryItemDetails[size];
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

  public OrderTimeStampDetails getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(
      OrderTimeStampDetails timestamps) {
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

  public OrderHistoryCustomerDetails getCustomerDetails() {
    return customerDetails;
  }

  public void setCustomerDetails(
      OrderHistoryCustomerDetails customerDetails) {
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

  public ArrayList<OrderHistStoreOrderDetails> getStoreOrders() {
    return storeOrders;
  }

  public void setStoreOrders(
      ArrayList<OrderHistStoreOrderDetails> storeOrders) {
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

  public ArrayList<OrderHistOrderDetails> getOrders() {
    return orders;
  }

  public void setOrders(
      ArrayList<OrderHistOrderDetails> orders) {
    this.orders = orders;
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

  public ArrayList<OrdersHistorySellerDetails> getSellers() {
    return sellers;
  }

  public void setSellers(
      ArrayList<OrdersHistorySellerDetails> sellers) {
    this.sellers = sellers;
  }

  public OrderStatusDetails getStatus() {
    return status;
  }

  public void setStatus(OrderStatusDetails status) {
    this.status = status;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
