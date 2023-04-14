package com.kotlintestgradle.model.ecom.home;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.common.FinalPriceListData;
import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.pdp.SupplierData;
import com.kotlintestgradle.model.ecom.productcategory.SubCategoryData;
import java.util.ArrayList;

public class CategoryData implements Parcelable {
  private String subCategoryName;
  private int penCount;
  private int productCount;
  private String id;
  private String imageUrl;
  private String logo;
  private String name;
  private String bannerImage;
  private String offerdescription;
  private String currencySymbol;
  private String currency;
  private String offerId;
  private String offername;
  private String offerName;
  private String catName;
  private String bannerImageUrl;
  private ArrayList<ImageData> images;
  private int discountType;
  private String parentProductId;
  private String availableQuantity;
  private String childProductId;
  private double discountPrice;
  private CategoryOfferData offers;
  private SupplierData supplier;
  private String unitId;
  private boolean outOfStock;
  private FinalPriceListData finalPriceList;
  private String productName;
  private String brandName;
  private String manufactureName;
  private String imageMobile;
  private String subCatName;
  private int type;
  private int cartQuantity;
  private boolean prescriptionRequired;
  private boolean isVariantsAvailable;
  private int mTotalStarRating;
  private String mobileImage;
  private String webImage;
  private ArrayList<SubCategoryData> subCategoryList;

  public CategoryData(String catName) {
    this.catName = catName;
  }

  public CategoryData(String parentProductId, String childProductId) {
    this.parentProductId = parentProductId;
    this.childProductId = childProductId;
  }

  public CategoryData(String currencySymbol, String currency, String catName,
      ArrayList<ImageData> images, String childProductId, String unitId, boolean outOfStock,
      FinalPriceListData finalPriceList, String productName, String brandName,
      String parentProductId) {
    this.currencySymbol = currencySymbol;
    this.currency = currency;
    this.catName = catName;
    this.images = images;
    this.parentProductId = parentProductId;
    this.childProductId = childProductId;
    this.unitId = unitId;
    this.outOfStock = outOfStock;
    this.finalPriceList = finalPriceList;
    this.productName = productName;
    this.brandName = brandName;
  }

  public CategoryData(int productCount, String id, String logo, String name, String mobileImage, String webImage) {
    this.productCount = productCount;
    this.id = id;
    this.logo = logo;
    this.name = name;
    this.mobileImage = mobileImage;
    this.webImage = webImage;
  }

  public CategoryData(String subCategoryName, int penCount, int productCount, String id,
                      String imageUrl, String logo, String name, String bannerImage,
                      String offerdescription, String currencySymbol,
                      String currency, String offerId, String offername, String catName,
                      String bannerImageUrl, ArrayList<ImageData> images, int discountType,
                      String parentProductId, String availableQuantity, String childProductId,
                      double discountPrice, CategoryOfferData offers, SupplierData supplier,
                      String unitId, boolean outOfStock,
                      FinalPriceListData finalPriceList, String productName,
                      String brandName, String imageMobile, String subCatName, int type, int totalStarRating,
                      String offerName) {
    this.subCategoryName = subCategoryName;
    this.penCount = penCount;
    this.productCount = productCount;
    this.id = id;
    this.imageUrl = imageUrl;
    this.logo = logo;
    this.name = name;
    this.bannerImage = bannerImage;
    this.offerdescription = offerdescription;
    this.currencySymbol = currencySymbol;
    this.currency = currency;
    this.offerId = offerId;
    this.offername = offername;
    this.catName = catName;
    this.bannerImageUrl = bannerImageUrl;
    this.images = images;
    this.discountType = discountType;
    this.parentProductId = parentProductId;
    this.availableQuantity = availableQuantity;
    this.childProductId = childProductId;
    this.discountPrice = discountPrice;
    this.offers = offers;
    this.supplier = supplier;
    this.unitId = unitId;
    this.outOfStock = outOfStock;
    this.finalPriceList = finalPriceList;
    this.productName = productName;
    this.brandName = brandName;
    this.imageMobile = imageMobile;
    this.type = type;
    this.subCatName = subCatName;
    this.mTotalStarRating = totalStarRating;
    this.offerName = offerName;
  }



