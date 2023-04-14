package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.getcart.QuantityData;

import java.util.ArrayList;

public class OrderHistProductData implements Parcelable {

  private String upcNumber;
  private String color;
  private OrderTimeStampData timestamps;
  private SingleUnitPriceData singleUnitPrice;
  private OrderHistOfferData offerDetails;
  private String noofunits;
  private String productOrderId;
  private OrderHistoryAccountingData accounting;
  private String aisle;
  private String packageType;
  private String centralProductId;
  private String allowOrderOutOfStock;
  private OrderHistShippingData shippingDetails;
  private String unitId;
  private String productDeliveryFee;
  private String mouData;
  private ImageData images;
  private String brandName;
  private QuantityData quantity;
  private String productId;
  private ArrayList<String> addOns;
  private String packageId;
  private String currencySymbol;
  private OrderHistPackagingData packaging;
  private String shelf;
  private String isCentral;
  private String directions;
  private String name;
  private OrderHistRatingData rattingData;
  private ArrayList<OrderHistAttributeData> attributes;
  private String currencyCode;
  private OrderStatusData status;
  private String sellerName;
  private String invoiceLink;
  private String reason;
  private boolean isSplitProduct;
  int type;

  public OrderHistProductData(int type) {
    this.type = type;
  }

  public OrderHistProductData(String upcNumber, String color,
                              OrderTimeStampData timestamps,
                              SingleUnitPriceData singleUnitPrice,
                              OrderHistOfferData offerDetails, String productOrderId,
                              OrderHistoryAccountingData accounting, String aisle, String packageType,
                              String centralProductId, String allowOrderOutOfStock,
                              OrderHistShippingData shippingDetails, String unitId, String productDeliveryFee,
                              String mouData, ImageData images, String brandName,
                              QuantityData quantity, String productId, ArrayList<String> addOns, String packageId,
                              String currencySymbol, OrderHistPackagingData packaging, String shelf,
                              String isCentral, String directions, String name,
                              OrderHistRatingData rattingData,
                              ArrayList<OrderHistAttributeData> attributes, String currencyCode,
                              OrderStatusData status, String invoiceLink, String reason, boolean isSplitProduct) {
    this.upcNumber = upcNumber;
    this.color = color;
    this.timestamps = timestamps;
    this.singleUnitPrice = singleUnitPrice;
    this.offerDetails = offerDetails;
    this.productOrderId = productOrderId;
    this.accounting = accounting;
    this.aisle = aisle;
    this.packageType = packageType;
    this.centralProductId = centralProductId;
    this.allowOrderOutOfStock = allowOrderOutOfStock;
    this.shippingDetails = shippingDetails;
    this.unitId = unitId;
    this.productDeliveryFee = productDeliveryFee;
    this.mouData = mouData;
    this.images = images;
    this.brandName = brandName;
    this.quantity = quantity;
    this.productId = productId;
    this.addOns = addOns;
    this.packageId = packageId;
    this.currencySymbol = currencySymbol;
    this.packaging = packaging;
    this.shelf = shelf;
    this.isCentral = isCentral;
    this.directions = directions;
    this.name = name;
    this.rattingData = rattingData;
    this.attributes = attributes;
    this.currencyCode = currencyCode;
    this.status = status;
    this.invoiceLink = invoiceLink;
    this.isSplitProduct = isSplitProduct;
    this.reason = reason;
  }

  public OrderHistProductData() {
  }

  public OrderHistProductData(String productId) {
    this.productId = productId;
  }

