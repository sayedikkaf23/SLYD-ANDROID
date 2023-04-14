package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.customer.remote.http.model.response.getcart.SelectedAddOns;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import java.util.ArrayList;

public class CartProductItemDetails implements Parcelable, ValidItem {
  @Expose
  @SerializedName("upcNumber")
  private String upcNumber;
  @Expose
  @SerializedName("color")
  private String color;
  @Expose
  @SerializedName("originalPrice")
  private String originalPrice;
/*  @Expose
  @SerializedName("noofunits")
  private String noofunits;*/
  @Expose
  @SerializedName("availableQuantity")
  private int availableQuantity;

  @Expose
  @SerializedName("discount")
  private String discount;
  @Expose
  @SerializedName("finalPrice")
  private String finalPrice;
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
  @SerializedName("offerType")
  private String offerType;
  @Expose
  @SerializedName("deliveryDetails")
  private CartDeliveryDetails deliveryDetails;
  @Expose
  @SerializedName("unitId")
  private String unitId;
  @Expose
  @SerializedName("storeName")
  private String storeName;
  @Expose
  @SerializedName("inStock")
  private String inStock;

  /* @Expose
   @SerializedName("mouData")
  private String mouData;*/
  @Expose
  @SerializedName("offerTitle")
  private String offerTitle;
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
  @SerializedName("originalPriceTotal")
  private String originalPriceTotal;
/*  @Expose
  @SerializedName("addOns")
  private ArrayList<String> addOns;*/
  @Expose
  @SerializedName("currencySymbol")
  private String currencySymbol;
  @Expose
  @SerializedName("accounting")
  private CartAccountingDetails accounting;
  @Expose
  @SerializedName("offerDetails")
  private CartOfferDetails offerDetails;
  /*@Expose
   @SerializedName("packaging")
  private String packaging;
*/
  @Expose
  @SerializedName("tax")
  private ArrayList<CartTaxDetails> tax;
  @Expose
  @SerializedName("storeId")
  private String storeId;
  @Expose
  @SerializedName("productTaxArray")
  private ArrayList<CartTaxDetails> productTaxArray;
  @Expose
  @SerializedName("shelf")
  private String shelf;
  @Expose
  @SerializedName("offerDiscount")
  private String offerDiscount;
  @Expose
  @SerializedName("isCentral")
  private String isCentral;
  @Expose
  @SerializedName("totalAppliedTaxOnProduct")
  private String totalAppliedTaxOnProduct;
  @Expose
  @SerializedName("discountedPrice")
  private String discountedPrice;
  @Expose
  @SerializedName("directions")
  private String directions;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("offerId")
  private String offerId;
  @Expose
  @SerializedName("offerValue")
  private String offerValue;
  @Expose
  @SerializedName("attributes")
  private ArrayList<CartAttributesDetails> attributes;
  @Expose
  @SerializedName("_id")
  private String _id;
  @Expose
  @SerializedName("currencyCode")
  private String currencyCode;
  @Expose
  @SerializedName("manufactureName")
  private String manufactureName;
  @Expose
  @SerializedName("prescriptionRequired")
  private boolean prescriptionRequired;
  @Expose
  @SerializedName("mouDataUnit")
  private String mouDataUnit;
  @Expose
  @SerializedName("productQuantity")
  private String productQuantity;

  @Expose
  @SerializedName("addToCartOnId")
  private String addToCartOnId;

  @Expose
  @SerializedName("addOnAvailable")
  private int addOnAvailable;

  @Expose
  @SerializedName("containsMeat")
  private boolean containsMeat;

  @Expose
  @SerializedName("selectedAddOns")
  private ArrayList<SelectedAddOns> selectedAddOns;

  public ArrayList<SelectedAddOns> getSelectedAddOns() {
    return selectedAddOns;
  }

  public void setSelectedAddOns(ArrayList<SelectedAddOns> selectedAddOns) {
    this.selectedAddOns = selectedAddOns;
  }

