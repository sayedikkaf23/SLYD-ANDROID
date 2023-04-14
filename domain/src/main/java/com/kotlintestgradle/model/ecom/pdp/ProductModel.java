package com.kotlintestgradle.model.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.common.FinalPriceListData;
import com.kotlintestgradle.model.ecom.common.ImageData;
import java.util.ArrayList;

public class ProductModel implements Parcelable {
  private PdpOfferData offers;
  private ArrayList<PdpOfferData> allOffers;
  private String brandName;
  private ArrayList<ImageData> images;
  private String parentProductId;
  private String childProductId;
  private String subCatName;
  private String currencySymbol;
  private String productName;
  private ArrayList<ColorsData> colors;
  private String subSubCatName;
  private String detailDesc;
  private ArrayList<String> highlight;
  private ArrayList<SizeData> sizes;
  private String isPrimary;
  private String catName;
  private SupplierData supplier;
  private String unitId;
  private String currency;
  private String shortDesc;
  private ArrayList<AttributesData> attributes;
  private ArrayList<AttributesData> htmlAttributes;
  private boolean isFavorite;

  private boolean isPrescriptionRequired;
  private String mouDataUnit;

  private FinalPriceListData finalPriceList;
  private ArrayList<LinkToUnitData> linkToUnit;
  private String mouUnit;
  private int discountType;
  private boolean outOfStock;
  private String mou;
  private String shareLink;
  private int availableQuantity;
  private ArrayList<VariantsData> variantsData;
  private ArrayList<SizeChartData> mSizeChartData;
  private int sellerCount;
  private String manufacturerName;
  private boolean isShoppingList;

  public ProductModel(ArrayList<VariantsData> variantsData, PdpOfferData offers,
      ArrayList<PdpOfferData> allOffers, String brandName,
      ArrayList<ImageData> images, String parentProductId, String childProductId, String subCatName,
      String currencySymbol, String productName, ArrayList<ColorsData> colors, String subSubCatName,
      String detailDesc, ArrayList<String> highlight, ArrayList<SizeData> sizes, String isPrimary,
      String catName, SupplierData supplier, String unitId, String currency, String shortDesc,
      ArrayList<AttributesData> attributes, boolean isFavorite, FinalPriceListData finalPriceList,
      ArrayList<LinkToUnitData> linkToUnit, String mouUnit, int discountType, boolean outOfStock,
      String mou, int availableQuantity, ArrayList<SizeChartData> sizeChartData, int sellerCount,
                      String shareLink) {
    this.offers = offers;
    this.allOffers = allOffers;
    this.brandName = brandName;
    this.images = images;
    this.parentProductId = parentProductId;
    this.childProductId = childProductId;
    this.subCatName = subCatName;
    this.currencySymbol = currencySymbol;
    this.productName = productName;
    this.colors = colors;
    this.subSubCatName = subSubCatName;
    this.detailDesc = detailDesc;
    this.highlight = highlight;
    this.sizes = sizes;
    this.mSizeChartData = sizeChartData;
    this.isPrimary = isPrimary;
    this.catName = catName;
    this.supplier = supplier;
    this.unitId = unitId;
    this.currency = currency;
    this.shortDesc = shortDesc;
    this.attributes = attributes;
    this.isFavorite = isFavorite;
    this.finalPriceList = finalPriceList;
    this.linkToUnit = linkToUnit;
    this.mouUnit = mouUnit;
    this.discountType = discountType;
    this.outOfStock = outOfStock;
    this.mou = mou;
    this.availableQuantity = availableQuantity;
    this.variantsData = variantsData;
    this.sellerCount = sellerCount;
    this.shareLink = shareLink;
  }

