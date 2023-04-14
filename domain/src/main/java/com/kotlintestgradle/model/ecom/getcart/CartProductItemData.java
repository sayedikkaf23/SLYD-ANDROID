package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.customer.domain.model.getcart.SelectedDataRecord;
import com.kotlintestgradle.model.ecom.common.ImageData;
import java.util.ArrayList;

public class CartProductItemData implements Parcelable {
  private String upcNumber;
  private String color;
  private String originalPrice;
/*
  private String noofunits;
*/
  private String discount;
  private String finalPrice;
  private String aisle;
  private String packageType;
  private String centralProductId;
  private String addToCartOnId;
  private String offerType;
  private CartDeliveryData deliveryDetails;
  private String unitId;
  private String storeName;
  private String inStock;
  private String mouData;
  private String offerTitle;
  private String manufactureName;
  private String mouProductDetail;
  private String productQuantity;
  private ImageData images;
  private String brandName;
  private QuantityData quantity;
  private String originalPriceTotal;
  private ArrayList<String> addOns;
  private String currencySymbol;
  private boolean prescriptionRequired;
  /*
    private String packaging;
  */
  private ArrayList<CartTaxData> tax;
  private String storeId;
  private ArrayList<CartTaxData> productTaxArray;
  private String shelf;
  private String offerDiscount;
  private String isCentral;
  private String totalAppliedTaxOnProduct;
  private String discountedPrice;
  private String directions;
  private String name;
  private String offerId;
  private ArrayList<CartAttributesData> attributes;
  private String mId;
  private CartAccoutingData accounting;
  private CartOfferData offerDetails;
  private String currencyCode;
  private String offerValue;
  private int availableQuantity;
  private int cartQuantity;
  private int addOnAvailable;
  private boolean isEnable = true;
   private int type;
  private boolean containsMeat = false;
  private ArrayList<SelectedDataRecord> selectedAddOns;

  public CartProductItemData(int type) {
    this.type = type;
  }

  public CartProductItemData(String upcNumber, String color, String originalPrice,
       String discount, String finalPrice, String aisle,
      String packageType, String centralProductId, String offerType, String offerValue,
      CartDeliveryData deliveryDetails, String unitId, String storeName, String inStock,
      String offerTitle, ImageData images, String brandName,
      QuantityData quantity, String originalPriceTotal, /*ArrayList<String> addOns,*/
      String currencySymbol,
      ArrayList<CartTaxData> tax, String storeId,
      ArrayList<CartTaxData> productTaxArray, String shelf, String offerDiscount,
      String isCentral, String totalAppliedTaxOnProduct, String discountedPrice,
      String directions, String name, String offerId,
      ArrayList<CartAttributesData> attributes, String id, String currencyCode,
      CartAccoutingData accounting, CartOfferData offerDetails/*, int availableQuantity*/,String addToCartOnId,
                             int addOnAvailable,boolean containsMeat,ArrayList<SelectedDataRecord> selectedAddOns,
                             int availableQuantity) {
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
    this.deliveryDetails = deliveryDetails;
    this.unitId = unitId;
    this.addToCartOnId = addToCartOnId;
    this.storeName = storeName;
    this.inStock = inStock;
    this.mouData = mouData;
    this.offerValue = offerValue;
    this.offerTitle = offerTitle;
    this.images = images;
    this.brandName = brandName;
    this.quantity = quantity;
    this.originalPriceTotal = originalPriceTotal;
    this.addOns = addOns;
    this.currencySymbol = currencySymbol;
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
    this.mId = id;
    this.currencyCode = currencyCode;
    this.accounting = accounting;
    this.offerDetails = offerDetails;
    this.availableQuantity = availableQuantity;
    this.addOnAvailable = addOnAvailable;
    this.containsMeat = containsMeat;
    this.selectedAddOns= selectedAddOns;
  }