  protected OrderHistProductData(Parcel in) {
    upcNumber = in.readString();
    color = in.readString();
    timestamps = in.readParcelable(OrderTimeStampData.class.getClassLoader());
    singleUnitPrice = in.readParcelable(SingleUnitPriceData.class.getClassLoader());
    offerDetails = in.readParcelable(OrderHistOfferData.class.getClassLoader());
    noofunits = in.readString();
    productOrderId = in.readString();
    accounting = in.readParcelable(OrderHistoryAccountingData.class.getClassLoader());
    aisle = in.readString();
    packageType = in.readString();
    centralProductId = in.readString();
    allowOrderOutOfStock = in.readString();
    shippingDetails = in.readParcelable(OrderHistShippingData.class.getClassLoader());
    unitId = in.readString();
    productDeliveryFee = in.readString();
    mouData = in.readString();
    images = in.readParcelable(ImageData.class.getClassLoader());
    brandName = in.readString();
    quantity = in.readParcelable(QuantityData.class.getClassLoader());
    productId = in.readString();
    addOns = in.createStringArrayList();
    packageId = in.readString();
    currencySymbol = in.readString();
    packaging = in.readParcelable(OrderHistPackagingData.class.getClassLoader());
    shelf = in.readString();
    isCentral = in.readString();
    directions = in.readString();
    name = in.readString();
    rattingData = in.readParcelable(OrderHistRatingData.class.getClassLoader());
    attributes = in.createTypedArrayList(OrderHistAttributeData.CREATOR);
    currencyCode = in.readString();
    status = in.readParcelable(OrderStatusData.class.getClassLoader());
    sellerName = in.readString();
    invoiceLink = in.readString();
    reason = in.readString();
    isSplitProduct = in.readByte() != 0;
    type = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(upcNumber);
    dest.writeString(color);
    dest.writeParcelable(timestamps, flags);
    dest.writeParcelable(singleUnitPrice, flags);
    dest.writeParcelable(offerDetails, flags);
    dest.writeString(noofunits);
    dest.writeString(productOrderId);
    dest.writeParcelable(accounting, flags);
    dest.writeString(aisle);
    dest.writeString(packageType);
    dest.writeString(centralProductId);
    dest.writeString(allowOrderOutOfStock);
    dest.writeParcelable(shippingDetails, flags);
    dest.writeString(unitId);
    dest.writeString(productDeliveryFee);
    dest.writeString(mouData);
    dest.writeParcelable(images, flags);
    dest.writeString(brandName);
    dest.writeParcelable(quantity, flags);
    dest.writeString(productId);
    dest.writeStringList(addOns);
    dest.writeString(packageId);
    dest.writeString(currencySymbol);
    dest.writeParcelable(packaging, flags);
    dest.writeString(shelf);
    dest.writeString(isCentral);
    dest.writeString(directions);
    dest.writeString(name);
    dest.writeParcelable(rattingData, flags);
    dest.writeTypedList(attributes);
    dest.writeString(currencyCode);
    dest.writeParcelable(status, flags);
    dest.writeString(sellerName);
    dest.writeString(invoiceLink);
    dest.writeString(reason);
    dest.writeByte((byte) (isSplitProduct ? 1 : 0));
    dest.writeInt(type);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistProductData> CREATOR = new Creator<OrderHistProductData>() {
    @Override
    public OrderHistProductData createFromParcel(Parcel in) {
      return new OrderHistProductData(in);
    }

    @Override
    public OrderHistProductData[] newArray(int size) {
      return new OrderHistProductData[size];
    }
  };

  public String getUpcNumber() {
    return upcNumber;
  }

  public void setUpcNumber(String upcNumber) {
    this.upcNumber = upcNumber;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public OrderTimeStampData getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(OrderTimeStampData timestamps) {
    this.timestamps = timestamps;
  }

  public SingleUnitPriceData getSingleUnitPrice() {
    return singleUnitPrice;
  }

  public void setSingleUnitPrice(
      SingleUnitPriceData singleUnitPrice) {
    this.singleUnitPrice = singleUnitPrice;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public OrderHistOfferData getOfferDetails() {
    return offerDetails;
  }

  public void setOfferDetails(OrderHistOfferData offerDetails) {
    this.offerDetails = offerDetails;
  }

  public String getNoofunits() {
    return noofunits;
  }

  public void setNoofunits(String noofunits) {
    this.noofunits = noofunits;
  }

  public String getProductOrderId() {
    return productOrderId;
  }

  public void setProductOrderId(String productOrderId) {
    this.productOrderId = productOrderId;
  }

  public OrderHistoryAccountingData getAccounting() {
    return accounting;
  }

  public void setAccounting(OrderHistoryAccountingData accounting) {
    this.accounting = accounting;
  }

  public String getAisle() {
    return aisle;
  }

  public void setAisle(String aisle) {
    this.aisle = aisle;
  }

  public String getPackageType() {
    return packageType;
  }

  public void setPackageType(String packageType) {
    this.packageType = packageType;
  }

  public String getCentralProductId() {
    return centralProductId;
  }

  public void setCentralProductId(String centralProductId) {
    this.centralProductId = centralProductId;
  }

  public String getAllowOrderOutOfStock() {
    return allowOrderOutOfStock;
  }

  public void setAllowOrderOutOfStock(String allowOrderOutOfStock) {
    this.allowOrderOutOfStock = allowOrderOutOfStock;
  }

  public OrderHistShippingData getShippingDetails() {
    return shippingDetails;
  }

  public void setShippingDetails(
      OrderHistShippingData shippingDetails) {
    this.shippingDetails = shippingDetails;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getProductDeliveryFee() {
    return productDeliveryFee;
  }

  public void setProductDeliveryFee(String productDeliveryFee) {
    this.productDeliveryFee = productDeliveryFee;
  }

  public String getMouData() {
    return mouData;
  }

  public void setMouData(String mouData) {
    this.mouData = mouData;
  }

  public ImageData getImages() {
    return images;
  }

  public void setImages(ImageData images) {
    this.images = images;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public QuantityData getQuantity() {
    return quantity;
  }

  public void setQuantity(QuantityData quantity) {
    this.quantity = quantity;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public ArrayList<String> getAddOns() {
    return addOns;
  }

  public void setAddOns(ArrayList<String> addOns) {
    this.addOns = addOns;
  }

  public String getPackageId() {
    return packageId;
  }

  public void setPackageId(String packageId) {
    this.packageId = packageId;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public OrderHistPackagingData getPackaging() {
    return packaging;
  }

  public void setPackaging(OrderHistPackagingData packaging) {
    this.packaging = packaging;
  }

  public String getShelf() {
    return shelf;
  }

  public void setShelf(String shelf) {
    this.shelf = shelf;
  }

  public String getIsCentral() {
    return isCentral;
  }

  public void setIsCentral(String isCentral) {
    this.isCentral = isCentral;
  }

  public String getDirections() {
    return directions;
  }

  public void setDirections(String directions) {
    this.directions = directions;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrderHistRatingData getRattingData() {
    return rattingData;
  }

  public void setRattingData(OrderHistRatingData rattingData) {
    this.rattingData = rattingData;
  }

  public ArrayList<OrderHistAttributeData> getAttributes() {
    return attributes;
  }

  public void setAttributes(
      ArrayList<OrderHistAttributeData> attributes) {
    this.attributes = attributes;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public OrderStatusData getStatus() {
    return status;
  }

  public void setStatus(OrderStatusData status) {
    this.status = status;
  }

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  public String getInvoiceLink() {
    return invoiceLink;
  }

  public void setInvoiceLink(String invoiceLink) {
    this.invoiceLink = invoiceLink;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public boolean isSplitProduct() {
    return isSplitProduct;
  }

  public void setSplitProduct(boolean splitProduct) {
    isSplitProduct = splitProduct;
  }

  @Override
  public boolean equals(Object o) {
    OrderHistProductData orderHistProductData = (OrderHistProductData) o;
    if (orderHistProductData != null && !TextUtils.isEmpty(orderHistProductData.productId)) {
      return this.productId.equals(orderHistProductData.productId);
    } else {
      return false;
    }
  }
}
