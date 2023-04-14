package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.LogoImageData;
import java.util.ArrayList;

public class CartSellerData implements Parcelable {
  private String targetAmtForFreeDelivery;
  private String sellerCartValue;
  private String freeDelivery;
  private String fullFillMentCenterId;
  private String minOrder;
  private String driverType;
  private int driverTypeId;
  private LogoImageData logo;
  private ArrayList<CartProductItemData> products;
  private String fullfilledBy;
  /*
    private String storeTax;
  */
  /*
    private String phone;
  */
  private CartPickupAddressData pickupAddress;
  private String autoDispatch;
  private String autoAcceptOrders;
  private String isInventoryCheck;
  private String name;
  private String areaName;
  private String city;
  private int supportedOrderTypes;
  private LogoImageData logoImageData;
  private boolean storeIsOpen;
  private boolean minimumFeeEligible;
  private boolean freeDeliveryEligible;

  public CartSellerData(String targetAmtForFreeDelivery, String sellerCartValue,
      String freeDelivery, String fullFillMentCenterId, String minOrder, String driverType,
      ArrayList<CartProductItemData> products, String fullfilledBy,
      CartPickupAddressData pickupAddress, String autoDispatch,
      String autoAcceptOrders, String isInventoryCheck, String name) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
    this.sellerCartValue = sellerCartValue;
    this.freeDelivery = freeDelivery;
    this.fullFillMentCenterId = fullFillMentCenterId;
    this.minOrder = minOrder;
    this.driverType = driverType;
    this.products = products;
    this.fullfilledBy = fullfilledBy;
    this.pickupAddress = pickupAddress;
    this.autoDispatch = autoDispatch;
    this.autoAcceptOrders = autoAcceptOrders;
    this.isInventoryCheck = isInventoryCheck;
    this.name = name;
    this.areaName = areaName;
    this.city = city;
  }

  public CartSellerData(String targetAmtForFreeDelivery, String sellerCartValue,
                        String freeDelivery, String fullFillMentCenterId, String minOrder, String driverType,
                        ArrayList<CartProductItemData> products, String fullfilledBy,
                        CartPickupAddressData pickupAddress, String autoDispatch,
                        String autoAcceptOrders, String isInventoryCheck, String name, LogoImageData logo) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
    this.sellerCartValue = sellerCartValue;
    this.freeDelivery = freeDelivery;
    this.fullFillMentCenterId = fullFillMentCenterId;
    this.minOrder = minOrder;
    this.driverType = driverType;
    this.products = products;
    this.fullfilledBy = fullfilledBy;
    this.pickupAddress = pickupAddress;
    this.autoDispatch = autoDispatch;
    this.autoAcceptOrders = autoAcceptOrders;
    this.isInventoryCheck = isInventoryCheck;
    this.name = name;
    this.logo = logo;
  }

  public CartSellerData(String targetAmtForFreeDelivery, String sellerCartValue,
                        String freeDelivery, String fullFillMentCenterId, String minOrder, String driverType,
                        ArrayList<CartProductItemData> products, String fullfilledBy,
                        CartPickupAddressData pickupAddress, String autoDispatch,
                        String autoAcceptOrders, String isInventoryCheck,String areaName,
      String city, String name, LogoImageData logo) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
    this.sellerCartValue = sellerCartValue;
    this.freeDelivery = freeDelivery;
    this.fullFillMentCenterId = fullFillMentCenterId;
    this.minOrder = minOrder;
    this.driverType = driverType;
    this.products = products;
    this.fullfilledBy = fullfilledBy;
    this.pickupAddress = pickupAddress;
    this.autoDispatch = autoDispatch;
    this.autoAcceptOrders = autoAcceptOrders;
    this.isInventoryCheck = isInventoryCheck;
    this.name = name;
    this.logo = logo;
    this.areaName=areaName;
    this.city=city;
    this.driverTypeId = driverTypeId;
    this.supportedOrderTypes = supportedOrderTypes;
  }

  public CartSellerData(String targetAmtForFreeDelivery, String sellerCartValue,
                        String freeDelivery, String fullFillMentCenterId, String minOrder, String driverType,
                        ArrayList<CartProductItemData> products, String fullfilledBy,
                        CartPickupAddressData pickupAddress, String autoDispatch,
                        String autoAcceptOrders, String isInventoryCheck, String name, String areaName, String cityname,
                        LogoImageData logo,int supportedOrderTypes,boolean storeIsOpen,boolean minimumFeeEligible,boolean freeDeliveryEligible) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
    this.sellerCartValue = sellerCartValue;
    this.freeDelivery = freeDelivery;
    this.fullFillMentCenterId = fullFillMentCenterId;
    this.minOrder = minOrder;
    this.driverType = driverType;
    this.products = products;
    this.fullfilledBy = fullfilledBy;
    this.pickupAddress = pickupAddress;
    this.autoDispatch = autoDispatch;
    this.autoAcceptOrders = autoAcceptOrders;
    this.isInventoryCheck = isInventoryCheck;
    this.name = name;
    this.logo = logo;
    this.areaName = areaName;
    this.city = cityname;
    this.supportedOrderTypes = supportedOrderTypes;
    this.storeIsOpen = storeIsOpen;
    this.minimumFeeEligible = minimumFeeEligible;
    this.freeDeliveryEligible = freeDeliveryEligible;
  }

  public CartSellerData(String targetAmtForFreeDelivery, String sellerCartValue,
                        String freeDelivery, String fullFillMentCenterId, String minOrder, String driverType,
                        ArrayList<CartProductItemData> products, String fullfilledBy,
                        CartPickupAddressData pickupAddress, String autoDispatch,
                        String autoAcceptOrders, String isInventoryCheck, String name, String areaName, String cityname,
                        LogoImageData logo,int supportedOrderTypes,boolean storeIsOpen, int driverTypeId) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
    this.sellerCartValue = sellerCartValue;
    this.freeDelivery = freeDelivery;
    this.fullFillMentCenterId = fullFillMentCenterId;
    this.minOrder = minOrder;
    this.driverType = driverType;
    this.products = products;
    this.fullfilledBy = fullfilledBy;
    this.pickupAddress = pickupAddress;
    this.autoDispatch = autoDispatch;
    this.autoAcceptOrders = autoAcceptOrders;
    this.isInventoryCheck = isInventoryCheck;
    this.name = name;
    this.logo = logo;
    this.areaName = areaName;
    this.city = cityname;
    this.supportedOrderTypes = supportedOrderTypes;
    this.storeIsOpen = storeIsOpen;
    this.driverTypeId = driverTypeId;
  }

  protected CartSellerData(Parcel in) {
    targetAmtForFreeDelivery = in.readString();
    sellerCartValue = in.readString();
    freeDelivery = in.readString();
    fullFillMentCenterId = in.readString();
    minOrder = in.readString();
    driverType = in.readString();
    driverTypeId = in.readInt();
    logo = in.readParcelable(LogoImageData.class.getClassLoader());
    products = in.createTypedArrayList(CartProductItemData.CREATOR);
    fullfilledBy = in.readString();
    pickupAddress = in.readParcelable(CartPickupAddressData.class.getClassLoader());
    autoDispatch = in.readString();
    autoAcceptOrders = in.readString();
    isInventoryCheck = in.readString();
    name = in.readString();
    areaName = in.readString();
    city = in.readString();
    supportedOrderTypes = in.readInt();
    logoImageData = in.readParcelable(LogoImageData.class.getClassLoader());
    storeIsOpen = in.readByte() != 0;
    minimumFeeEligible = in.readByte() != 0;
    freeDeliveryEligible = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(targetAmtForFreeDelivery);
    dest.writeString(sellerCartValue);
    dest.writeString(freeDelivery);
    dest.writeString(fullFillMentCenterId);
    dest.writeString(minOrder);
    dest.writeString(driverType);
    dest.writeInt(driverTypeId);
    dest.writeParcelable(logo, flags);
    dest.writeTypedList(products);
    dest.writeString(fullfilledBy);
    dest.writeParcelable(pickupAddress, flags);
    dest.writeString(autoDispatch);
    dest.writeString(autoAcceptOrders);
    dest.writeString(isInventoryCheck);
    dest.writeString(name);
    dest.writeString(areaName);
    dest.writeString(city);
    dest.writeInt(supportedOrderTypes);
    dest.writeParcelable(logoImageData, flags);
    dest.writeByte((byte) (storeIsOpen ? 1 : 0));
    dest.writeByte((byte) (minimumFeeEligible ? 1 : 0));
    dest.writeByte((byte) (freeDeliveryEligible ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartSellerData> CREATOR = new Creator<CartSellerData>() {
    @Override
    public CartSellerData createFromParcel(Parcel in) {
      return new CartSellerData(in);
    }

    @Override
    public CartSellerData[] newArray(int size) {
      return new CartSellerData[size];
    }
  };

  public String getTargetAmtForFreeDelivery() {
    return targetAmtForFreeDelivery;
  }

  public void setTargetAmtForFreeDelivery(String targetAmtForFreeDelivery) {
    this.targetAmtForFreeDelivery = targetAmtForFreeDelivery;
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

  public ArrayList<CartProductItemData> getProducts() {
    return products;
  }

  public void setProducts(
      ArrayList<CartProductItemData> products) {
    this.products = products;
  }

  public LogoImageData getLogoImageData() {
    return logoImageData;
  }

  public void setLogoImageData(LogoImageData logoImageData) {
    this.logoImageData = logoImageData;
  }

  public boolean isMinimumFeeEligible() {
    return minimumFeeEligible;
  }

  public void setMinimumFeeEligible(boolean minimumFeeEligible) {
    this.minimumFeeEligible = minimumFeeEligible;
  }

  public boolean isFreeDeliveryEligible() {
    return freeDeliveryEligible;
  }

  public void setFreeDeliveryEligible(boolean freeDeliveryEligible) {
    this.freeDeliveryEligible = freeDeliveryEligible;
  }

  public String getFullfilledBy() {
    return fullfilledBy;
  }

  public void setFullfilledBy(String fullfilledBy) {
    this.fullfilledBy = fullfilledBy;
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

  public LogoImageData getLogo() {
    return logo;
  }

  public void setLogo(LogoImageData logo) {
    this.logo = logo;
  }

  public int getSupportedOrderTypes() {
    return supportedOrderTypes;
  }

  public void setSupportedOrderTypes(int supportedOrderTypes) {
    this.supportedOrderTypes = supportedOrderTypes;
  }

  public int getDriverTypeId() {
    return driverTypeId;
  }

  public boolean isStoreIsOpen() {
    return storeIsOpen;
  }

  public void setStoreIsOpen(boolean storeIsOpen) {
    this.storeIsOpen = storeIsOpen;
  }

  public void setDriverTypeId(int driverTypeId) {
    this.driverTypeId = driverTypeId;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