  protected CartProductItemData(Parcel in) {
    upcNumber = in.readString();
    color = in.readString();
    originalPrice = in.readString();
    discount = in.readString();
    finalPrice = in.readString();
    aisle = in.readString();
    packageType = in.readString();
    centralProductId = in.readString();
    addToCartOnId = in.readString();
    offerType = in.readString();
    deliveryDetails = in.readParcelable(CartDeliveryData.class.getClassLoader());
    unitId = in.readString();
    storeName = in.readString();
    inStock = in.readString();
    mouData = in.readString();
    offerTitle = in.readString();
    manufactureName = in.readString();
    mouProductDetail = in.readString();
    productQuantity = in.readString();
    images = in.readParcelable(ImageData.class.getClassLoader());
    brandName = in.readString();
    quantity = in.readParcelable(QuantityData.class.getClassLoader());
    originalPriceTotal = in.readString();
    addOns = in.createStringArrayList();
    currencySymbol = in.readString();
    prescriptionRequired = in.readByte() != 0;
    tax = in.createTypedArrayList(CartTaxData.CREATOR);
    storeId = in.readString();
    productTaxArray = in.createTypedArrayList(CartTaxData.CREATOR);
    shelf = in.readString();
    offerDiscount = in.readString();
    isCentral = in.readString();
    totalAppliedTaxOnProduct = in.readString();
    discountedPrice = in.readString();
    directions = in.readString();
    name = in.readString();
    offerId = in.readString();
    attributes = in.createTypedArrayList(CartAttributesData.CREATOR);
    mId = in.readString();
    accounting = in.readParcelable(CartAccoutingData.class.getClassLoader());
    offerDetails = in.readParcelable(CartOfferData.class.getClassLoader());
    currencyCode = in.readString();
    offerValue = in.readString();
    availableQuantity = in.readInt();
    cartQuantity = in.readInt();
    addOnAvailable = in.readInt();
    isEnable = in.readByte() != 0;
    type = in.readInt();
    containsMeat = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(upcNumber);
    dest.writeString(color);
    dest.writeString(originalPrice);
    dest.writeString(discount);
    dest.writeString(finalPrice);
    dest.writeString(aisle);
    dest.writeString(packageType);
    dest.writeString(centralProductId);
    dest.writeString(addToCartOnId);
    dest.writeString(offerType);
    dest.writeParcelable(deliveryDetails, flags);
    dest.writeString(unitId);
    dest.writeString(storeName);
    dest.writeString(inStock);
    dest.writeString(mouData);
    dest.writeString(offerTitle);
    dest.writeString(manufactureName);
    dest.writeString(mouProductDetail);
    dest.writeString(productQuantity);
    dest.writeParcelable(images, flags);
    dest.writeString(brandName);
    dest.writeParcelable(quantity, flags);
    dest.writeString(originalPriceTotal);
    dest.writeStringList(addOns);
    dest.writeString(currencySymbol);
    dest.writeByte((byte) (prescriptionRequired ? 1 : 0));
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
    dest.writeTypedList(attributes);
    dest.writeString(mId);
    dest.writeParcelable(accounting, flags);
    dest.writeParcelable(offerDetails, flags);
    dest.writeString(currencyCode);
    dest.writeString(offerValue);
    dest.writeInt(availableQuantity);
    dest.writeInt(cartQuantity);
    dest.writeInt(addOnAvailable);
    dest.writeByte((byte) (isEnable ? 1 : 0));
    dest.writeInt(type);
    dest.writeByte((byte) (containsMeat ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartProductItemData> CREATOR = new Creator<CartProductItemData>() {
    @Override
    public CartProductItemData createFromParcel(Parcel in) {
      return new CartProductItemData(in);
    }

    @Override
    public CartProductItemData[] newArray(int size) {
      return new CartProductItemData[size];
    }
  };

  public String getAddToCartOnId() {
    return addToCartOnId;
  }

  public void setAddToCartOnId(String addToCartOnId) {
    this.addToCartOnId = addToCartOnId;
  }

  public ArrayList<SelectedDataRecord> getSelectedAddOns() {
    return selectedAddOns;
  }

  public void setSelectedAddOns(ArrayList<SelectedDataRecord> selectedAddOns) {
    this.selectedAddOns = selectedAddOns;
  }

  public boolean isContainsMeat() {
    return containsMeat;
  }

  public void setContainsMeat(boolean containsMeat) {
    this.containsMeat = containsMeat;
  }

  public int getAddOnAvailable() {
    return addOnAvailable;
  }

  public void setAddOnAvailable(int addOnAvailable) {
    this.addOnAvailable = addOnAvailable;
  }

  public CartProductItemData(String upcNumber, String color, String originalPrice,
                             String discount, String finalPrice, String aisle,
                             String packageType, String centralProductId, String offerType, String offerValue,
                             CartDeliveryData deliveryDetails, String unitId, String storeName, String inStock,
                             String offerTitle, ImageData images, String brandName,
                             QuantityData quantity, String originalPriceTotal, /*ArrayList<String> addOns,*/
                             String currencySymbol,
                             ArrayList<CartTaxData> tax, String storeId,
                             ArrayList<CartTaxData> productTaxArray, String shelf, String offerDiscount,
                             String isCentral, String totalAppliedTaxOnProduct, String discountedPrice,
                             String directions, String name, String offerId,
                             ArrayList<CartAttributesData> attributes, String id, String currencyCode,
                             CartAccoutingData accounting, CartOfferData offerDetails, /*int availableQuantity,*/
                             boolean prescriptionRequired, String manufactureName, String productQuantity, String mouDataUnit) {
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
    this.deliveryDetails = deliveryDetails;
    this.unitId = unitId;
    this.storeName = storeName;
    this.inStock = inStock;
    this.mouData = mouData;
    this.offerValue = offerValue;
    this.offerTitle = offerTitle;
    this.images = images;
    this.brandName = brandName;
    this.quantity = quantity;
    this.originalPriceTotal = originalPriceTotal;
    this.addOns = addOns;
    this.currencySymbol = currencySymbol;
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
    this.mId = id;
    this.currencyCode = currencyCode;
    this.accounting = accounting;
    this.offerDetails = offerDetails;
    this.availableQuantity = availableQuantity;
    this.prescriptionRequired = prescriptionRequired;
    this.manufactureName = manufactureName;
    this.mouProductDetail = mouDataUnit;
    this.productQuantity = productQuantity;
  }

  public CartProductItemData(String upcNumber, String color, String originalPrice,
      String discount, String finalPrice, String aisle,
      String packageType, String centralProductId, String offerType, String offerValue,
      CartDeliveryData deliveryDetails, String unitId, String storeName, String inStock,
      String offerTitle, ImageData images, String brandName,
      QuantityData quantity, String originalPriceTotal, /*ArrayList<String> addOns,*/
      String currencySymbol,
      ArrayList<CartTaxData> tax, String storeId,
      ArrayList<CartTaxData> productTaxArray, String shelf, String offerDiscount,
      String isCentral, String totalAppliedTaxOnProduct, String discountedPrice,
      String directions, String name, String offerId,
      ArrayList<CartAttributesData> attributes, String id, String currencyCode,
      CartAccoutingData accounting, CartOfferData offerDetails/*, int availableQuantity*/,
      int addOnAvailable,boolean containsMeat,ArrayList<SelectedDataRecord> selectedAddOns) {
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
    this.deliveryDetails = deliveryDetails;
    this.unitId = unitId;
    this.storeName = storeName;
    this.inStock = inStock;
    this.mouData = mouData;
    this.offerValue = offerValue;
    this.offerTitle = offerTitle;
    this.images = images;
    this.brandName = brandName;
    this.quantity = quantity;
    this.originalPriceTotal = originalPriceTotal;
    this.addOns = addOns;
    this.currencySymbol = currencySymbol;
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
    this.mId = id;
    this.currencyCode = currencyCode;
    this.accounting = accounting;
    this.offerDetails = offerDetails;
    this.availableQuantity = availableQuantity;
    this.addOnAvailable = addOnAvailable;
    this.containsMeat = containsMeat;
    this.selectedAddOns= selectedAddOns;
  }

  public CartProductItemData(String upcNumber, String color, String originalPrice,
                             String discount, String finalPrice, String aisle,
                             String packageType, String centralProductId, String offerType, String offerValue,
                             CartDeliveryData deliveryDetails, String unitId, String storeName, String inStock,
                             String offerTitle, ImageData images, String brandName,
                             QuantityData quantity, String originalPriceTotal, ArrayList<String> addOns,
                             String currencySymbol,
                             ArrayList<CartTaxData> tax, String storeId,
                             ArrayList<CartTaxData> productTaxArray, String shelf, String offerDiscount,
                             String isCentral, String totalAppliedTaxOnProduct, String discountedPrice,
                             String directions, String name, String offerId,
                             ArrayList<CartAttributesData> attributes, String id, String currencyCode,
                             CartAccoutingData accounting, CartOfferData offerDetails, int availableQuantity,
                             boolean prescriptionRequired, String manufactureName, String productQuantity, String mouDataUnit) {
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
    this.deliveryDetails = deliveryDetails;
    this.unitId = unitId;
    this.storeName = storeName;
    this.inStock = inStock;
    this.mouData = mouData;
    this.offerValue = offerValue;
    this.offerTitle = offerTitle;
    this.images = images;
    this.brandName = brandName;
    this.quantity = quantity;
    this.originalPriceTotal = originalPriceTotal;
    this.addOns = addOns;
    this.currencySymbol = currencySymbol;
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
    this.mId = id;
    this.currencyCode = currencyCode;
    this.accounting = accounting;
    this.offerDetails = offerDetails;
    this.availableQuantity = availableQuantity;
    this.prescriptionRequired = prescriptionRequired;
    this.manufactureName = manufactureName;
    this.mouProductDetail = mouDataUnit;
    this.productQuantity = productQuantity;
  }

  public CartProductItemData(String upcNumber, String color, String originalPrice,
                             String discount, String finalPrice, String aisle,
                             String packageType, String centralProductId, String offerType, String offerValue,
                             CartDeliveryData deliveryDetails, String unitId, String storeName, String inStock,
                             String offerTitle, ImageData images, String brandName,
                             QuantityData quantity, String originalPriceTotal, ArrayList<String> addOns,
                             String currencySymbol,
                             ArrayList<CartTaxData> tax, String storeId,
                             ArrayList<CartTaxData> productTaxArray, String shelf, String offerDiscount,
                             String isCentral, String totalAppliedTaxOnProduct, String discountedPrice,
                             String directions, String name, String offerId,
                             ArrayList<CartAttributesData> attributes, String id, String currencyCode,
                             CartAccoutingData accounting, CartOfferData offerDetails/*, int availableQuantity*/,
                             int addOnAvailable) {
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
    this.deliveryDetails = deliveryDetails;
    this.unitId = unitId;
    this.storeName = storeName;
    this.inStock = inStock;
    this.mouData = mouData;
    this.offerValue = offerValue;
    this.offerTitle = offerTitle;
    this.images = images;
    this.brandName = brandName;
    this.quantity = quantity;
    this.originalPriceTotal = originalPriceTotal;
    this.addOns = addOns;
    this.currencySymbol = currencySymbol;
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
    this.mId = id;
    this.currencyCode = currencyCode;
    this.accounting = accounting;
    this.offerDetails = offerDetails;
    this.availableQuantity = availableQuantity;
    this.addOnAvailable = addOnAvailable;
    this.containsMeat = containsMeat;
    this.selectedAddOns= selectedAddOns;
  }

  public CartAccoutingData getAccounting() {
    return accounting;
  }

  public void setAccounting(CartAccoutingData accounting) {
    this.accounting = accounting;
  }

  public CartOfferData getOfferDetails() {
    return offerDetails;
  }

  public void setOfferDetails(CartOfferData offerDetails) {
    this.offerDetails = offerDetails;
  }

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

  public String getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(String originalPrice) {
    this.originalPrice = originalPrice;
  }

  /*public String getNoofunits() {
    return noofunits;
  }

  public void setNoofunits(String noofunits) {
    this.noofunits = noofunits;
  }*/

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

  public String getOfferValue() {
    return offerValue;
  }

  public void setOfferValue(String offerValue) {
    this.offerValue = offerValue;
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

  public CartDeliveryData getDeliveryDetails() {
    return deliveryDetails;
  }

  public void setDeliveryDetails(CartDeliveryData deliveryDetails) {
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

  public int getAvailableQuantity() {
    return availableQuantity;
  }

  public void setAvailableQuantity(int availableQuantity) {
    this.availableQuantity = availableQuantity;
  }

  public String getInStock() {
    return inStock;
  }

  public void setInStock(String inStock) {
    this.inStock = inStock;
  }

  public String getMouData() {
    return mouData;
  }

  public void setMouData(String mouData) {
    this.mouData = mouData;
  }

  public String getOfferTitle() {
    return offerTitle;
  }

  public void setOfferTitle(String offerTitle) {
    this.offerTitle = offerTitle;
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

  public String getOriginalPriceTotal() {
    return originalPriceTotal;
  }

  public void setOriginalPriceTotal(String originalPriceTotal) {
    this.originalPriceTotal = originalPriceTotal;
  }

  public ArrayList<String> getAddOns() {
    return addOns;
  }

  public void setAddOns(ArrayList<String> addOns) {
    this.addOns = addOns;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public ArrayList<CartTaxData> getTax() {
    return tax;
  }

  public void setTax(ArrayList<CartTaxData> tax) {
    this.tax = tax;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public ArrayList<CartTaxData> getProductTaxArray() {
    return productTaxArray;
  }

  public void setProductTaxArray(
      ArrayList<CartTaxData> productTaxArray) {
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

  public ArrayList<CartAttributesData> getAttributes() {
    return attributes;
  }

  public void setAttributes(
      ArrayList<CartAttributesData> attributes) {
    this.attributes = attributes;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    this.mId = id;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public boolean isEnable() {
    return isEnable;
  }

  public void setEnable(boolean enable) {
    isEnable = enable;
  }

  public boolean isPrescriptionRequired() {
    return prescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    this.prescriptionRequired = prescriptionRequired;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getManufactureName() {
    return manufactureName;
  }

  public void setManufactureName(String manufactureName) {
    this.manufactureName = manufactureName;
  }

  public String getMouProductDetail() {
    return mouProductDetail;
  }

  public void setMouProductDetail(String mouProductDetail) {
    this.mouProductDetail = mouProductDetail;
  }

  public String getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(String productQuantity) {
    this.productQuantity = productQuantity;
  }

  public String getmId() {
    return mId;
  }

  public void setmId(String mId) {
    this.mId = mId;
  }

  public int getCartQuantity() {
    return cartQuantity;
  }

  public void setCartQuantity(int cartQuantity) {
    this.cartQuantity = cartQuantity;
  }
}
