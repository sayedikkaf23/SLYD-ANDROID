package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.FinalPriceListDetails;
import java.util.ArrayList;

public class Supplier implements ValidItem, Parcelable {
  @SerializedName("productId")
  @Expose
  private String productId;
  @SerializedName("retailerQty")
  @Expose
  private String retailerQty;
  @SerializedName("retailerPrice")
  @Expose
  private String retailerPrice;
  @SerializedName("currencySymbol")
  @Expose
  private String currencySymbol;
  @SerializedName("distributorPrice")
  @Expose
  private String distributorPrice;
  @SerializedName("currency")
  @Expose
  private String currency;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("distributorQty")
  @Expose
  private String distributorQty;
  @SerializedName("supplierName")
  @Expose
  private String supplierName;
  @SerializedName("rating")
  @Expose
  private String rating;
  @SerializedName("totalRating")
  @Expose
  private String totalRating;
  @SerializedName("supplier")
  @Expose
  private SupplierDetails supplier;
  @SerializedName("reviewParameter")
  @Expose
  private ArrayList<ReviewParametersDetails> reviewParameter;

  @SerializedName("finalPriceList")
  @Expose
  private FinalPriceListDetails finalPriceList;

  @SerializedName("sellerSince")
  @Expose
  private String sellerSince;

  public Supplier(String productId, String retailerQty, String retailerPrice, String currencySymbol,
      String distributorPrice, String currency, String id, String distributorQty,
      String supplierName, String rating, String totalRating, SupplierDetails supplier,
      FinalPriceListDetails finalPriceList, ArrayList<ReviewParametersDetails> reviewParameter,
      String sellerSince) {
    this.productId = productId;
    this.retailerQty = retailerQty;
    this.retailerPrice = retailerPrice;
    this.currencySymbol = currencySymbol;
    this.distributorPrice = distributorPrice;
    this.currency = currency;
    this.id = id;
    this.distributorQty = distributorQty;
    this.supplierName = supplierName;
    this.rating = rating;
    this.totalRating = totalRating;
    this.supplier = supplier;
    this.finalPriceList = finalPriceList;
    this.reviewParameter = reviewParameter;
    this.sellerSince = sellerSince;
  }

  protected Supplier(Parcel in) {
    productId = in.readString();
    retailerQty = in.readString();
    retailerPrice = in.readString();
    currencySymbol = in.readString();
    distributorPrice = in.readString();
    currency = in.readString();
    id = in.readString();
    distributorQty = in.readString();
    supplierName = in.readString();
    rating = in.readString();
    totalRating = in.readString();
    reviewParameter = in.createTypedArrayList(ReviewParametersDetails.CREATOR);
    finalPriceList = in.readParcelable(FinalPriceListDetails.class.getClassLoader());
    sellerSince = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(productId);
    dest.writeString(retailerQty);
    dest.writeString(retailerPrice);
    dest.writeString(currencySymbol);
    dest.writeString(distributorPrice);
    dest.writeString(currency);
    dest.writeString(id);
    dest.writeString(distributorQty);
    dest.writeString(supplierName);
    dest.writeString(rating);
    dest.writeString(totalRating);
    dest.writeTypedList(reviewParameter);
    dest.writeParcelable(finalPriceList, flags);
    dest.writeString(sellerSince);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Supplier> CREATOR = new Creator<Supplier>() {
    @Override
    public Supplier createFromParcel(Parcel in) {
      return new Supplier(in);
    }

    @Override
    public Supplier[] newArray(int size) {
      return new Supplier[size];
    }
  };

  public String getSellerSince() {
    return sellerSince;
  }

  public void setSellerSince(String sellerSince) {
    this.sellerSince = sellerSince;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public FinalPriceListDetails getFinalPriceList() {
    return finalPriceList;
  }

  public void setFinalPriceList(
      FinalPriceListDetails finalPriceList) {
    this.finalPriceList = finalPriceList;
  }

  public SupplierDetails getSupplier() {
    return supplier;
  }

  public void setSupplier(SupplierDetails supplier) {
    this.supplier = supplier;
  }

  public ArrayList<ReviewParametersDetails> getReviewParameter() {
    return reviewParameter;
  }

  public void setReviewParameter(
      ArrayList<ReviewParametersDetails> reviewParameter) {
    this.reviewParameter = reviewParameter;
  }

  public String getRetailerQty() {
    return retailerQty;
  }

  public void setRetailerQty(String retailerQty) {
    this.retailerQty = retailerQty;
  }

  public String getRetailerPrice() {
    return retailerPrice;
  }

  public void setRetailerPrice(String retailerPrice) {
    this.retailerPrice = retailerPrice;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getDistributorPrice() {
    return distributorPrice;
  }

  public void setDistributorPrice(String distributorPrice) {
    this.distributorPrice = distributorPrice;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getTotalRating() {
    return totalRating;
  }

  public void setTotalRating(String totalRating) {
    this.totalRating = totalRating;
  }

  public String getDistributorQty() {
    return distributorQty;
  }

  public void setDistributorQty(String distributorQty) {
    this.distributorQty = distributorQty;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
