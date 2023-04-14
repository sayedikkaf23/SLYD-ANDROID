package com.kotlintestgradle.remote.model.response.ecom.homeSubCategory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.FinalPriceListDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.Supplier;
import java.util.ArrayList;

public class SubCategoryProductDetails implements Parcelable, ValidItem {
  @SerializedName("offers")
  @Expose
  private ProductOfferDetails offers;

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

  @SerializedName("childProductId")
  @Expose
  private String childProductId;

  @SerializedName("currencySymbol")
  @Expose
  private String currencySymbol;

  @SerializedName("TotalStarRating")
  @Expose
  private int TotalStarRating;

  @SerializedName("productName")
  @Expose
  private String productName;

  @SerializedName("supplier")
  @Expose
  private Supplier supplier;

  @SerializedName("outOfStock")
  @Expose
  private boolean outOfStock;

  @SerializedName("unitId")
  @Expose
  private String unitId;

  @SerializedName("currency")
  @Expose
  private String currency;

  @SerializedName("discountType")
  @Expose
  private int discountType;

  @SerializedName("finalPriceList")
  @Expose
  private FinalPriceListDetails finalPriceList;

  @SerializedName("prescriptionRequired")
  @Expose
  private boolean prescriptionRequired;

  @SerializedName("manufactureName")
  @Expose
  private String manufactureName;

  private int cartQuantity;

  public SubCategoryProductDetails(ProductOfferDetails offers, String availableQuantity,
      String brandName, ArrayList<ImagesDetails> images,
      String parentProductId, String childProductId,
      String currencySymbol, int totalStarRating,
      String productName, Supplier supplier, boolean outOfStock,
      String unitId, String currency, int discountType,
      FinalPriceListDetails finalPriceList) {
    this.offers = offers;
    this.availableQuantity = availableQuantity;
    this.brandName = brandName;
    this.images = images;
    this.parentProductId = parentProductId;
    this.childProductId = childProductId;
    this.currencySymbol = currencySymbol;
    TotalStarRating = totalStarRating;
    this.productName = productName;
    this.supplier = supplier;
    this.outOfStock = outOfStock;
    this.unitId = unitId;
    this.currency = currency;
    this.discountType = discountType;
    this.finalPriceList = finalPriceList;
  }

  protected SubCategoryProductDetails(Parcel in) {
    offers = in.readParcelable(ProductOfferDetails.class.getClassLoader());
    availableQuantity = in.readString();
    brandName = in.readString();
    images = in.createTypedArrayList(ImagesDetails.CREATOR);
    parentProductId = in.readString();
    childProductId = in.readString();
    currencySymbol = in.readString();
    TotalStarRating = in.readInt();
    productName = in.readString();
    outOfStock = in.readByte() != 0;
    unitId = in.readString();
    currency = in.readString();
    discountType = in.readInt();
    finalPriceList = in.readParcelable(FinalPriceListDetails.class.getClassLoader());
  }

  public SubCategoryProductDetails(String childProductId) {
    this.childProductId = childProductId;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(offers, flags);
    dest.writeString(availableQuantity);
    dest.writeString(brandName);
    dest.writeTypedList(images);
    dest.writeString(parentProductId);
    dest.writeString(childProductId);
    dest.writeString(currencySymbol);
    dest.writeInt(TotalStarRating);
    dest.writeString(productName);
    dest.writeByte((byte) (outOfStock ? 1 : 0));
    dest.writeString(unitId);
    dest.writeString(currency);
    dest.writeInt(discountType);
    dest.writeParcelable(finalPriceList, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SubCategoryProductDetails> CREATOR =
      new Creator<SubCategoryProductDetails>() {
        @Override
        public SubCategoryProductDetails createFromParcel(Parcel in) {
          return new SubCategoryProductDetails(in);
        }

        @Override
        public SubCategoryProductDetails[] newArray(int size) {
          return new SubCategoryProductDetails[size];
        }
      };

  public ProductOfferDetails getOffers() {
    return offers;
  }

  public void setOffers(ProductOfferDetails offers) {
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

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
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

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public int getDiscountType() {
    return discountType;
  }

  public void setDiscountType(int discountType) {
    this.discountType = discountType;
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

  public int getCartQuantity() {
    return cartQuantity;
  }

  public void setCartQuantity(int cartQuantity) {
    this.cartQuantity = cartQuantity;
  }

  public String getManufactureName() {
    return manufactureName;
  }

  public void setManufactureName(String manufactureName) {
    this.manufactureName = manufactureName;
  }

  @Override
  public boolean equals(Object o) {
    if (o != null) {
      SubCategoryProductDetails that = (SubCategoryProductDetails) o;
      return this.getChildProductId().equals(that.getChildProductId());
    } else {
      return false;
    }
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
