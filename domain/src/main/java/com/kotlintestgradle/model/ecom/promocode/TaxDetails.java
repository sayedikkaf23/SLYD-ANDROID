package com.kotlintestgradle.model.ecom.promocode;

import android.os.Parcel;
import android.os.Parcelable;

public class TaxDetails implements Parcelable {
  private String taxId;
  private String taxName;
  private String taxValue;
  private float totalValue;

  public TaxDetails(String taxId, String taxName, String taxValue, float totalValue) {
    this.taxId = taxId;
    this.taxName = taxName;
    this.taxValue = taxValue;
    this.totalValue = totalValue;
  }

  protected TaxDetails(Parcel in) {
    taxId = in.readString();
    taxName = in.readString();
    taxValue = in.readString();
    totalValue = in.readFloat();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(taxId);
    dest.writeString(taxName);
    dest.writeString(taxValue);
    dest.writeFloat(totalValue);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TaxDetails> CREATOR = new Creator<TaxDetails>() {
    @Override
    public TaxDetails createFromParcel(Parcel in) {
      return new TaxDetails(in);
    }

    @Override
    public TaxDetails[] newArray(int size) {
      return new TaxDetails[size];
    }
  };

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

  public String getTaxValue() {
    return taxValue;
  }

  public void setTaxValue(String taxValue) {
    this.taxValue = taxValue;
  }

  public Float getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(Float totalValue) {
    this.totalValue = totalValue;
  }
}
