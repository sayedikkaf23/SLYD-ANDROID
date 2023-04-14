package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.BasicSizeDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.FinalPriceListDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.SizeChart;
import java.util.ArrayList;

public class ProductModelDetails implements ValidItem,Parcelable {

  @SerializedName("offers")
  @Expose
  private PdpOfferDetails offers;
  @SerializedName("allOffers")
  @Expose
  private ArrayList<PdpOfferDetails> allOffers;
  @SerializedName("brandName")
  @Expose
  private String brandName;
  @SerializedName("shareLink")
  @Expose
  private String shareLink;
  @SerializedName("images")
  @Expose
  private ArrayList<ImagesDetails> images;
  @SerializedName("parentProductId")
  @Expose
  private String parentProductId;
  @SerializedName("childProductId")
  @Expose
  private String childProductId;
  @SerializedName("subCatName")
  @Expose
  private String subCatName;
  @SerializedName("currencySymbol")
  @Expose
  private String currencySymbol;
  @SerializedName("productName")
  @Expose
  private String productName;
  @SerializedName("sizeChart")
  @Expose
  private ArrayList<SizeChart> sizeChart;
  @SerializedName("colors")
  @Expose
  private ArrayList<ColorsDetails> colors;
  @SerializedName("subSubCatName")
  @Expose
  private String subSubCatName;
  @SerializedName("detailDesc")
  @Expose
  private String detailDesc;
  @SerializedName("highlight")
  @Expose
  private ArrayList<String> highlight;
  @SerializedName("sizes")
  @Expose
  private ArrayList<BasicSizeDetails> sizes;
  @SerializedName("isPrimary")
  @Expose
  private String isPrimary;
  @SerializedName("catName")
  @Expose
  private String catName;
  @SerializedName("supplier")
  @Expose
  private Supplier supplier;
  @SerializedName("sellerCount")
  @Expose
  private int sellerCount;
  @SerializedName("unitId")
  @Expose
  private String unitId;
  @SerializedName("currency")
  @Expose
  private String currency;
  @SerializedName("shortDesc")
  @Expose
  private String shortDesc;
  @SerializedName("attributes")
  @Expose
  private ArrayList<AttributesDetails> attributes;
  @SerializedName("isFavourite")
  @Expose
  private boolean isFavourite;
  @SerializedName("finalPriceList")
  @Expose
  private FinalPriceListDetails finalPriceList;
  @SerializedName("linkToUnit")
  @Expose
  private ArrayList<LinkToUnit> linkToUnit;
  @SerializedName("mouUnit")
  @Expose
  private String mouUnit;
  @SerializedName("discountType")
  @Expose
  private int discountType;
  @SerializedName("outOfStock")
  @Expose
  private boolean outOfStock;
  @SerializedName("mou")
  @Expose
  private String mou;
  @SerializedName("availableQuantity")
  @Expose
  private int availableQuantity;
  @SerializedName("prescriptionRequired")
  @Expose
  private boolean isPrescriptionRequired;
  @SerializedName("manufactureName")
  @Expose
  private String manufactureName;
  @SerializedName("mouDataUnit")
  @Expose
  private String mouDataUnit;
  @SerializedName("variants")
  @Expose
  private ArrayList<VariantDetails> variants;
  @SerializedName("htmlAttributes")
  @Expose
  private ArrayList<AttributesDetails> htmlAttributes;
  @SerializedName("isShoppingList")
  @Expose
  private boolean isShoppingList;