  public CartProductItemDetails(String upcNumber, String color, String originalPrice, String discount, String finalPrice, String aisle,
                                String packageType, String centralProductId, String offerType, String offerValue,
                                CartDeliveryDetails deliveryDetails, String unitId, String storeName, String inStock,
                                String offerTitle,
                                ImagesDetails images, String brandName,
                                QuantityDetails quantity, String originalPriceTotal,
                                ArrayList<String> addOns, String currencySymbol,
                                ArrayList<CartTaxDetails> tax, String storeId,
                                ArrayList<CartTaxDetails> productTaxArray, String shelf, String offerDiscount,
                                String isCentral, String totalAppliedTaxOnProduct, String discountedPrice,
                                String directions, String name, String offerId,
                                ArrayList<CartAttributesDetails> attributes, String _id, String currencyCode, CartAccountingDetails accounting,
                                CartOfferDetails offerDetails, Integer availableQuantity, int addOnAvailable, boolean containsMeat) {
    this.upcNumber = upcNumber;
    this.color = color;
    this.originalPrice = originalPrice;
/*
    this.noofunits = noofunits;
*/
    this.discount = discount;
    this.finalPrice = finalPrice;
    this.aisle = aisle;
    this.packageType = packageType;
    this.centralProductId = centralProductId;
    this.offerType = offerType;
    this.offerValue=offerValue;
    this.deliveryDetails = deliveryDetails;
    this.unitId = unitId;
    this.storeName = storeName;
    this.inStock = inStock;
/*
    this.mouData = mouData;
*/
    this.offerTitle = offerTitle;
    this.images = images;
    this.brandName = brandName;
    this.quantity = quantity;
    this.originalPriceTotal = originalPriceTotal;
   // this.addOns = addOns;
    this.currencySymbol = currencySymbol;
/*
    this.packaging = packaging;
*/
    this.tax = tax;
    this.storeId = storeId;
    this.productTaxArray = productTaxArray;
    this.shelf = shelf;
    this.offerDiscount = offerDiscount;
    this.isCentral = isCentral;
    this.totalAppliedTaxOnProduct = totalAppliedTaxOnProduct;
    this.discountedPrice = discountedPrice;
    this.directions = directions;
    this.name = name;
    this.offerId = offerId;
    this.attributes = attributes;
    this._id = _id;
    this.currencyCode = currencyCode;
    this.accounting=accounting;
    this.offerDetails=offerDetails;
    this.addOnAvailable =addOnAvailable;
    this.containsMeat =containsMeat;
   // this.availableQuantity=availableQuantity;
  }

  protected CartProductItemDetails(Parcel in) {
    upcNumber = in.readString();
    color = in.readString();
    originalPrice = in.readString();
/*
    noofunits = in.readString();
*/
    availableQuantity = in.readInt();
    discount = in.readString();
    finalPrice = in.readString();
    aisle = in.readString();
    packageType = in.readString();
    centralProductId = in.readString();
    offerType = in.readString();
    unitId = in.readString();
    storeName = in.readString();
    inStock = in.readString();
    offerTitle = in.readString();
    images = in.readParcelable(ImagesDetails.class.getClassLoader());
    brandName = in.readString();
    originalPriceTotal = in.readString();
   // addOns = in.createStringArrayList();
    currencySymbol = in.readString();
    accounting = in.readParcelable(CartAccountingDetails.class.getClassLoader());
    offerDetails = in.readParcelable(CartOfferDetails.class.getClassLoader());
    tax = in.createTypedArrayList(CartTaxDetails.CREATOR);
    storeId = in.readString();
    productTaxArray = in.createTypedArrayList(CartTaxDetails.CREATOR);
    shelf = in.readString();
    offerDiscount = in.readString();
    isCentral = in.readString();
    totalAppliedTaxOnProduct = in.readString();
    discountedPrice = in.readString();
    directions = in.readString();
    name = in.readString();
    offerId = in.readString();
    offerValue = in.readString();
    attributes = in.createTypedArrayList(CartAttributesDetails.CREATOR);
    _id = in.readString();
    currencyCode = in.readString();
    addToCartOnId = in.readString();

  }

  public int getAddOnAvailable() {
    return addOnAvailable;
  }

  public void setAddOnAvailable(int addOnAvailable) {
    this.addOnAvailable = addOnAvailable;
  }

  public boolean isContainsMeat() {
    return containsMeat;
  }

