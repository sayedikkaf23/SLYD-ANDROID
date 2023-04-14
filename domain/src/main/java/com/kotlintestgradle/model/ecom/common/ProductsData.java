package com.kotlintestgradle.model.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.pdp.ColorsData;
import com.kotlintestgradle.model.ecom.pdp.SizeData;
import com.kotlintestgradle.model.ecom.pdp.SupplierData;
import java.util.ArrayList;

public class ProductsData implements Parcelable {

  public static final Creator<ProductsData> CREATOR = new Creator<ProductsData>() {
    @Override
    public ProductsData createFromParcel(Parcel in) {
      return new ProductsData(in);
    }

    @Override
    public ProductsData[] newArray(int size) {
      return new ProductsData[size];
    }
  };
  private OffersListData offers;
  private String availableQuantity;
  private String brandName;
  private ArrayList<ImageData> images;
  private String parentProductId;
  private SupplierData suppliers;
  private ArrayList<String> stores;
  private String childProductId;
  private String storeCount;
  private String discountPrice;
  private String currencySymbol;
  private int mTotalStarRating;
  private ArrayList<ColorsData> colors;
  private String productName;
  private String mou;
  private ArrayList<SizeData> sizes;
  private String minimumPurchaseUnit;
  private boolean outOfStock;
  private String unitId;
  private String discountType;
  private String currency;
  private String mouUnit;
  private int cartQuantity;
  private FinalPriceListData finalPriceList;
  private int avgRating;
  private boolean isRemoved;
  private boolean prescriptionRequired;
  private String manufactureName;
  private boolean variantCount;

  public ProductsData(String availableQuantity, String brandName, String childProductId,
      String productName, FinalPriceListData finalPriceList, ArrayList<ImageData> images,
      String currencySymbol, String parentProductId,
      boolean isRemoved) {
    this.availableQuantity = availableQuantity;
    this.brandName = brandName;
    this.childProductId = childProductId;
    this.productName = productName;
    this.finalPriceList = finalPriceList;
    this.isRemoved = isRemoved;
    this.images = images;
    this.currencySymbol = currencySymbol;
    this.parentProductId = parentProductId;
  }

  public ProductsData(String childProductId) {
    this.childProductId = childProductId;
  }

  public ProductsData(String childProductId, boolean isRemoved) {
    this.childProductId = childProductId;
    this.isRemoved = isRemoved;
  }

  public ProductsData(OffersListData offers, String availableQuantity, String brandName,
      ArrayList<ImageData> images, String parentProductId,
      SupplierData suppliers, ArrayList<String> stores, String childProductId,
      String storeCount, String discountPrice, String currencySymbol,
      int totalStarRating, ArrayList<ColorsData> colors,
      String productName, String mou, ArrayList<SizeData> sizes,
      String minimumPurchaseUnit, boolean outOfStock, String unitId,
      String discountType, String currency, String mouUnit,
      FinalPriceListData finalPriceList, int avgRating) {
    this.offers = offers;
    this.availableQuantity = availableQuantity;
    this.brandName = brandName;
    this.images = images;
    this.parentProductId = parentProductId;
    this.suppliers = suppliers;
    this.stores = stores;
    this.childProductId = childProductId;
    this.storeCount = storeCount;
    this.discountPrice = discountPrice;
    this.currencySymbol = currencySymbol;
    mTotalStarRating = totalStarRating;
    this.colors = colors;
    this.productName = productName;
    this.mou = mou;
    this.sizes = sizes;
    this.minimumPurchaseUnit = minimumPurchaseUnit;
    this.outOfStock = outOfStock;
    this.unitId = unitId;
    this.discountType = discountType;
    this.currency = currency;
    this.mouUnit = mouUnit;
    this.finalPriceList = finalPriceList;
    this.avgRating = avgRating;
  }

  public ProductsData(OffersListData offers, String availableQuantity, String brandName,
                      ArrayList<ImageData> images, String parentProductId,
                      SupplierData suppliers, ArrayList<String> stores, String childProductId,
                      String storeCount, String discountPrice, String currencySymbol,
                      int totalStarRating, ArrayList<ColorsData> colors,
                      String productName, String mou, ArrayList<SizeData> sizes,
                      String minimumPurchaseUnit, boolean outOfStock, String unitId,
                      String discountType, String currency, String mouUnit,
                      FinalPriceListData finalPriceList, int avgRating, boolean prescriptionRequired,
                      boolean variantCount, String manufactureName) {
    this.offers = offers;
    this.availableQuantity = availableQuantity;
    this.brandName = brandName;
    this.images = images;
    this.parentProductId = parentProductId;
    this.suppliers = suppliers;
    this.stores = stores;
    this.childProductId = childProductId;
    this.storeCount = storeCount;
    this.discountPrice = discountPrice;
    this.currencySymbol = currencySymbol;
    mTotalStarRating = totalStarRating;
    this.colors = colors;
    this.productName = productName;
    this.mou = mou;
    this.sizes = sizes;
    this.minimumPurchaseUnit = minimumPurchaseUnit;
    this.outOfStock = outOfStock;
    this.unitId = unitId;
    this.discountType = discountType;
    this.currency = currency;
    this.mouUnit = mouUnit;
    this.finalPriceList = finalPriceList;
    this.avgRating = avgRating;
    this.prescriptionRequired = prescriptionRequired;
    this.variantCount = variantCount;
    this.manufactureName = manufactureName;
  }

