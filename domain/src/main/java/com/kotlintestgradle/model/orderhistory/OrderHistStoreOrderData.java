package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;
import com.customer.domain.model.orderdetails.pharmacy.StoreRatingData;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.getcart.CartPickupAddressData;
import com.kotlintestgradle.model.orderdetails.pharmacy.DriverDetailsData;

import java.util.ArrayList;

public class OrderHistStoreOrderData implements Parcelable {

  private String requestedFor;
  private String orderType;
  private String paymentTypeText;
  private String driverTypeId;
  private OrderTimeStampData timestamps;
  private String createdTimeStamp;
  private String orderTypeMsg;
  private OrderHistoryAccountingData accounting;
  private String paymentType;
  private ArrayList<OrderHistProductData> products;
  private String requestedForTimeStamp;
  private String autoDispatch;
  private CartPickupAddressData deliveryAddress;
  private String storePhone;
  private String customerId;
  private String storeName;
  private OrderHistoryCustomerData customerDetails;
  private String freeDeliveryLimitPerStore;
  private String payByWallet;
  private String storeType;
  private String cartId;
  private String storeId;
  private String driverType;
  private String masterOrderId;
  private String storeTypeMsg;
  private String createdDate;
  private String storeOrderId;
  private String autoAcceptOrders;
  private CartPickupAddressData pickupAddress;
  private StoreLogoData storeLogo;
  private String id;
  private AddressListItemData billingAddress;
  private OrderHistoryPlanData storePlan;
  private OrderStatusData status;
  private DriverDetailsData driverDetailsData;
  private StoreRatingData storeRatingData;
  private StoreRatingData driverRatingData;

  public OrderHistStoreOrderData(String requestedFor, String orderType,
      String paymentTypeText, String driverTypeId,
      OrderTimeStampData timestamps, String createdTimeStamp, String orderTypeMsg,
      OrderHistoryAccountingData accounting, String paymentType,
      ArrayList<OrderHistProductData> products, String requestedForTimeStamp,
      String autoDispatch, CartPickupAddressData deliveryAddress, String storePhone,
      String customerId, String storeName,
      OrderHistoryCustomerData customerDetails, String freeDeliveryLimitPerStore,
      String payByWallet, String storeType, String cartId, String storeId,
      String driverType, String masterOrderId, String storeTypeMsg, String createdDate,
      String storeOrderId, String autoAcceptOrders,
      CartPickupAddressData pickupAddress,
      StoreLogoData storeLogo, String id,
      AddressListItemData billingAddress,
      OrderHistoryPlanData storePlan, OrderStatusData status,DriverDetailsData driverDetailsData,
                                 StoreRatingData storeRatingData, StoreRatingData driverRatingData) {
    this.requestedFor = requestedFor;
    this.orderType = orderType;
    this.paymentTypeText = paymentTypeText;
    this.driverTypeId = driverTypeId;
    this.timestamps = timestamps;
    this.createdTimeStamp = createdTimeStamp;
    this.orderTypeMsg = orderTypeMsg;
    this.accounting = accounting;
    this.paymentType = paymentType;
    this.driverDetailsData = driverDetailsData;
    this.products = products;
    this.requestedForTimeStamp = requestedForTimeStamp;
    this.autoDispatch = autoDispatch;
    this.deliveryAddress = deliveryAddress;
    this.storePhone = storePhone;
    this.customerId = customerId;
    this.storeName = storeName;
    this.customerDetails = customerDetails;
    this.freeDeliveryLimitPerStore = freeDeliveryLimitPerStore;
    this.payByWallet = payByWallet;
    this.storeType = storeType;
    this.cartId = cartId;
    this.storeId = storeId;
    this.driverType = driverType;
    this.masterOrderId = masterOrderId;
    this.storeTypeMsg = storeTypeMsg;
    this.createdDate = createdDate;
    this.storeOrderId = storeOrderId;
    this.autoAcceptOrders = autoAcceptOrders;
    this.pickupAddress = pickupAddress;
    this.storeLogo = storeLogo;
    this.id = id;
    this.billingAddress = billingAddress;
    this.storePlan = storePlan;
    this.status = status;
    this.storeRatingData = storeRatingData;
    this.driverRatingData = driverRatingData;
  }