  public void setContainsMeat(boolean containsMeat) {
    this.containsMeat = containsMeat;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(upcNumber);
    dest.writeString(color);
    dest.writeString(originalPrice);
/*
    dest.writeString(noofunits);
*/
   // dest.writeInt(availableQuantity);
    dest.writeString(discount);
    dest.writeString(finalPrice);
    dest.writeString(aisle);
    dest.writeString(packageType);
    dest.writeString(centralProductId);
    dest.writeString(offerType);
    dest.writeString(unitId);
    dest.writeString(storeName);
    dest.writeString(inStock);
    dest.writeString(offerTitle);
    dest.writeParcelable(images, flags);
    dest.writeString(brandName);
    dest.writeString(originalPriceTotal);
   // dest.writeStringList(addOns);
    dest.writeString(currencySymbol);
    dest.writeParcelable(accounting, flags);
    dest.writeParcelable(offerDetails, flags);
    dest.writeTypedList(tax);
    dest.writeString(storeId);
    dest.writeTypedList(productTaxArray);
    dest.writeString(shelf);
    dest.writeString(offerDiscount);
    dest.writeString(isCentral);
    dest.writeString(totalAppliedTaxOnProduct);
    dest.writeString(discountedPrice);
    dest.writeString(directions);
    dest.writeString(name);
    dest.writeString(offerId);
    dest.writeString(offerValue);
    dest.writeTypedList(attributes);
    dest.writeString(_id);
    dest.writeString(currencyCode);
    dest.writeString(addToCartOnId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartProductItemDetails> CREATOR =
      new Creator<CartProductItemDetails>() {
        @Override
        public CartProductItemDetails createFromParcel(Parcel in) {
          return new CartProductItemDetails(in);
        }

        @Override
        public CartProductItemDetails[] newArray(int size) {
          return new CartProductItemDetails[size];
        }
      };

  public String getUpcNumber() {
    return upcNumber;
  }

  public void setUpcNumber(String upcNumber) {
    this.upcNumber = upcNumber;
  }

  public CartOfferDetails getOfferDetails() {
    return offerDetails;
  }

  public void setOfferDetails(
      CartOfferDetails offerDetails) {
    this.offerDetails = offerDetails;
  }

  public CartAccountingDetails getAccounting() {
    return accounting;
  }

  public void setAccounting(
      CartAccountingDetails accounting) {
    this.accounting = accounting;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(String originalPrice) {
    this.originalPrice = originalPrice;
  }

  public String getOfferValue() {
    return offerValue;
  }

  public void setOfferValue(String offerValue) {
    this.offerValue = offerValue;
  }

 /* public String getNoofunits() {
    return noofunits;
  }

  public void setNoofunits(String noofunits) {
    this.noofunits = noofunits;
  }*/

  public String getAddToCartOnId() {
    return addToCartOnId;
  }

  public void setAddToCartOnId(String addToCartOnId) {
    this.addToCartOnId = addToCartOnId;
  }

  public String getDiscount() {
    return discount;
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(String finalPrice) {
    this.finalPrice = finalPrice;
  }

  public Integer getAvailableQuantity() {
    return availableQuantity;
  }

  public void setAvailableQuantity(Integer availableQuantity) {
    this.availableQuantity = availableQuantity;
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

  public String getOfferType() {
    return offerType;
  }

  public void setOfferType(String offerType) {
    this.offerType = offerType;
  }

  public CartDeliveryDetails getDeliveryDetails() {
    return deliveryDetails;
  }

  public void setDeliveryDetails(
      CartDeliveryDetails deliveryDetails) {
    this.deliveryDetails = deliveryDetails;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public String getInStock() {
    return inStock;
  }

  public void setInStock(String inStock) {
    this.inStock = inStock;
  }

/*  public String getMouData() {
    return mouData;
  }

  public void setMouData(String mouData) {
    this.mouData = mouData;
  }*/

  public String getOfferTitle() {
    return offerTitle;
  }

  public void setOfferTitle(String offerTitle) {
    this.offerTitle = offerTitle;
  }

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

  public String getOriginalPriceTotal() {
    return originalPriceTotal;
  }

  public void setOriginalPriceTotal(String originalPriceTotal) {
    this.originalPriceTotal = originalPriceTotal;
  }

/*  public ArrayList<String> getAddOns() {
    return addOns;
  }

  public void setAddOns(ArrayList<String> addOns) {
    this.addOns = addOns;
  }*/

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

 /* public String getPackaging() {
    return packaging;
  }

  public void setPackaging(String packaging) {
    this.packaging = packaging;
  }*/

  public ArrayList<CartTaxDetails> getTax() {
    return tax;
  }

  public void setTax(
      ArrayList<CartTaxDetails> tax) {
    this.tax = tax;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public ArrayList<CartTaxDetails> getProductTaxArray() {
    return productTaxArray;
  }

  public void setProductTaxArray(
      ArrayList<CartTaxDetails> productTaxArray) {
    this.productTaxArray = productTaxArray;
  }

  public String getShelf() {
    return shelf;
  }

  public void setShelf(String shelf) {
    this.shelf = shelf;
  }

  public String getOfferDiscount() {
    return offerDiscount;
  }

  public void setOfferDiscount(String offerDiscount) {
    this.offerDiscount = offerDiscount;
  }

  public String getIsCentral() {
    return isCentral;
  }

  public void setIsCentral(String isCentral) {
    this.isCentral = isCentral;
  }

  public String getTotalAppliedTaxOnProduct() {
    return totalAppliedTaxOnProduct;
  }

  public void setTotalAppliedTaxOnProduct(String totalAppliedTaxOnProduct) {
    this.totalAppliedTaxOnProduct = totalAppliedTaxOnProduct;
  }

  public String getDiscountedPrice() {
    return discountedPrice;
  }

  public void setDiscountedPrice(String discountedPrice) {
    this.discountedPrice = discountedPrice;
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

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public ArrayList<CartAttributesDetails> getAttributes() {
    return attributes;
  }

  public void setAttributes(
      ArrayList<CartAttributesDetails> attributes) {
    this.attributes = attributes;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getManufactureName() {
    return manufactureName;
  }

  public void setManufactureName(String manufactureName) {
    this.manufactureName = manufactureName;
  }

  public boolean isPrescriptionRequired() {
    return prescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    this.prescriptionRequired = prescriptionRequired;
  }

  public String getMouDataUnit() {
    return mouDataUnit;
  }

  public void setMouDataUnit(String mouDataUnit) {
    this.mouDataUnit = mouDataUnit;
  }

  public String getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(String productQuantity) {
    this.productQuantity = productQuantity;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