  public ProductModel(ArrayList<VariantsData> variantsData, PdpOfferData offers,
      ArrayList<PdpOfferData> allOffers, String brandName,
      ArrayList<ImageData> images, String parentProductId, String childProductId, String subCatName,
      String currencySymbol, String productName, ArrayList<ColorsData> colors, String subSubCatName,
      String detailDesc, ArrayList<String> highlight, ArrayList<SizeData> sizes, String isPrimary,
      String catName, SupplierData supplier, String unitId, String currency, String shortDesc,
      ArrayList<AttributesData> attributes, boolean isFavorite,
      FinalPriceListData finalPriceList, ArrayList<LinkToUnitData> linkToUnit,
      String mouUnit, int discountType, boolean outOfStock, String mou, int availableQuantity,
      ArrayList<SizeChartData> sizeChartData, int sellerCount, boolean isPrescriptionRequired,
      String manufacturerName, String mouDataUnit, ArrayList<AttributesData> htmlAttributes,
      boolean isShoppingList, String shareLink) {
    this.offers = offers;
    this.allOffers = allOffers;
    this.brandName = brandName;
    this.images = images;
    this.parentProductId = parentProductId;
    this.childProductId = childProductId;
    this.subCatName = subCatName;
    this.currencySymbol = currencySymbol;
    this.productName = productName;
    this.colors = colors;
    this.subSubCatName = subSubCatName;
    this.detailDesc = detailDesc;
    this.highlight = highlight;
    this.sizes = sizes;
    this.mSizeChartData = sizeChartData;
    this.isPrimary = isPrimary;
    this.catName = catName;
    this.supplier = supplier;
    this.unitId = unitId;
    this.currency = currency;
    this.shortDesc = shortDesc;
    this.attributes = attributes;
    this.isFavorite = isFavorite;
    this.finalPriceList = finalPriceList;
    this.linkToUnit = linkToUnit;
    this.mouUnit = mouUnit;
    this.discountType = discountType;
    this.outOfStock = outOfStock;
    this.mou = mou;
    this.availableQuantity = availableQuantity;
    this.variantsData = variantsData;
    this.sellerCount = sellerCount;
    this.isPrescriptionRequired = isPrescriptionRequired;
    this.manufacturerName = manufacturerName;
    this.mouDataUnit = mouDataUnit;
    this.htmlAttributes = htmlAttributes;
    this.isShoppingList = isShoppingList;
    this.shareLink = shareLink;
  }