  protected ProductsData(Parcel in) {
    availableQuantity = in.readString();
    brandName = in.readString();
    parentProductId = in.readString();
    stores = in.createStringArrayList();
    childProductId = in.readString();
    storeCount = in.readString();
    discountPrice = in.readString();
    currencySymbol = in.readString();
    mTotalStarRating = in.readInt();
    productName = in.readString();
    mou = in.readString();
    minimumPurchaseUnit = in.readString();
    unitId = in.readString();
    discountType = in.readString();
    currency = in.readString();
    mouUnit = in.readString();
    colors = new ArrayList<ColorsData>();
    in.readTypedList(colors, ColorsData.CREATOR);
    images = new ArrayList<ImageData>();
    in.readTypedList(images, ImageData.CREATOR);
    sizes = new ArrayList<SizeData>();
    in.readTypedList(sizes, SizeData.CREATOR);
    finalPriceList = (FinalPriceListData) in.readValue(FinalPriceListData.class.getClassLoader());
  }

  public boolean isRemoved() {
    return isRemoved;
  }

  public void setRemoved(boolean removed) {
    isRemoved = removed;
  }

  public int getAvgRating() {
    return avgRating;
  }

  public void setAvgRating(int avgRating) {
    this.avgRating = avgRating;
  }

  public OffersListData getOffers() {
    return offers;
  }

  public void setOffers(OffersListData offers) {
    this.offers = offers;
  }

  public String getAvailableQuantity() {
    return availableQuantity;
  }

  public void setAvailableQuantity(String availableQuantity) {
    this.availableQuantity = availableQuantity;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public ArrayList<ImageData> getImages() {
    return images;
  }

  public void setImages(ArrayList<ImageData> images) {
    this.images = images;
  }

  public String getParentProductId() {
    return parentProductId;
  }

  public void setParentProductId(String parentProductId) {
    this.parentProductId = parentProductId;
  }

  public SupplierData getSuppliers() {
    return suppliers;
  }

  public void setSuppliers(SupplierData suppliers) {
    this.suppliers = suppliers;
  }

  public ArrayList<String> getStores() {
    return stores;
  }

  public void setStores(ArrayList<String> stores) {
    this.stores = stores;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public String getStoreCount() {
    return storeCount;
  }

  public void setStoreCount(String storeCount) {
    this.storeCount = storeCount;
  }

  public String getDiscountPrice() {
    return discountPrice;
  }

  public void setDiscountPrice(String discountPrice) {
    this.discountPrice = discountPrice;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public int getTotalStarRating() {
    return mTotalStarRating;
  }

  public void setTotalStarRating(int totalStarRating) {
    mTotalStarRating = totalStarRating;
  }

  public ArrayList<ColorsData> getColors() {
    return colors;
  }

  public void setColors(ArrayList<ColorsData> colors) {
    this.colors = colors;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getMou() {
    return mou;
  }

  public void setMou(String mou) {
    this.mou = mou;
  }

  public ArrayList<SizeData> getSizes() {
    return sizes;
  }

  public void setSizes(ArrayList<SizeData> sizes) {
    this.sizes = sizes;
  }

  public String getMinimumPurchaseUnit() {
    return minimumPurchaseUnit;
  }

  public void setMinimumPurchaseUnit(String minimumPurchaseUnit) {
    this.minimumPurchaseUnit = minimumPurchaseUnit;
  }

  public boolean getOutOfStock() {
    return outOfStock;
  }

  public void setOutOfStock(boolean outOfStock) {
    this.outOfStock = outOfStock;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public String getDiscountType() {
    return discountType;
  }

  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getMouUnit() {
    return mouUnit;
  }

  public void setMouUnit(String mouUnit) {
    this.mouUnit = mouUnit;
  }

  public int getCartQuantity() {
    return cartQuantity;
  }

  public void setCartQuantity(int cartQuantity) {
    this.cartQuantity = cartQuantity;
  }

  public FinalPriceListData getFinalPriceList() {
    return finalPriceList;
  }

  public void setFinalPriceList(FinalPriceListData finalPriceList) {
    this.finalPriceList = finalPriceList;
  }

  public boolean isPrescriptionRequired() {
    return prescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    this.prescriptionRequired = prescriptionRequired;
  }

  public String getManufactureName() {
    return manufactureName;
  }

  public void setManufactureName(String manufactureName) {
    this.manufactureName = manufactureName;
  }

  public boolean isVariantCount() {
    return variantCount;
  }

  public void setVariantCount(boolean variantCount) {
    this.variantCount = variantCount;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(availableQuantity);
    parcel.writeString(brandName);
    parcel.writeString(parentProductId);
    parcel.writeStringList(stores);
    parcel.writeString(childProductId);
    parcel.writeString(storeCount);
    parcel.writeString(discountPrice);
    parcel.writeString(currencySymbol);
    parcel.writeInt(mTotalStarRating);
    parcel.writeString(productName);
    parcel.writeString(mou);
    parcel.writeString(minimumPurchaseUnit);
    parcel.writeString(unitId);
    parcel.writeString(discountType);
    parcel.writeString(currency);
    parcel.writeString(mouUnit);
    parcel.writeTypedList(colors);
    parcel.writeTypedList(sizes);
    parcel.writeTypedList(images);
    parcel.writeValue(finalPriceList);

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProductsData)) {
      return false;
    }
    ProductsData itemData = (ProductsData) o;
    if (getChildProductId() == null) {
      return false;
    }
    return getChildProductId().equalsIgnoreCase(itemData.getChildProductId());
  }
}
