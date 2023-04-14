package com.kotlintestgradle.model.orderdetails;



import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.getcart.CartPickupAddressData;
import com.kotlintestgradle.model.orderdetails.pharmacy.StoreLogoData;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryAccountingData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryCustomerData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryPlanData;
import com.kotlintestgradle.model.orderhistory.OrderStatusData;
import com.kotlintestgradle.model.orderhistory.OrderTimeStampData;

import java.util.ArrayList;

public class OrderData {
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
  private ArrayList<OrderHistProductData> alongWith;
  private String requestedForTimeStamp;
  private String autoDispatch;
  private AddressListItemData deliveryAddress;
  /*
    private String storePhone;
  */
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
  private ArrayList<String> packingDetails;
  private String storeTypeMsg;
  private String createdDate;
  private String storeOrderId;
  private String autoAcceptOrders;
  private CartPickupAddressData pickupAddress;
  private StoreLogoData storeLogo;
  private String storeEmail;
  private String id;
  private AddressListItemData billingAddress;
  private OrderHistoryPlanData storePlan;
  private OrderStatusData status;
  private ArrayList<DeliveryStatusData> deliveryStatus;
  private PackageBoxData packageBox;
  private PartnerData partnerData;

  public OrderData(String requestedFor, String orderType, String paymentTypeText,
                   String driverTypeId, OrderTimeStampData timestamps, String createdTimeStamp,
                   String orderTypeMsg, OrderHistoryAccountingData accounting, String paymentType,
                   ArrayList<OrderHistProductData> products, ArrayList<OrderHistProductData> alongWith,
                   String requestedForTimeStamp, PartnerData partnerData,
                   String autoDispatch, AddressListItemData deliveryAddress,
                   String customerId, String storeName,
                   OrderHistoryCustomerData customerDetails, String freeDeliveryLimitPerStore,
                   String payByWallet, String storeType, String cartId, String storeId,
                   String driverType, String masterOrderId, ArrayList<String> packingDetails,
                   String storeTypeMsg, String createdDate, String storeOrderId, String autoAcceptOrders,
                   CartPickupAddressData pickupAddress,
                   StoreLogoData storeLogo, String storeEmail, String id,
                   AddressListItemData billingAddress,
                   OrderHistoryPlanData storePlan, OrderStatusData status,
                   ArrayList<DeliveryStatusData> deliveryStatus,
                   PackageBoxData packageBox) {
    this.requestedFor = requestedFor;
    this.orderType = orderType;
    this.paymentTypeText = paymentTypeText;
    this.driverTypeId = driverTypeId;
    this.timestamps = timestamps;
    this.createdTimeStamp = createdTimeStamp;
    this.orderTypeMsg = orderTypeMsg;
    this.accounting = accounting;
    this.paymentType = paymentType;
    this.products = products;
    this.alongWith = alongWith;
    this.requestedForTimeStamp = requestedForTimeStamp;
    this.partnerData = partnerData;
    this.autoDispatch = autoDispatch;
    this.deliveryAddress = deliveryAddress;
    this.customerId = customerId;
    this.storeName = storeName;
    this.customerDetails = customerDetails;
    this.freeDeliveryLimitPerStore = freeDeliveryLimitPerStore;
    this.payByWallet = payByWallet;
    this.storeType = storeType;
    this.cartId = cartId;
    this.deliveryStatus = deliveryStatus;
    this.packageBox = packageBox;
    this.storeId = storeId;
    this.driverType = driverType;
    this.masterOrderId = masterOrderId;
    this.packingDetails = packingDetails;
    this.storeTypeMsg = storeTypeMsg;
    this.createdDate = createdDate;
    this.storeOrderId = storeOrderId;
    this.autoAcceptOrders = autoAcceptOrders;
    this.pickupAddress = pickupAddress;
    this.storeLogo = storeLogo;
    this.storeEmail = storeEmail;
    this.id = id;
    this.billingAddress = billingAddress;
    this.storePlan = storePlan;
    this.status = status;
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

  public ArrayList<OrderHistProductData> getAlongWith() {
    return alongWith;
  }

  public void setAlongWith(
      ArrayList<OrderHistProductData> alongWith) {
    this.alongWith = alongWith;
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

  public AddressListItemData getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(AddressListItemData deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  /* public String getStorePhone() {
     return storePhone;
   }

   public void setStorePhone(String storePhone) {
     this.storePhone = storePhone;
   }
 */
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

  public ArrayList<String> getPackingDetails() {
    return packingDetails;
  }

  public void setPackingDetails(ArrayList<String> packingDetails) {
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

  public String getStoreEmail() {
    return storeEmail;
  }

  public void setStoreEmail(String storeEmail) {
    this.storeEmail = storeEmail;
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

  public PartnerData getPartnerData() {
    return partnerData;
  }

  public void setPartnerData(PartnerData partnerData) {
    this.partnerData = partnerData;
  }
}