  protected OrderHistStoreOrderData(Parcel in) {
    requestedFor = in.readString();
    orderType = in.readString();
    paymentTypeText = in.readString();
    driverTypeId = in.readString();
    timestamps = in.readParcelable(OrderTimeStampData.class.getClassLoader());
    createdTimeStamp = in.readString();
    orderTypeMsg = in.readString();
    accounting = in.readParcelable(OrderHistoryAccountingData.class.getClassLoader());
    paymentType = in.readString();
    products = in.createTypedArrayList(OrderHistProductData.CREATOR);
    requestedForTimeStamp = in.readString();
    autoDispatch = in.readString();
    deliveryAddress = in.readParcelable(CartPickupAddressData.class.getClassLoader());
    storePhone = in.readString();
    customerId = in.readString();
    storeName = in.readString();
    driverDetailsData = in.readParcelable(DriverDetailsData.class.getClassLoader());
    customerDetails = in.readParcelable(OrderHistoryCustomerData.class.getClassLoader());
    freeDeliveryLimitPerStore = in.readString();
    payByWallet = in.readString();
    storeType = in.readString();
    cartId = in.readString();
    storeId = in.readString();
    driverType = in.readString();
    masterOrderId = in.readString();
    storeTypeMsg = in.readString();
    createdDate = in.readString();
    storeOrderId = in.readString();
    autoAcceptOrders = in.readString();
    pickupAddress = in.readParcelable(CartPickupAddressData.class.getClassLoader());
    storeLogo = in.readParcelable(StoreLogoData.class.getClassLoader());
    id = in.readString();
    billingAddress = in.readParcelable(AddressListItemData.class.getClassLoader());
    storePlan = in.readParcelable(OrderHistoryPlanData.class.getClassLoader());
    status = in.readParcelable(OrderStatusData.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(requestedFor);
    dest.writeString(orderType);
    dest.writeString(paymentTypeText);
    dest.writeString(driverTypeId);
    dest.writeParcelable(timestamps, flags);
    dest.writeString(createdTimeStamp);
    dest.writeString(orderTypeMsg);
    dest.writeParcelable(accounting, flags);
    dest.writeString(paymentType);
    dest.writeTypedList(products);
    dest.writeString(requestedForTimeStamp);
    dest.writeString(autoDispatch);
    dest.writeParcelable(deliveryAddress, flags);
    dest.writeString(storePhone);
    dest.writeString(customerId);
    dest.writeString(storeName);
    dest.writeParcelable(customerDetails, flags);
    dest.writeParcelable(driverDetailsData, flags);
    dest.writeString(freeDeliveryLimitPerStore);
    dest.writeString(payByWallet);
    dest.writeString(storeType);
    dest.writeString(cartId);
    dest.writeString(storeId);
    dest.writeString(driverType);
    dest.writeString(masterOrderId);
    dest.writeString(storeTypeMsg);
    dest.writeString(createdDate);
    dest.writeString(storeOrderId);
    dest.writeString(autoAcceptOrders);
    dest.writeParcelable(pickupAddress, flags);
    dest.writeParcelable(storeLogo, flags);
    dest.writeString(id);
    dest.writeParcelable(billingAddress, flags);
    dest.writeParcelable(storePlan, flags);
    dest.writeParcelable(status, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistStoreOrderData> CREATOR = new Creator<OrderHistStoreOrderData>() {
    @Override
    public OrderHistStoreOrderData createFromParcel(Parcel in) {
      return new OrderHistStoreOrderData(in);
    }

    @Override
    public OrderHistStoreOrderData[] newArray(int size) {
      return new OrderHistStoreOrderData[size];
    }
  };

  public DriverDetailsData getDriverDetailsData() {
    return driverDetailsData;
  }

  public void setDriverDetailsData(DriverDetailsData driverDetailsData) {
    this.driverDetailsData = driverDetailsData;
  }

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

  public String getDriverTypeId() {
    return driverTypeId;
  }

  public void setDriverTypeId(String driverTypeId) {
    this.driverTypeId = driverTypeId;
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

  public ArrayList<OrderHistProductData> getProducts() {
    return products;
  }

  public void setProducts(
      ArrayList<OrderHistProductData> products) {
    this.products = products;
  }

  public String getRequestedForTimeStamp() {
    return requestedForTimeStamp;
  }

  public void setRequestedForTimeStamp(String requestedForTimeStamp) {
    this.requestedForTimeStamp = requestedForTimeStamp;
  }

  public String getAutoDispatch() {
    return autoDispatch;
  }

  public void setAutoDispatch(String autoDispatch) {
    this.autoDispatch = autoDispatch;
  }

  public CartPickupAddressData getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(CartPickupAddressData deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public String getStorePhone() {
    return storePhone;
  }

  public void setStorePhone(String storePhone) {
    this.storePhone = storePhone;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public OrderHistoryCustomerData getCustomerDetails() {
    return customerDetails;
  }

  public void setCustomerDetails(
      OrderHistoryCustomerData customerDetails) {
    this.customerDetails = customerDetails;
  }

  public String getFreeDeliveryLimitPerStore() {
    return freeDeliveryLimitPerStore;
  }

  public void setFreeDeliveryLimitPerStore(String freeDeliveryLimitPerStore) {
    this.freeDeliveryLimitPerStore = freeDeliveryLimitPerStore;
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

  public String getCartId() {
    return cartId;
  }

  public void setCartId(String cartId) {
    this.cartId = cartId;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getDriverType() {
    return driverType;
  }

  public void setDriverType(String driverType) {
    this.driverType = driverType;
  }

  public String getMasterOrderId() {
    return masterOrderId;
  }

  public void setMasterOrderId(String masterOrderId) {
    this.masterOrderId = masterOrderId;
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

  public String getStoreOrderId() {
    return storeOrderId;
  }

  public void setStoreOrderId(String storeOrderId) {
    this.storeOrderId = storeOrderId;
  }

  public String getAutoAcceptOrders() {
    return autoAcceptOrders;
  }

  public void setAutoAcceptOrders(String autoAcceptOrders) {
    this.autoAcceptOrders = autoAcceptOrders;
  }

  public CartPickupAddressData getPickupAddress() {
    return pickupAddress;
  }

  public void setPickupAddress(CartPickupAddressData pickupAddress) {
    this.pickupAddress = pickupAddress;
  }

  public StoreLogoData getStoreLogo() {
    return storeLogo;
  }

  public void setStoreLogo(StoreLogoData storeLogo) {
    this.storeLogo = storeLogo;
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

  public OrderHistoryPlanData getStorePlan() {
    return storePlan;
  }

  public void setStorePlan(OrderHistoryPlanData storePlan) {
    this.storePlan = storePlan;
  }

  public OrderStatusData getStatus() {
    return status;
  }

  public void setStatus(OrderStatusData status) {
    this.status = status;
  }

  public StoreRatingData getStoreRatingData() {
    return storeRatingData;
  }

  public void setStoreRatingData(StoreRatingData storeRatingData) {
    this.storeRatingData = storeRatingData;
  }

  public StoreRatingData getDriverRatingData() {
    return driverRatingData;
  }

  public void setDriverRatingData(StoreRatingData driverRatingData) {
    this.driverRatingData = driverRatingData;
  }
}
