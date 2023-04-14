package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class SupplierDetails  implements Parcelable, ValidItem {
  @SerializedName("retailerPrice")
  @Expose
  private String retailerPrice;
  @SerializedName("supplierName")
  @Expose
  private String supplierName;
  @SerializedName("rating")
  @Expose
  private String rating;
  @SerializedName("currency")
  @Expose
  private String currency;
  @SerializedName("productId")
  @Expose
  private String productId;

  public SupplierDetails(String retailerPrice, String supplierName, String rating,
      String currency,String productId) {
    this.retailerPrice = retailerPrice;
    this.supplierName = supplierName;
    this.rating = rating;
    this.currency = currency;
    this.productId=productId;
  }

  protected SupplierDetails(Parcel in) {
    retailerPrice = in.readString();
    supplierName = in.readString();
    rating = in.readString();
    currency = in.readString();
    productId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(retailerPrice);
    dest.writeString(supplierName);
    dest.writeString(rating);
    dest.writeString(currency);
    dest.writeString(productId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SupplierDetails> CREATOR = new Creator<SupplierDetails>() {
    @Override
    public SupplierDetails createFromParcel(Parcel in) {
      return new SupplierDetails(in);
    }

    @Override
    public SupplierDetails[] newArray(int size) {
      return new SupplierDetails[size];
    }
  };

  public String getRetailerPrice() {
    return retailerPrice;
  }

  public void setRetailerPrice(String retailerPrice) {
    this.retailerPrice = retailerPrice;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
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

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
