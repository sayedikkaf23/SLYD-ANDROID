package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.QuantityDetails;

import java.util.ArrayList;

public class OrderHistProductDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("upcNumber")
  private String upcNumber;
  @Expose
  @SerializedName("color")
  private String color;
  @Expose
  @SerializedName("timestamps")
  private OrderTimeStampDetails timestamps;
  @Expose
  @SerializedName("singleUnitPrice")
  private SingleUnitPriceDetails singleUnitPrice;
  @Expose
  @SerializedName("offerDetails")
  private OrderHistOfferDetails offerDetails;
 /* @Expose
  @SerializedName("noofunits")
  private String noofunits;*/
  @Expose
  @SerializedName("productOrderId")
  private String productOrderId;
  @Expose
  @SerializedName("accounting")
  private OrderHistoryAccounting accounting;
  @Expose
  @SerializedName("aisle")
  private String aisle;
  @Expose
  @SerializedName("packageType")
  private String packageType;
  @Expose
  @SerializedName("centralProductId")
  private String centralProductId;
  @Expose
  @SerializedName("allowOrderOutOfStock")
  private String allowOrderOutOfStock;
  @Expose
  @SerializedName("shippingDetails")
  private OrderHistShippingDetails shippingDetails;
  @Expose
  @SerializedName("unitId")
  private String unitId;
  @Expose
  @SerializedName("productDeliveryFee")
  private String productDeliveryFee;
 /* @Expose
  @SerializedName("mouData")
  private String mouData;*/
  @Expose
  @SerializedName("images")
  private ImagesDetails images;
  @Expose
  @SerializedName("brandName")
  private String brandName;
  @Expose
  @SerializedName("quantity")
  private QuantityDetails quantity;
  @Expose
  @SerializedName("productId")
  private String productId;
  @Expose
  @SerializedName("addOns")
  private ArrayList<String> addOns;
  @Expose
  @SerializedName("packageId")
  private String packageId;
  @Expose
  @SerializedName("currencySymbol")
  private String currencySymbol;
  @Expose
  @SerializedName("packaging")
  private OrderHistPackagingDetails packaging;
  @Expose
  @SerializedName("shelf")
  private String shelf;
  @Expose
  @SerializedName("isCentral")
  private String isCentral;
  @Expose
  @SerializedName("directions")
  private String directions;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("rattingData")
  private OrderHistRatingDetails rattingData;
  @Expose
  @SerializedName("attributes")
  private ArrayList<OrderHistAttributeDetails> attributes;
  @Expose
  @SerializedName("currencyCode")
  private String currencyCode;
  @Expose
  @SerializedName("status")
  private OrderStatusDetails status;
  @Expose
  @SerializedName("invoiceLink")
  private String invoiceLink;
  @Expose
  @SerializedName("reason")
  private String reason;
  @Expose
  @SerializedName("isSplitProduct")
  private Boolean isSplitProduct;
  public OrderHistProductDetails(String upcNumber, String color,
      OrderTimeStampDetails timestamps,
      SingleUnitPriceDetails singleUnitPrice,
      OrderHistOfferDetails offerDetails, String noofunits, String productOrderId,
      OrderHistoryAccounting accounting, String aisle, String packageType,
      String centralProductId, String allowOrderOutOfStock,
      OrderHistShippingDetails shippingDetails, String unitId, String productDeliveryFee,
       ImagesDetails images, String brandName,
      QuantityDetails quantity, String productId, ArrayList<String> addOns,
      String packageId, String currencySymbol,
      OrderHistPackagingDetails packaging, String shelf, String isCentral,
      String directions, String name,
      OrderHistRatingDetails rattingData,
      ArrayList<OrderHistAttributeDetails> attributes, String currencyCode,
      OrderStatusDetails status,String invoiceLink,Boolean isSplitProduct,String reason) {
    this.upcNumber = upcNumber;
    this.color = color;
    this.timestamps = timestamps;
    this.singleUnitPrice = singleUnitPrice;
    this.offerDetails = offerDetails;
/*
    this.noofunits = noofunits;
*/
    this.productOrderId = productOrderId;
    this.accounting = accounting;
    this.aisle = aisle;
    this.packageType = packageType;
    this.centralProductId = centralProductId;
    this.allowOrderOutOfStock = allowOrderOutOfStock;
    this.shippingDetails = shippingDetails;
    this.unitId = unitId;
    this.productDeliveryFee = productDeliveryFee;
  //  this.mouData = mouData;
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
    this.invoiceLink=invoiceLink;
    this.isSplitProduct=isSplitProduct;
    this.reason=reason;
  }

  protected OrderHistProductDetails(Parcel in) {
    upcNumber = in.readString();
    color = in.readString();
    offerDetails = in.readParcelable(OrderHistOfferDetails.class.getClassLoader());
/*
    noofunits = in.readString();
*/
    productOrderId = in.readString();
    accounting = in.readParcelable(OrderHistoryAccounting.class.getClassLoader());
    aisle = in.readString();
    packageType = in.readString();
    centralProductId = in.readString();
    allowOrderOutOfStock = in.readString();
    unitId = in.readString();
    productDeliveryFee = in.readString();
    brandName = in.readString();
    productId = in.readString();
    addOns = in.createStringArrayList();
    packageId = in.readString();
    currencySymbol = in.readString();
    packaging = in.readParcelable(OrderHistPackagingDetails.class.getClassLoader());
    shelf = in.readString();
    isCentral = in.readString();
    directions = in.readString();
    name = in.readString();
    attributes = in.createTypedArrayList(OrderHistAttributeDetails.CREATOR);
    currencyCode = in.readString();
    invoiceLink = in.readString();
    reason = in.readString();
    byte tmpIsSplitProduct = in.readByte();
    isSplitProduct = tmpIsSplitProduct == 0 ? null : tmpIsSplitProduct == 1;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(upcNumber);
    dest.writeString(color);
    dest.writeParcelable(offerDetails, flags);
/*
    dest.writeString(noofunits);
*/
    dest.writeString(productOrderId);
    dest.writeParcelable(accounting, flags);
    dest.writeString(aisle);
    dest.writeString(packageType);
    dest.writeString(centralProductId);
    dest.writeString(allowOrderOutOfStock);
    dest.writeString(unitId);
    dest.writeString(productDeliveryFee);
    dest.writeString(brandName);
    dest.writeString(productId);
    dest.writeStringList(addOns);
    dest.writeString(packageId);
    dest.writeString(currencySymbol);
    dest.writeParcelable(packaging, flags);
    dest.writeString(shelf);
    dest.writeString(isCentral);
    dest.writeString(directions);
    dest.writeString(name);
    dest.writeTypedList(attributes);
    dest.writeString(currencyCode);
    dest.writeString(invoiceLink);
    dest.writeString(reason);
    dest.writeByte((byte) (isSplitProduct == null ? 0 : isSplitProduct ? 1 : 2));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistProductDetails> CREATOR =
      new Creator<OrderHistProductDetails>() {
        @Override
        public OrderHistProductDetails createFromParcel(Parcel in) {
          return new OrderHistProductDetails(in);
        }

        @Override
        public OrderHistProductDetails[] newArray(int size) {
          return new OrderHistProductDetails[size];
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

  public OrderTimeStampDetails getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(
      OrderTimeStampDetails timestamps) {
    this.timestamps = timestamps;
  }

  public SingleUnitPriceDetails getSingleUnitPrice() {
    return singleUnitPrice;
  }

  public void setSingleUnitPrice(
      SingleUnitPriceDetails singleUnitPrice) {
    this.singleUnitPrice = singleUnitPrice;
  }

  public OrderHistOfferDetails getOfferDetails() {
    return offerDetails;
  }

  public void setOfferDetails(OrderHistOfferDetails offerDetails) {
    this.offerDetails = offerDetails;
  }

  /*public String getNoofunits() {
    return noofunits;
  }

  public void setNoofunits(String noofunits) {
    this.noofunits = noofunits;
  }*/

  public String getProductOrderId() {
    return productOrderId;
  }

  public void setProductOrderId(String productOrderId) {
    this.productOrderId = productOrderId;
  }

  public OrderHistoryAccounting getAccounting() {
    return accounting;
  }

  public void setAccounting(
      OrderHistoryAccounting accounting) {
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

  public OrderHistShippingDetails getShippingDetails() {
    return shippingDetails;
  }

  public void setShippingDetails(
      OrderHistShippingDetails shippingDetails) {
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

 /* public String getMouData() {
    return mouData;
  }

  public void setMouData(String mouData) {
    this.mouData = mouData;
  }*/

  public ImagesDetails getImages() {
    return images;
  }

  public void setImages(ImagesDetails images) {
    this.images = images;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public QuantityDetails getQuantity() {
    return quantity;
  }

  public void setQuantity(QuantityDetails quantity) {
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

  public OrderHistPackagingDetails getPackaging() {
    return packaging;
  }

  public void setPackaging(
      OrderHistPackagingDetails packaging) {
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

  public OrderHistRatingDetails getRattingData() {
    return rattingData;
  }

  public void setRattingData(
      OrderHistRatingDetails rattingData) {
    this.rattingData = rattingData;
  }

  public ArrayList<OrderHistAttributeDetails> getAttributes() {
    return attributes;
  }

  public void setAttributes(
      ArrayList<OrderHistAttributeDetails> attributes) {
    this.attributes = attributes;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public OrderStatusDetails getStatus() {
    return status;
  }

  public void setStatus(OrderStatusDetails status) {
    this.status = status;
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

  public Boolean getSplitProduct() {
    return isSplitProduct;
  }

  public void setSplitProduct(Boolean splitProduct) {
    isSplitProduct = splitProduct;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
