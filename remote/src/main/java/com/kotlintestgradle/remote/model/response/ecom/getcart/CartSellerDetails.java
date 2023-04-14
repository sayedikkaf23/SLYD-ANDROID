package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class CartSellerDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("targetAmtForFreeDelivery")
  private String targetAmtForFreeDelivery;

  @Expose
  @SerializedName("sellerCartValue")
  private String sellerCartValue;

  @Expose
  @SerializedName("freeDelivery")
  private String freeDelivery;

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
  @SerializedName("products")
  private ArrayList<CartProductItemDetails> products;

  @Expose
  @SerializedName("fullfilledBy")
  private String fullfilledBy;

  @Expose
  @SerializedName("logo")
  private LogoImages logo;

 /* @Expose
  @SerializedName("storeTax")
  private String storeTax;*/

 /* @Expose
  @SerializedName("phone")
  private String phone;*/

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
  @SerializedName("driverTypeId")
  private int driverTypeId;

  @Expose
  @SerializedName("areaName")
  private String areaName;

  @Expose
  @SerializedName("cityName")
  private String cityName;

  @Expose
  @SerializedName("supportedOrderTypes")
  private int supportedOrderTypes;

  @Expose
  @SerializedName("storeIsOpen")
  private boolean storeIsOpen;

  @Expose
  @SerializedName("minOrderSatisfy")
  private boolean minOrderSatisfy;

  @Expose
  @SerializedName("freeDeliverySatisfy")
  private boolean freeDeliverySatisfy;

  /*@Expose
  @SerializedName("logo")
  private String logo;*/

  public CartSellerDetails(String targetAmtForFreeDelivery, String sellerCartValue,
      String freeDelivery, String fullFillMentCenterId, String minOrder, String driverType,
      ArrayList<CartProductItemDetails> products, String fullfilledBy,

      CartPickupAddressDetails pickupAddress, String autoDispatch, String autoAcceptOrders,
      String isInventoryCheck, String name,String cityName,String areaName,LogoImages logoImages) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
    this.sellerCartValue = sellerCartValue;
    this.freeDelivery = freeDelivery;
    this.fullFillMentCenterId = fullFillMentCenterId;
    this.minOrder = minOrder;
    this.driverType = driverType;
    this.products = products;
    this.fullfilledBy = fullfilledBy;
    this.areaName = areaName;
    this.cityName = cityName;
    this.logo = logoImages;
/*
    this.storeTax = storeTax;
*/
/*
    this.phone = phone;
*/
    this.pickupAddress = pickupAddress;
    this.autoDispatch = autoDispatch;
    this.autoAcceptOrders = autoAcceptOrders;
    this.isInventoryCheck = isInventoryCheck;
    this.name = name;
