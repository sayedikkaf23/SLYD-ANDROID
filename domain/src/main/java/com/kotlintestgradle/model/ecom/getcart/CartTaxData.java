package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartTaxData implements Parcelable {

  private String totalValue;

  private String taxValue;

  private String taxId;

  private String taxName;

  public CartTaxData(String totalValue, String taxValue, String taxId, String taxName) {
    this.totalValue = totalValue;
    this.taxValue = taxValue;
    this.taxId = taxId;
    this.taxName = taxName;
  }

  protected CartTaxData(Parcel in) {
    totalValue = in.readString();
    taxValue = in.readString();
    taxId = in.readString();
    taxName = in.readString();
  }

  public static final Creator<CartTaxData> CREATOR = new Creator<CartTaxData>() {
    @Override
    public CartTaxData createFromParcel(Parcel in) {
      return new CartTaxData(in);
    }

    @Override
    public CartTaxData[] newArray(int size) {
      return new CartTaxData[size];
    }
  };

  public String getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(String totalValue) {
    this.totalValue = totalValue;
  }

  public String getTaxValue() {
    return taxValue;
  }

  public void setTaxValue(String taxValue) {
    this.taxValue = taxValue;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public String getTaxName() {
    return taxName;
  }

  public void setTaxName(String taxName) {
    this.taxName = taxName;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(totalValue);
    dest.writeString(taxValue);
    dest.writeString(taxId);
    dest.writeString(taxName);
  }
}