  public CategoryData(String subCategoryName, int penCount, int productCount, String id,
                      String imageUrl, String logo, String name, String bannerImage,
                      String offerdescription, String currencySymbol,
                      String currency, String offerId, String offername, String catName,
                      String bannerImageUrl, ArrayList<ImageData> images, int discountType,
                      String parentProductId, String availableQuantity, String childProductId,
                      double discountPrice, CategoryOfferData offers, SupplierData supplier,
                      String unitId, boolean outOfStock,
                      FinalPriceListData finalPriceList, String productName,
                      String brandName, String imageMobile, String subCatName, int type, int totalStarRating,
                      String offerName, boolean prescriptionRequired, boolean isVariantsAvailable,
                      String manufactureName) {
    this.subCategoryName = subCategoryName;
    this.penCount = penCount;
    this.productCount = productCount;
    this.id = id;
    this.imageUrl = imageUrl;
    this.logo = logo;
    this.name = name;
    this.bannerImage = bannerImage;
    this.offerdescription = offerdescription;
    this.currencySymbol = currencySymbol;
    this.currency = currency;
    this.offerId = offerId;
    this.offername = offername;
    this.catName = catName;
    this.bannerImageUrl = bannerImageUrl;
    this.images = images;
    this.discountType = discountType;
    this.parentProductId = parentProductId;
    this.availableQuantity = availableQuantity;
    this.childProductId = childProductId;
    this.discountPrice = discountPrice;
    this.offers = offers;
    this.supplier = supplier;
    this.unitId = unitId;
    this.outOfStock = outOfStock;
    this.finalPriceList = finalPriceList;
    this.productName = productName;
    this.brandName = brandName;
    this.imageMobile = imageMobile;
    this.type = type;
    this.subCatName = subCatName;
    this.mTotalStarRating = totalStarRating;
    this.offerName = offerName;
    this.prescriptionRequired = prescriptionRequired;
    this.isVariantsAvailable = isVariantsAvailable;
    this.manufactureName = manufactureName;
  }

  public CategoryData(CategoryOfferData offers, String availableQuantity, String brandName,
      ArrayList<ImageData> images, String parentProductId, String childProductId,
      String currencySymbol,
      int totalStarRating, String productName, SupplierData supplier, boolean outOfStock,
      String unitId, String currency, int discountType,
      FinalPriceListData finalPriceList) {
    this.finalPriceList = finalPriceList;
    this.unitId = unitId;
    this.discountType = discountType;
    this.currency = currency;
    this.supplier = supplier;
    this.productName = productName;
    this.mTotalStarRating = totalStarRating;
    this.currencySymbol = currencySymbol;
    this.childProductId = childProductId;
    this.parentProductId = parentProductId;
    this.images = images;
    this.offers = offers;
    this.availableQuantity = availableQuantity;
    this.brandName = brandName;
    this.outOfStock = outOfStock;
  }

  public CategoryData(CategoryOfferData offers, String availableQuantity, String brandName,
                      ArrayList<ImageData> images, String parentProductId, String childProductId,
                      String currencySymbol,
                      int totalStarRating, String productName, SupplierData supplier, boolean outOfStock,
                      String unitId, String currency, int discountType,
                      FinalPriceListData finalPriceList, boolean prescriptionRequired, String manufactureName) {
    this.finalPriceList = finalPriceList;
    this.unitId = unitId;
    this.discountType = discountType;
    this.currency = currency;
    this.supplier = supplier;
    this.productName = productName;
    this.mTotalStarRating = totalStarRating;
    this.currencySymbol = currencySymbol;
    this.childProductId = childProductId;
    this.parentProductId = parentProductId;
    this.images = images;
    this.offers = offers;
    this.availableQuantity = availableQuantity;
    this.brandName = brandName;
    this.outOfStock = outOfStock;
    this.prescriptionRequired = prescriptionRequired;
    this.manufactureName = manufactureName;
  }

