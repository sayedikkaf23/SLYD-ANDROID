package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartPickupAddressDetails;

import java.util.ArrayList;

public class OrdersHistorySellerDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("targetAmtForFreeDelivery")
  private String targetAmtForFreeDelivery;
  @Expose
  @SerializedName("planData")
  private OrderHistoryPlanDetails planData;
  @Expose
  @SerializedName("fullFillMentCenterId")
  private String fullFillMentCenterId;
  @Expose
  @SerializedName("minOrder")
  private String minOrder;
  @Expose
  @SerializedName("driverType")
  private String driverType;
  @Expose
  @SerializedName("fullfilledBy")
  private String fullfilledBy;
  @Expose
  @SerializedName("contactPersonName")
  private String contactPersonName;
  @Expose
  @SerializedName("storeTax")
  private ArrayList<String> storeTax;
  @Expose
  @SerializedName("phone")
  private String phone;
  @Expose
  @SerializedName("pickupAddress")
  private CartPickupAddressDetails pickupAddress;
  @Expose
  @SerializedName("autoDispatch")
  private String autoDispatch;
  @Expose
  @SerializedName("autoAcceptOrders")
  private String autoAcceptOrders;
  @Expose
  @SerializedName("isInventoryCheck")
  private String isInventoryCheck;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("logo")
  private OrderHistorySellerLogoDetails logo;
  @Expose
  @SerializedName("contactPersonEmail")
  private String contactPersonEmail;

  public OrdersHistorySellerDetails(String targetAmtForFreeDelivery,
      OrderHistoryPlanDetails planData, String fullFillMentCenterId, String minOrder,
      String driverType, String fullfilledBy, String contactPersonName,
      ArrayList<String> storeTax, String phone,
      CartPickupAddressDetails pickupAddress, String autoDispatch, String autoAcceptOrders,
      String isInventoryCheck, String name,
      OrderHistorySellerLogoDetails logo, String contactPersonEmail) {
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

  protected OrdersHistorySellerDetails(Parcel in) {
    targetAmtForFreeDelivery = in.readString();
    planData = in.readParcelable(OrderHistoryPlanDetails.class.getClassLoader());
    fullFillMentCenterId = in.readString();
    minOrder = in.readString();
    driverType = in.readString();
    fullfilledBy = in.readString();
    contactPersonName = in.readString();
    storeTax = in.createStringArrayList();
    phone = in.readString();
    autoDispatch = in.readString();
    autoAcceptOrders = in.readString();
    isInventoryCheck = in.readString();
    name = in.readString();
    logo = in.readParcelable(OrderHistorySellerLogoDetails.class.getClassLoader());
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

  public static final Creator<OrdersHistorySellerDetails> CREATOR =
      new Creator<OrdersHistorySellerDetails>() {
        @Override
        public OrdersHistorySellerDetails createFromParcel(Parcel in) {
          return new OrdersHistorySellerDetails(in);
        }

        @Override
        public OrdersHistorySellerDetails[] newArray(int size) {
          return new OrdersHistorySellerDetails[size];
        }
      };

  public String getTargetAmtForFreeDelivery() {
    return targetAmtForFreeDelivery;
  }

  public void setTargetAmtForFreeDelivery(String targetAmtForFreeDelivery) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
  }

  public OrderHistoryPlanDetails getPlanData() {
    return planData;
  }

  public void setPlanData(
      OrderHistoryPlanDetails planData) {
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

  public CartPickupAddressDetails getPickupAddress() {
    return pickupAddress;
  }

  public void setPickupAddress(
      CartPickupAddressDetails pickupAddress) {
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

  public OrderHistorySellerLogoDetails getLogo() {
    return logo;
  }

  public void setLogo(
      OrderHistorySellerLogoDetails logo) {
    this.logo = logo;
  }

  public String getContactPersonEmail() {
    return contactPersonEmail;
  }

  public void setContactPersonEmail(String contactPersonEmail) {
    this.contactPersonEmail = contactPersonEmail;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