  protected ProductModel(Parcel in) {
    brandName = in.readString();
    images = in.createTypedArrayList(ImageData.CREATOR);
    parentProductId = in.readString();
    childProductId = in.readString();
    subCatName = in.readString();
    currencySymbol = in.readString();
    productName = in.readString();
    colors = in.createTypedArrayList(ColorsData.CREATOR);
    subSubCatName = in.readString();
    detailDesc = in.readString();
    highlight = in.createStringArrayList();
    sizes = in.createTypedArrayList(SizeData.CREATOR);
    isPrimary = in.readString();
    catName = in.readString();
    unitId = in.readString();
    currency = in.readString();
    shortDesc = in.readString();
    attributes = in.createTypedArrayList(AttributesData.CREATOR);
    htmlAttributes = in.createTypedArrayList(AttributesData.CREATOR);
    isFavorite = in.readByte() != 0;
    isPrescriptionRequired = in.readByte() != 0;
    mouDataUnit = in.readString();
    finalPriceList = in.readParcelable(FinalPriceListData.class.getClassLoader());
    mouUnit = in.readString();
    discountType = in.readInt();
    outOfStock = in.readByte() != 0;
    mou = in.readString();
    shareLink = in.readString();
    availableQuantity = in.readInt();
    variantsData = in.createTypedArrayList(VariantsData.CREATOR);
    sellerCount = in.readInt();
    manufacturerName = in.readString();
    isShoppingList = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(brandName);
    dest.writeTypedList(images);
    dest.writeString(parentProductId);
    dest.writeString(childProductId);
    dest.writeString(subCatName);
    dest.writeString(currencySymbol);
    dest.writeString(productName);
    dest.writeTypedList(colors);
    dest.writeString(subSubCatName);
    dest.writeString(detailDesc);
    dest.writeStringList(highlight);
    dest.writeTypedList(sizes);
    dest.writeString(isPrimary);
    dest.writeString(catName);
    dest.writeString(unitId);
    dest.writeString(currency);
    dest.writeString(shortDesc);
    dest.writeTypedList(attributes);
    dest.writeTypedList(htmlAttributes);
    dest.writeByte((byte) (isFavorite ? 1 : 0));
    dest.writeByte((byte) (isPrescriptionRequired ? 1 : 0));
    dest.writeString(mouDataUnit);
    dest.writeParcelable(finalPriceList, flags);
    dest.writeString(mouUnit);
    dest.writeInt(discountType);
    dest.writeByte((byte) (outOfStock ? 1 : 0));
    dest.writeString(mou);
    dest.writeString(shareLink);
    dest.writeInt(availableQuantity);
    dest.writeTypedList(variantsData);
    dest.writeInt(sellerCount);
    dest.writeString(manufacturerName);
    dest.writeByte((byte) (isShoppingList ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
    @Override
    public ProductModel createFromParcel(Parcel in) {
      return new ProductModel(in);
    }

    @Override
    public ProductModel[] newArray(int size) {
      return new ProductModel[size];
    }
  };

  public ArrayList<VariantsData> getVariantsData() {
    return variantsData;
  }

  public void setVariantsData(ArrayList<VariantsData> variantsData) {
    this.variantsData = variantsData;
  }

  public PdpOfferData getOffers() {
    return offers;
  }

  public void setOffers(PdpOfferData offers) {
    this.offers = offers;
  }

  public int getSellerCount() {
    return sellerCount;
  }

  public void setSellerCount(int sellerCount) {
    this.sellerCount = sellerCount;
  }

  public boolean isFavorite() {
    return isFavorite;
  }

  public void setFavorite(boolean favorite) {
    isFavorite = favorite;
  }

  public ArrayList<SizeChartData> getSizeChartData() {
    return mSizeChartData;
  }

  public void setSizeChartData(ArrayList<SizeChartData> sizeChartData) {
    this.mSizeChartData = sizeChartData;
  }

  public String getMouUnit() {
    return mouUnit;
  }

  public void setMouUnit(String mouUnit) {
    this.mouUnit = mouUnit;
  }

  public int getDiscountType() {
    return discountType;
  }

  public void setDiscountType(int discountType) {
    this.discountType = discountType;
  }

  public boolean isOutOfStock() {
    return outOfStock;
  }

  public void setOutOfStock(boolean outOfStock) {
    this.outOfStock = outOfStock;
  }

  public String getMou() {
    return mou;
  }

  public void setMou(String mou) {
    this.mou = mou;
  }

  public int getAvailableQuantity() {
    return availableQuantity;
  }

  public void setAvailableQuantity(int availableQuantity) {
    this.availableQuantity = availableQuantity;
  }

  public ArrayList<LinkToUnitData> getLinkToUnit() {
    return linkToUnit;
  }

  public void setLinkToUnit(ArrayList<LinkToUnitData> linkToUnit) {
    this.linkToUnit = linkToUnit;
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

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public String getSubCatName() {
    return subCatName;
  }

  public void setSubCatName(String subCatName) {
    this.subCatName = subCatName;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public ArrayList<ColorsData> getColors() {
    return colors;
  }

  public void setColors(ArrayList<ColorsData> colors) {
    this.colors = colors;
  }

  public String getSubSubCatName() {
    return subSubCatName;
  }

  public void setSubSubCatName(String subSubCatName) {
    this.subSubCatName = subSubCatName;
  }

  public String getDetailDesc() {
    return detailDesc;
  }

  public void setDetailDesc(String detailDesc) {
    this.detailDesc = detailDesc;
  }

  public ArrayList<String> getHighlight() {
    return highlight;
  }

  public void setHighlight(ArrayList<String> highlight) {
    this.highlight = highlight;
  }

  public ArrayList<SizeData> getSizes() {
    return sizes;
  }

  public void setSizes(ArrayList<SizeData> sizes) {
    this.sizes = sizes;
  }

  public String getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(String isPrimary) {
    this.isPrimary = isPrimary;
  }

  public String getCatName() {
    return catName;
  }

  public void setCatName(String catName) {
    this.catName = catName;
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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getShortDesc() {
    return shortDesc;
  }

  public void setShortDesc(String shortDesc) {
    this.shortDesc = shortDesc;
  }

  public ArrayList<AttributesData> getAttributes() {
    return attributes;
  }

  public void setAttributes(ArrayList<AttributesData> attributes) {
    this.attributes = attributes;
  }

  public ArrayList<AttributesData> getHtmlAttributes() {
    return htmlAttributes;
  }

  public void setHtmlAttributes(ArrayList<AttributesData> htmlAttributes) {
    this.htmlAttributes = htmlAttributes;
  }

  public boolean getIsFavourite() {
    return isFavorite;
  }

  public void setIsFavourite(boolean isFavourite) {
    this.isFavorite = isFavourite;
  }

  public FinalPriceListData getFinalPriceList() {
    return finalPriceList;
  }

  public void setFinalPriceList(FinalPriceListData finalPriceList) {
    this.finalPriceList = finalPriceList;
  }

  public ArrayList<PdpOfferData> getAllOffers() {
    return allOffers;
  }

  public void setAllOffers(ArrayList<PdpOfferData> allOffers) {
    this.allOffers = allOffers;
  }

  public boolean isPrescriptionRequired() {
    return isPrescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    isPrescriptionRequired = prescriptionRequired;
  }

  public String getManufacturerName() {
    return manufacturerName;
  }

  public void setManufacturerName(String manufacturerName) {
    this.manufacturerName = manufacturerName;
  }

  public String getMouDataUnit() {
    return mouDataUnit;
  }

  public void setMouDataUnit(String mouDataUnit) {
    this.mouDataUnit = mouDataUnit;
  }

  public boolean isShoppingList() {
    return isShoppingList;
  }

  public String getShareLink() {
    return shareLink;
  }

  public void setShareLink(String shareLink) {
    this.shareLink = shareLink;
  }
}
