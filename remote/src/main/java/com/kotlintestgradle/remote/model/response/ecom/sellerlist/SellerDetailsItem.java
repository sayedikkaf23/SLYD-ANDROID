package com.kotlintestgradle.remote.model.response.ecom.sellerlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.Supplier;
import java.util.ArrayList;

public class SellerDetailsItem implements Parcelable, ValidItem {

  @SerializedName("images")
  @Expose
  private ArrayList<ImagesDetails> images;

  @SerializedName("productSeo")
  @Expose
  private ProductSeoDetails productSeo;

  @SerializedName("exchangePolicy")
  @Expose
  private ExchangePolicyDetails exchangePolicy;

  @SerializedName("supplier")
  @Expose
  private ArrayList<Supplier> supplier;

  @SerializedName("returnPolicy")
  @Expose
  private ReturnPolicyDetails returnPolicy;

  @SerializedName("currencySymbol")
  @Expose
  private String currencySymbol;

  @SerializedName("warranty")
  @Expose
  private WarrantyDetails warranty;

  @SerializedName("currency")
  @Expose
  private String currency;

  @SerializedName("TotalStarRating")
  @Expose
  private String TotalStarRating;

  @SerializedName("replacementPolicy")
  @Expose
  private ReplacementPolicyDetails replacementPolicy;

  @SerializedName("term&condition")
  @Expose
  private TermAndConditionDetails termAndcondition;

  @SerializedName("productName")
  @Expose
  private String productName;

  public SellerDetailsItem(
      ArrayList<ImagesDetails> images,
      ProductSeoDetails productSeo,
      ExchangePolicyDetails exchangePolicy,
      ArrayList<Supplier> supplier,
      ReturnPolicyDetails returnPolicy, String currencySymbol,
      WarrantyDetails warranty, String currency, String totalStarRating,
      ReplacementPolicyDetails replacementPolicy,
      TermAndConditionDetails termAndcondition, String productName) {
    this.images = images;
    this.productSeo = productSeo;
    this.exchangePolicy = exchangePolicy;
    this.supplier = supplier;
    this.returnPolicy = returnPolicy;
    this.currencySymbol = currencySymbol;
    this.warranty = warranty;
    this.currency = currency;
    TotalStarRating = totalStarRating;
    this.replacementPolicy = replacementPolicy;
    this.termAndcondition = termAndcondition;
    this.productName = productName;
  }

  protected SellerDetailsItem(Parcel in) {
    images = in.createTypedArrayList(ImagesDetails.CREATOR);
    productSeo = in.readParcelable(ProductSeoDetails.class.getClassLoader());
    exchangePolicy = in.readParcelable(ExchangePolicyDetails.class.getClassLoader());
    supplier = in.createTypedArrayList(Supplier.CREATOR);
    returnPolicy = in.readParcelable(ReturnPolicyDetails.class.getClassLoader());
    currencySymbol = in.readString();
    currency = in.readString();
    TotalStarRating = in.readString();
    replacementPolicy = in.readParcelable(ReplacementPolicyDetails.class.getClassLoader());
    productName = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(images);
    dest.writeParcelable(productSeo, flags);
    dest.writeParcelable(exchangePolicy, flags);
    dest.writeTypedList(supplier);
    dest.writeParcelable(returnPolicy, flags);
    dest.writeString(currencySymbol);
    dest.writeString(currency);
    dest.writeString(TotalStarRating);
    dest.writeParcelable(replacementPolicy, flags);
    dest.writeString(productName);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SellerDetailsItem> CREATOR = new Creator<SellerDetailsItem>() {
    @Override
    public SellerDetailsItem createFromParcel(Parcel in) {
      return new SellerDetailsItem(in);
    }

    @Override
    public SellerDetailsItem[] newArray(int size) {
      return new SellerDetailsItem[size];
    }
  };

  public ArrayList<ImagesDetails> getImages() {
    return images;
  }

  public void setImages(
      ArrayList<ImagesDetails> images) {
    this.images = images;
  }

  public ProductSeoDetails getProductSeo() {
    return productSeo;
  }

  public void setProductSeo(
      ProductSeoDetails productSeo) {
    this.productSeo = productSeo;
  }

  public ExchangePolicyDetails getExchangePolicy() {
    return exchangePolicy;
  }

  public void setExchangePolicy(
      ExchangePolicyDetails exchangePolicy) {
    this.exchangePolicy = exchangePolicy;
  }

  public ArrayList<Supplier> getSupplier() {
    return supplier;
  }

  public void setSupplier(
      ArrayList<Supplier> supplier) {
    this.supplier = supplier;
  }

  public ReturnPolicyDetails getReturnPolicy() {
    return returnPolicy;
  }

  public void setReturnPolicy(
      ReturnPolicyDetails returnPolicy) {
    this.returnPolicy = returnPolicy;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public WarrantyDetails getWarranty() {
    return warranty;
  }

  public void setWarranty(WarrantyDetails warranty) {
    this.warranty = warranty;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getTotalStarRating() {
    return TotalStarRating;
  }

  public void setTotalStarRating(String totalStarRating) {
    TotalStarRating = totalStarRating;
  }

  public ReplacementPolicyDetails getReplacementPolicy() {
    return replacementPolicy;
  }

  public void setReplacementPolicy(
      ReplacementPolicyDetails replacementPolicy) {
    this.replacementPolicy = replacementPolicy;
  }

  public TermAndConditionDetails getTermAndcondition() {
    return termAndcondition;
  }

  public void setTermAndcondition(
      TermAndConditionDetails termAndcondition) {
    this.termAndcondition = termAndcondition;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