/*
    this.logo = logo;
*/
  }


  protected CartSellerDetails(Parcel in) {
    targetAmtForFreeDelivery = in.readString();
    sellerCartValue = in.readString();
    freeDelivery = in.readString();
    fullFillMentCenterId = in.readString();
    minOrder = in.readString();
    driverType = in.readString();
    products = in.createTypedArrayList(CartProductItemDetails.CREATOR);
    fullfilledBy = in.readString();
    logo = in.readParcelable(LogoImages.class.getClassLoader());
    pickupAddress = in.readParcelable(CartPickupAddressDetails.class.getClassLoader());
    autoDispatch = in.readString();
    autoAcceptOrders = in.readString();
    isInventoryCheck = in.readString();
    name = in.readString();
    driverTypeId = in.readInt();
    areaName = in.readString();
    cityName = in.readString();
    supportedOrderTypes = in.readInt();
    storeIsOpen = in.readByte() != 0;
    minOrderSatisfy = in.readByte() != 0;
    freeDeliverySatisfy = in.readByte() != 0;
  }

  public static final Creator<CartSellerDetails> CREATOR = new Creator<CartSellerDetails>() {
    @Override
    public CartSellerDetails createFromParcel(Parcel in) {
      return new CartSellerDetails(in);
    }

    @Override
    public CartSellerDetails[] newArray(int size) {
      return new CartSellerDetails[size];
    }
  };

  public String getTargetAmtForFreeDelivery() {
    return targetAmtForFreeDelivery;
  }

  public void setTargetAmtForFreeDelivery(String targetAmtForFreeDelivery) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
  }

  public boolean isMinOrderSatisfy() {
    return minOrderSatisfy;
  }

  public void setMinOrderSatisfy(boolean minOrderSatisfy) {
    this.minOrderSatisfy = minOrderSatisfy;
  }

  public boolean isFreeDeliverySatisfy() {
    return freeDeliverySatisfy;
  }

  public void setFreeDeliverySatisfy(boolean freeDeliverySatisfy) {
    this.freeDeliverySatisfy = freeDeliverySatisfy;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getSellerCartValue() {
    return sellerCartValue;
  }

  public void setSellerCartValue(String sellerCartValue) {
    this.sellerCartValue = sellerCartValue;
  }

  public String getFreeDelivery() {
    return freeDelivery;
  }

  public void setFreeDelivery(String freeDelivery) {
    this.freeDelivery = freeDelivery;
  }

  public boolean isStoreIsOpen() {
    return storeIsOpen;
  }

  public void setStoreIsOpen(boolean storeIsOpen) {
    this.storeIsOpen = storeIsOpen;
  }

  public String getFullFillMentCenterId() {
    return fullFillMentCenterId;
  }

  public void setFullFillMentCenterId(String fullFillMentCenterId) {
    this.fullFillMentCenterId = fullFillMentCenterId;
  }

  public int getSupportedOrderTypes() {
    return supportedOrderTypes;
  }

  public void setSupportedOrderTypes(int supportedOrderTypes) {
    this.supportedOrderTypes = supportedOrderTypes;
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

  public ArrayList<CartProductItemDetails> getProducts() {
    return products;
  }

  public void setProducts(
      ArrayList<CartProductItemDetails> products) {
    this.products = products;
  }

  public String getFullfilledBy() {
    return fullfilledBy;
  }

  public void setFullfilledBy(String fullfilledBy) {
    this.fullfilledBy = fullfilledBy;
  }

 /* public String getStoreTax() {
    return storeTax;
  }

  public void setStoreTax(String storeTax) {
    this.storeTax = storeTax;
  }
*/
 /* public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
*/
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

  public LogoImages getLogo() {
    return logo;
  }

  public void setLogo(LogoImages logo) {
    this.logo = logo;
  }

  public int getDriverTypeId() {
    return driverTypeId;
  }

  public void setDriverTypeId(int driverTypeId) {
    this.driverTypeId = driverTypeId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(targetAmtForFreeDelivery);
    parcel.writeString(sellerCartValue);
    parcel.writeString(freeDelivery);
    parcel.writeString(fullFillMentCenterId);
    parcel.writeString(minOrder);
    parcel.writeString(driverType);
    parcel.writeTypedList(products);
    parcel.writeString(fullfilledBy);
    parcel.writeParcelable(logo, i);
    parcel.writeParcelable(pickupAddress, i);
    parcel.writeString(autoDispatch);
    parcel.writeString(autoAcceptOrders);
    parcel.writeString(isInventoryCheck);
    parcel.writeString(name);
    parcel.writeInt(driverTypeId);
    parcel.writeString(areaName);
    parcel.writeString(cityName);
    parcel.writeInt(supportedOrderTypes);
    parcel.writeByte((byte) (storeIsOpen ? 1 : 0));
    parcel.writeByte((byte) (minOrderSatisfy ? 1 : 0));
    parcel.writeByte((byte) (freeDeliverySatisfy ? 1 : 0));
  }

  @Override
  public boolean isValid() {
    return true;
  }

  /* public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }*/
}