  protected CategoryData(Parcel in) {
    subCategoryName = in.readString();
    penCount = in.readInt();
    productCount = in.readInt();
    id = in.readString();
    imageUrl = in.readString();
    logo = in.readString();
    name = in.readString();
    bannerImage = in.readString();
    offerdescription = in.readString();
    currencySymbol = in.readString();
    currency = in.readString();
    offerId = in.readString();
    offername = in.readString();
    offerName = in.readString();
    catName = in.readString();
    bannerImageUrl = in.readString();
    images = in.createTypedArrayList(ImageData.CREATOR);
    discountType = in.readInt();
    parentProductId = in.readString();
    availableQuantity = in.readString();
    childProductId = in.readString();
    discountPrice = in.readDouble();
    unitId = in.readString();
    outOfStock = in.readByte() != 0;
    finalPriceList = in.readParcelable(FinalPriceListData.class.getClassLoader());
    productName = in.readString();
    brandName = in.readString();
    manufactureName = in.readString();
    imageMobile = in.readString();
    subCatName = in.readString();
    type = in.readInt();
    cartQuantity = in.readInt();
    prescriptionRequired = in.readByte() != 0;
    isVariantsAvailable = in.readByte() != 0;
    mTotalStarRating = in.readInt();
    mobileImage = in.readString();
    webImage = in.readString();
    subCategoryList = in.createTypedArrayList(SubCategoryData.CREATOR);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(subCategoryName);
    dest.writeInt(penCount);
    dest.writeInt(productCount);
    dest.writeString(id);
    dest.writeString(imageUrl);
    dest.writeString(logo);
    dest.writeString(name);
    dest.writeString(bannerImage);
    dest.writeString(offerdescription);
    dest.writeString(currencySymbol);
    dest.writeString(currency);
    dest.writeString(offerId);
    dest.writeString(offername);
    dest.writeString(offerName);
    dest.writeString(catName);
    dest.writeString(bannerImageUrl);
    dest.writeTypedList(images);
    dest.writeInt(discountType);
    dest.writeString(parentProductId);
    dest.writeString(availableQuantity);
    dest.writeString(childProductId);
    dest.writeDouble(discountPrice);
    dest.writeString(unitId);
    dest.writeByte((byte) (outOfStock ? 1 : 0));
    dest.writeParcelable(finalPriceList, flags);
    dest.writeString(productName);
    dest.writeString(brandName);
    dest.writeString(manufactureName);
    dest.writeString(imageMobile);
    dest.writeString(subCatName);
    dest.writeInt(type);
    dest.writeInt(cartQuantity);
    dest.writeByte((byte) (prescriptionRequired ? 1 : 0));
    dest.writeByte((byte) (isVariantsAvailable ? 1 : 0));
    dest.writeInt(mTotalStarRating);
    dest.writeString(mobileImage);
    dest.writeString(webImage);
    dest.writeTypedList(subCategoryList);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
    @Override
    public CategoryData createFromParcel(Parcel in) {
      return new CategoryData(in);
    }

    @Override
    public CategoryData[] newArray(int size) {
      return new CategoryData[size];
    }
  };

  public ArrayList<SubCategoryData> getSubCategoryList() {
    return subCategoryList;
  }

  public void setSubCategoryList(
      ArrayList<SubCategoryData> subCategoryList) {
    this.subCategoryList = subCategoryList;
  }

  public int getTotalStarRating() {
    return mTotalStarRating;
  }

  public void setTotalStarRating(int totalStarRating) {
    mTotalStarRating = totalStarRating;
  }

  public String getSubCatName() {
    return subCatName;
  }

