package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;


import com.kotlintestgradle.model.ecom.getcart.CartPickupAddressData;

import java.util.ArrayList;

public class OrdersHistorySellerData  implements Parcelable {
  private String targetAmtForFreeDelivery;

  private OrderHistoryPlanData planData;

  private String fullFillMentCenterId;

  private String minOrder;

  private String driverType;

  private String fullfilledBy;

  private String contactPersonName;

  private ArrayList<String> storeTax;

  private String phone;

  private CartPickupAddressData pickupAddress;

  private String autoDispatch;

  private String autoAcceptOrders;

  private String isInventoryCheck;

  private String name;

  private OrderHistorySellerLogoData logo;

  private String contactPersonEmail;

  public OrdersHistorySellerData(String targetAmtForFreeDelivery,
      OrderHistoryPlanData planData, String fullFillMentCenterId, String minOrder,
      String driverType, String fullfilledBy, String contactPersonName,
      ArrayList<String> storeTax, String phone,
      CartPickupAddressData pickupAddress, String autoDispatch, String autoAcceptOrders,
      String isInventoryCheck, String name,
      OrderHistorySellerLogoData logo, String contactPersonEmail) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
    this.planData = planData;
    this.fullFillMentCenterId = fullFillMentCenterId;
    this.minOrder = minOrder;
    this.driverType = driverType;
    this.fullfilledBy = fullfilledBy;
    this.contactPersonName = contactPersonName;
    this.storeTax = storeTax;
    this.phone = phone;
    this.pickupAddress = pickupAddress;
    this.autoDispatch = autoDispatch;
    this.autoAcceptOrders = autoAcceptOrders;
    this.isInventoryCheck = isInventoryCheck;
    this.name = name;
    this.logo = logo;
    this.contactPersonEmail = contactPersonEmail;
  }

  protected OrdersHistorySellerData(Parcel in) {
    targetAmtForFreeDelivery = in.readString();
    planData = in.readParcelable(OrderHistoryPlanData.class.getClassLoader());
    fullFillMentCenterId = in.readString();
    minOrder = in.readString();
    driverType = in.readString();
    fullfilledBy = in.readString();
    contactPersonName = in.readString();
    storeTax = in.createStringArrayList();
    phone = in.readString();
    pickupAddress = in.readParcelable(CartPickupAddressData.class.getClassLoader());
    autoDispatch = in.readString();
    autoAcceptOrders = in.readString();
    isInventoryCheck = in.readString();
    name = in.readString();
    logo = in.readParcelable(OrderHistorySellerLogoData.class.getClassLoader());
    contactPersonEmail = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(targetAmtForFreeDelivery);
    dest.writeParcelable(planData, flags);
    dest.writeString(fullFillMentCenterId);
    dest.writeString(minOrder);
    dest.writeString(driverType);
    dest.writeString(fullfilledBy);
    dest.writeString(contactPersonName);
    dest.writeStringList(storeTax);
    dest.writeString(phone);
    dest.writeParcelable(pickupAddress, flags);
    dest.writeString(autoDispatch);
    dest.writeString(autoAcceptOrders);
    dest.writeString(isInventoryCheck);
    dest.writeString(name);
    dest.writeParcelable(logo, flags);
    dest.writeString(contactPersonEmail);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrdersHistorySellerData> CREATOR = new Creator<OrdersHistorySellerData>() {
    @Override
    public OrdersHistorySellerData createFromParcel(Parcel in) {
      return new OrdersHistorySellerData(in);
    }

    @Override
    public OrdersHistorySellerData[] newArray(int size) {
      return new OrdersHistorySellerData[size];
    }
  };

  public String getTargetAmtForFreeDelivery() {
    return targetAmtForFreeDelivery;
  }

  public void setTargetAmtForFreeDelivery(String targetAmtForFreeDelivery) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
  }

  public OrderHistoryPlanData getPlanData() {
    return planData;
  }

  public void setPlanData(OrderHistoryPlanData planData) {
    this.planData = planData;
  }

  public String getFullFillMentCenterId() {
    return fullFillMentCenterId;
  }

  public void setFullFillMentCenterId(String fullFillMentCenterId) {
    this.fullFillMentCenterId = fullFillMentCenterId;
  }

  public String getMinOrder() {
    return minOrder;
  }

  public void setMinOrder(String minOrder) {
    this.minOrder = minOrder;
  }

  public String getDriverType() {
    return driverType;
  }

  public void setDriverType(String driverType) {
    this.driverType = driverType;
  }

  public String getFullfilledBy() {
    return fullfilledBy;
  }

  public void setFullfilledBy(String fullfilledBy) {
    this.fullfilledBy = fullfilledBy;
  }

  public String getContactPersonName() {
    return contactPersonName;
  }

  public void setContactPersonName(String contactPersonName) {
    this.contactPersonName = contactPersonName;
  }

  public ArrayList<String> getStoreTax() {
    return storeTax;
  }

  public void setStoreTax(ArrayList<String> storeTax) {
    this.storeTax = storeTax;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public CartPickupAddressData getPickupAddress() {
    return pickupAddress;
  }

  public void setPickupAddress(CartPickupAddressData pickupAddress) {
    this.pickupAddress = pickupAddress;
  }

  public String getAutoDispatch() {
    return autoDispatch;
  }

  public void setAutoDispatch(String autoDispatch) {
    this.autoDispatch = autoDispatch;
  }

  public String getAutoAcceptOrders() {
    return autoAcceptOrders;
  }

  public void setAutoAcceptOrders(String autoAcceptOrders) {
    this.autoAcceptOrders = autoAcceptOrders;
  }

  public String getIsInventoryCheck() {
    return isInventoryCheck;
  }

  public void setIsInventoryCheck(String isInventoryCheck) {
    this.isInventoryCheck = isInventoryCheck;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrderHistorySellerLogoData getLogo() {
    return logo;
  }

  public void setLogo(OrderHistorySellerLogoData logo) {
    this.logo = logo;
  }

  public String getContactPersonEmail() {
    return contactPersonEmail;
  }

  public void setContactPersonEmail(String contactPersonEmail) {
    this.contactPersonEmail = contactPersonEmail;
  }
}