  public ProductModelDetails(ArrayList<VariantDetails> variants, PdpOfferDetails offers,
      String brandName, ArrayList<ImagesDetails> images, String parentProductId,
      String childProductId, String subCatName, String currencySymbol, String productName,
      ArrayList<ColorsDetails> colors, String subSubCatName, String detailDesc,
      ArrayList<String> highlight, ArrayList<BasicSizeDetails> sizes, String isPrimary,
      String catName, Supplier supplier, String unitId, String currency, String shortDesc,
      ArrayList<AttributesDetails> attributes, boolean isFavorite,
      FinalPriceListDetails finalPriceList, ArrayList<LinkToUnit> linkToUnit, String mouUnit,
      int discountType, boolean outOfStock, String mou, int availableQuantity,
      ArrayList<SizeChart> sizeChart, int sellerCount, boolean isShoppingList) {
    this.offers = offers;
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
    this.isPrimary = isPrimary;
    this.catName = catName;
    this.supplier = supplier;
    this.unitId = unitId;
    this.currency = currency;
    this.shortDesc = shortDesc;
    this.attributes = attributes;
    this.isFavourite = isFavorite;
    this.finalPriceList = finalPriceList;
    this.linkToUnit = linkToUnit;
    this.mouUnit = mouUnit;
    this.discountType = discountType;
    this.outOfStock = outOfStock;
    this.mou = mou;
    this.availableQuantity = availableQuantity;
    this.variants = variants;
    this.sizeChart = sizeChart;
    this.sellerCount = sellerCount;
    this.isShoppingList = isShoppingList;
  }

  protected ProductModelDetails(Parcel in) {
    offers = in.readParcelable(PdpOfferDetails.class.getClassLoader());
    allOffers = in.createTypedArrayList(PdpOfferDetails.CREATOR);
    brandName = in.readString();
    shareLink = in.readString();
    images = in.createTypedArrayList(ImagesDetails.CREATOR);
    parentProductId = in.readString();
    childProductId = in.readString();
    subCatName = in.readString();
    currencySymbol = in.readString();
    productName = in.readString();
    sizeChart = in.createTypedArrayList(SizeChart.CREATOR);
    colors = in.createTypedArrayList(ColorsDetails.CREATOR);
    subSubCatName = in.readString();
    detailDesc = in.readString();
    highlight = in.createStringArrayList();
    sizes = in.createTypedArrayList(BasicSizeDetails.CREATOR);
    isPrimary = in.readString();
    catName = in.readString();
    supplier = in.readParcelable(Supplier.class.getClassLoader());
    sellerCount = in.readInt();
    unitId = in.readString();
    currency = in.readString();
    shortDesc = in.readString();
    attributes = in.createTypedArrayList(AttributesDetails.CREATOR);
    isFavourite = in.readByte() != 0;
    finalPriceList = in.readParcelable(FinalPriceListDetails.class.getClassLoader());
    linkToUnit = in.createTypedArrayList(LinkToUnit.CREATOR);
    mouUnit = in.readString();
    discountType = in.readInt();
    outOfStock = in.readByte() != 0;
    mou = in.readString();
    availableQuantity = in.readInt();
    isPrescriptionRequired = in.readByte() != 0;
    manufactureName = in.readString();
    mouDataUnit = in.readString();
    variants = in.createTypedArrayList(VariantDetails.CREATOR);
    htmlAttributes = in.createTypedArrayList(AttributesDetails.CREATOR);
    isShoppingList = in.readByte() != 0;
  }

  public static final Creator<ProductModelDetails> CREATOR = new Creator<ProductModelDetails>() {
    @Override
    public ProductModelDetails createFromParcel(Parcel in) {
      return new ProductModelDetails(in);
    }

    @Override
    public ProductModelDetails[] newArray(int size) {
      return new ProductModelDetails[size];
    }
  };

  public String getShareLink() {
    return shareLink;
  }

  public void setShareLink(String shareLink) {
    this.shareLink = shareLink;
  }

  public ArrayList<VariantDetails> getVariants() {
    return variants;
  }

  public void setVariants(ArrayList<VariantDetails> variants) {
    this.variants = variants;
  }

  public int getSellerCount() {
    return sellerCount;
  }

  public void setSellerCount(int sellerCount) {
    this.sellerCount = sellerCount;
  }

  public PdpOfferDetails getOffers() {
    return offers;
  }

  public void setOffers(PdpOfferDetails offers) {
    this.offers = offers;
  }

  public boolean isFavorite() {
    return isFavourite;
  }