  public void setSubCatName(String subCatName) {
    this.subCatName = subCatName;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getCartQuantity() {
    return cartQuantity;
  }

  public void setCartQuantity(int cartQuantity) {
    this.cartQuantity = cartQuantity;
  }


  public String getImageMobile() {
    return imageMobile;
  }

  public void setImageMobile(String imageMobile) {
    this.imageMobile = imageMobile;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getSubCategoryName() {
    return subCategoryName;
  }

  public void setSubCategoryName(String subCategoryName) {
    this.subCategoryName = subCategoryName;
  }

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public int getProductCount() {
    return productCount;
  }

  public void setProductCount(int productCount) {
    this.productCount = productCount;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBannerImage() {
    return bannerImage;
  }

  public void setBannerImage(String bannerImage) {
    this.bannerImage = bannerImage;
  }

  public String getOfferdescription() {
    return offerdescription;
  }

  public void setOfferdescription(String offerdescription) {
    this.offerdescription = offerdescription;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getOfferId() {
    return offerId;
  }

  public void setOfferId(String offerId) {
    this.offerId = offerId;
  }

  public String getOffername() {
    return offername;
  }

  public void setOffername(String offername) {
    this.offername = offername;
  }

  public String getCatName() {
    return catName;
  }

  public void setCatName(String catName) {
    this.catName = catName;
  }

  public String getBannerImageUrl() {
    return bannerImageUrl;
  }

  public void setBannerImageUrl(String bannerImageUrl) {
    this.bannerImageUrl = bannerImageUrl;
  }

  public ArrayList<ImageData> getImages() {
    return images;
  }

  public void setImages(ArrayList<ImageData> images) {
    this.images = images;
  }

  public int getDiscountType() {
    return discountType;
  }

  public void setDiscountType(int discountType) {
    this.discountType = discountType;
  }

  public String getParentProductId() {
    return parentProductId;
  }

  public void setParentProductId(String parentProductId) {
    this.parentProductId = parentProductId;
  }

  public String getAvailableQuantity() {
    return availableQuantity;
  }

  public void setAvailableQuantity(String availableQuantity) {
    this.availableQuantity = availableQuantity;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public double getDiscountPrice() {
    return discountPrice;
  }

  public void setDiscountPrice(double discountPrice) {
    this.discountPrice = discountPrice;
  }

  public CategoryOfferData getOffers() {
    return offers;
  }

  public void setOffers(CategoryOfferData offers) {
    this.offers = offers;
  }

  public SupplierData getSupplier() {
    return supplier;
  }

  public void setSupplier(SupplierData supplier) {
    this.supplier = supplier;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public boolean isOutOfStock() {
    return outOfStock;
  }

  public void setOutOfStock(boolean outOfStock) {
    this.outOfStock = outOfStock;
  }

  public FinalPriceListData getFinalPriceList() {
    return finalPriceList;
  }

  public void setFinalPriceList(FinalPriceListData finalPriceList) {
    this.finalPriceList = finalPriceList;
  }

  public String getManufactureName() {
    return manufactureName;
  }

  public void setManufactureName(String manufactureName) {
    this.manufactureName = manufactureName;
  }

  public String getOfferName() {
    return offerName;
  }

  public void setOfferName(String offerName) {
    this.offerName = offerName;
  }

  public boolean isPrescriptionRequired() {
    return prescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    this.prescriptionRequired = prescriptionRequired;
  }

  public boolean isVariantsAvailable() {
    return isVariantsAvailable;
  }

  public void setVariantsAvailable(boolean variantsAvailable) {
    isVariantsAvailable = variantsAvailable;
  }

  public String getMobileImage() {
    return mobileImage;
  }

  public void setMobileImage(String mobileImage) {
    this.mobileImage = mobileImage;
  }

  public String getWebImage() {
    return webImage;
  }

  public void setWebImage(String webImage) {
    this.webImage = webImage;
  }
}
