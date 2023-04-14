package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ColorsDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.Supplier;
import java.util.ArrayList;

public class ProductsDetails implements ValidItem, Parcelable {

  @SerializedName("offers")
  @Expose
  private OffersDataDetails offers;

  @SerializedName("availableQuantity")
  @Expose
  private String availableQuantity;

  @SerializedName("brandName")
  @Expose
  private String brandName;

  @SerializedName("images")
  @Expose
  private ArrayList<ImagesDetails> images;

  @SerializedName("parentProductId")
  @Expose
  private String parentProductId;

  @SerializedName("suppliers")
  @Expose
  private Supplier suppliers;

  @SerializedName("stores")
  @Expose
  private ArrayList<String> stores;

  @SerializedName("childProductId")
  @Expose
  private String childProductId;

  @SerializedName("storeCount")
  @Expose
  private String storeCount;

  @SerializedName("discountPrice")
  @Expose
  private String discountPrice;

  @SerializedName("currencySymbol")
  @Expose
  private String currencySymbol;

  @SerializedName("TotalStarRating")
  @Expose
  private int TotalStarRating;

  @SerializedName("colors")
  @Expose
  private ArrayList<ColorsDetails> colors;

  @SerializedName("productName")
  @Expose
  private String productName;

  @SerializedName("mou")
  @Expose
  private String mou;

  @SerializedName("sizes")
  @Expose
  private ArrayList<BasicSizeDetails> sizes;

  @SerializedName("minimumPurchaseUnit")
  @Expose
  private String minimumPurchaseUnit;

  @SerializedName("outOfStock")
  @Expose
  private boolean outOfStock;

  @SerializedName("unitId")
  @Expose
  private String unitId;

  @SerializedName("discountType")
  @Expose
  private String discountType;

  @SerializedName("currency")
  @Expose
  private String currency;

  @SerializedName("mouUnit")
  @Expose
  private String mouUnit;

  @SerializedName("finalPriceList")
  @Expose
  private FinalPriceListDetails finalPriceList;

  @SerializedName("avgRating")
  @Expose
  private int avgRating;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("finalPrice")
  @Expose
  private long finalPrice;

  @SerializedName("variantCount")
  @Expose
  private boolean variantCount;

  @SerializedName("prescriptionRequired")
  @Expose
  private boolean prescriptionRequired;

  @SerializedName("manufactureName")
  @Expose
  private String manufactureName;

  public ProductsDetails(String childProductId) {
    this.childProductId = childProductId;
  }

  public ProductsDetails(OffersDataDetails offers, String availableQuantity, String brandName,
                         ArrayList<ImagesDetails> images, String parentProductId,
                         Supplier suppliers, ArrayList<String> stores, String childProductId,
                         String storeCount, String discountPrice, String currencySymbol,
                         int totalStarRating, ArrayList<ColorsDetails> colors,
                         String productName, String mou, ArrayList<BasicSizeDetails> sizes,
                         String minimumPurchaseUnit, boolean outOfStock, String unitId,
                         String discountType, String currency, String mouUnit,
                         FinalPriceListDetails finalPriceList) {
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
    TotalStarRating = totalStarRating;
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
  }

  protected ProductsDetails(Parcel in) {
    availableQuantity = in.readString();
    brandName = in.readString();
    parentProductId = in.readString();
    stores = in.createStringArrayList();
    childProductId = in.readString();
    storeCount = in.readString();
    discountPrice = in.readString();
    currencySymbol = in.readString();
    TotalStarRating = in.readInt();
    productName = in.readString();
    mou = in.readString();
    minimumPurchaseUnit = in.readString();
    outOfStock = in.readByte() != 0;
    unitId = in.readString();
    discountType = in.readString();
    currency = in.readString();
    mouUnit = in.readString();
    avgRating = in.readInt();
    timestamp = in.readLong();
    finalPrice = in.readLong();
  }

  public static final Creator<ProductsDetails> CREATOR = new Creator<ProductsDetails>() {
    @Override
    public ProductsDetails createFromParcel(Parcel in) {
      return new ProductsDetails(in);
    }

    @Override
    public ProductsDetails[] newArray(int size) {
      return new ProductsDetails[size];
    }
  };

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(long finalPrice) {
    this.finalPrice = finalPrice;
  }

  public int getAvgRating() {
    return avgRating;
  }

  public void setAvgRating(int avgRating) {
    this.avgRating = avgRating;
  }

  public OffersDataDetails getOffers() {
    return offers;
  }

  public void setOffers(OffersDataDetails offers) {
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

  public ArrayList<ImagesDetails> getImages() {
    return images;
  }

  public void setImages(ArrayList<ImagesDetails> images) {
    this.images = images;
  }

  public String getParentProductId() {
    return parentProductId;
  }

  public void setParentProductId(String parentProductId) {
    this.parentProductId = parentProductId;
  }

  public Supplier getSuppliers() {
    return suppliers;
  }

  public void setSuppliers(Supplier suppliers) {
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
    return TotalStarRating;
  }

  public void setTotalStarRating(int totalStarRating) {
    TotalStarRating = totalStarRating;
  }

  public ArrayList<ColorsDetails> getColors() {
    return colors;
  }

  public void setColors(ArrayList<ColorsDetails> colors) {
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

  public ArrayList<BasicSizeDetails> getSizes() {
    return sizes;
  }

  public void setSizes(ArrayList<BasicSizeDetails> sizes) {
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

  public FinalPriceListDetails getFinalPriceList() {
    return finalPriceList;
  }

  public void setFinalPriceList(FinalPriceListDetails finalPriceList) {
    this.finalPriceList = finalPriceList;
  }

  public boolean isPrescriptionRequired() {
    return prescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    this.prescriptionRequired = prescriptionRequired;
  }

  public boolean isVariantCount() {
    return variantCount;
  }

  public void setVariantCount(boolean variantCount) {
    this.variantCount = variantCount;
  }

  public String getManufactureName() {
    return manufactureName;
  }

  public void setManufactureName(String manufactureName) {
    this.manufactureName = manufactureName;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(availableQuantity);
    dest.writeString(brandName);
    dest.writeString(parentProductId);
    dest.writeStringList(stores);
    dest.writeString(childProductId);
    dest.writeString(storeCount);
    dest.writeString(discountPrice);
    dest.writeString(currencySymbol);
    dest.writeInt(TotalStarRating);
    dest.writeString(productName);
    dest.writeString(mou);
    dest.writeString(minimumPurchaseUnit);
    dest.writeByte((byte) (outOfStock ? 1 : 0));
    dest.writeString(unitId);
    dest.writeString(discountType);
    dest.writeString(currency);
    dest.writeString(mouUnit);
    dest.writeInt(avgRating);
    dest.writeLong(timestamp);
    dest.writeLong(finalPrice);
  }

  @Override
  public boolean equals(Object o) {
    if (o != null) {
      ProductsDetails that = (ProductsDetails) o;
      return this.getChildProductId().equals(that.getChildProductId());
    } else {
      return false;
    }
  }
}