  public void setFavorite(boolean favorite) {
    isFavourite = favorite;
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

  public ArrayList<LinkToUnit> getLinkToUnit() {
    return linkToUnit;
  }

  public void setLinkToUnit(ArrayList<LinkToUnit> linkToUnit) {
    this.linkToUnit = linkToUnit;
  }

  public ArrayList<SizeChart> getSizeChart() {
    return sizeChart;
  }

  public void setSizeChart(ArrayList<SizeChart> sizeChart) {
    this.sizeChart = sizeChart;
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

  public ArrayList<ColorsDetails> getColors() {
    return colors;
  }

  public void setColors(ArrayList<ColorsDetails> colors) {
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

  public ArrayList<BasicSizeDetails> getSizes() {
    return sizes;
  }

  public void setSizes(ArrayList<BasicSizeDetails> sizes) {
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

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
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

  public ArrayList<AttributesDetails> getAttributes() {
    return attributes;
  }

  public void setAttributes(ArrayList<AttributesDetails> attributes) {
    this.attributes = attributes;
  }

  public boolean getIsFavourite() {
    return isFavourite;
  }

  public void setIsFavourite(boolean isFavourite) {
    this.isFavourite = isFavourite;
  }

  public FinalPriceListDetails getFinalPriceList() {
    return finalPriceList;
  }

  public void setFinalPriceList(FinalPriceListDetails finalPriceList) {
    this.finalPriceList = finalPriceList;
  }

  public ArrayList<PdpOfferDetails> getAllOffers() {
    return allOffers;
  }

  public void setAllOffers(
      ArrayList<PdpOfferDetails> allOffers) {
    this.allOffers = allOffers;
  }

  public boolean isPrescriptionRequired() {
    return isPrescriptionRequired;
  }

  public void setPrescriptionRequired(boolean prescriptionRequired) {
    isPrescriptionRequired = prescriptionRequired;
  }

  public ArrayList<AttributesDetails> getHtmlAttributes() {
    return htmlAttributes;
  }

  public void setHtmlAttributes(ArrayList<AttributesDetails> htmlAttributes) {
    this.htmlAttributes = htmlAttributes;
  }

  public String getManufactureName() {
    return manufactureName;
  }

  public void setManufactureName(String manufactureName) {
    this.manufactureName = manufactureName;
  }

  public String getMouDataUnit() {
    return mouDataUnit;
  }

  public void setMouDataUnit(String mouDataUnit) {
    this.mouDataUnit = mouDataUnit;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  public boolean isShoppingList() {
    return isShoppingList;
  }

  public void setShoppingList(boolean shoppingList) {
    isShoppingList = shoppingList;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(offers, flags);
    dest.writeTypedList(allOffers);
    dest.writeString(brandName);
    dest.writeString(shareLink);
    dest.writeTypedList(images);
    dest.writeString(parentProductId);
    dest.writeString(childProductId);
    dest.writeString(subCatName);
    dest.writeString(currencySymbol);
    dest.writeString(productName);
    dest.writeTypedList(sizeChart);
    dest.writeTypedList(colors);
    dest.writeString(subSubCatName);
    dest.writeString(detailDesc);
    dest.writeStringList(highlight);
    dest.writeTypedList(sizes);
    dest.writeString(isPrimary);
    dest.writeString(catName);
    dest.writeParcelable(supplier, flags);
    dest.writeInt(sellerCount);
    dest.writeString(unitId);
    dest.writeString(currency);
    dest.writeString(shortDesc);
    dest.writeTypedList(attributes);
    dest.writeByte((byte) (isFavourite ? 1 : 0));
    dest.writeParcelable(finalPriceList, flags);
    dest.writeTypedList(linkToUnit);
    dest.writeString(mouUnit);
    dest.writeInt(discountType);
    dest.writeByte((byte) (outOfStock ? 1 : 0));
    dest.writeString(mou);
    dest.writeInt(availableQuantity);
    dest.writeByte((byte) (isPrescriptionRequired ? 1 : 0));
    dest.writeString(manufactureName);
    dest.writeString(mouDataUnit);
    dest.writeTypedList(variants);
    dest.writeTypedList(htmlAttributes);
    dest.writeByte((byte) (isShoppingList ? 1 : 0));
  }
}
