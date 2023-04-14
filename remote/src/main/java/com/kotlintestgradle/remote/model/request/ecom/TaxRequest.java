package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaxRequest implements Parcelable {

  @Expose
  @SerializedName("taxId")
  private String taxId;

  @Expose
  @SerializedName("taxName")
  private String taxName;

  @Expose
  @SerializedName("taxValue")
  private String taxValue;

  @Expose
  @SerializedName("totalValue")
  private float totalValue;

  public TaxRequest(String taxId, String taxName, String taxValue, float totalValue) {
    this.taxId = taxId;
    this.taxName = taxName;
    this.taxValue = taxValue;
    this.totalValue = totalValue;
  }

  protected TaxRequest(Parcel in) {
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

  public static final Creator<TaxRequest> CREATOR = new Creator<TaxRequest>() {
    @Override
    public TaxRequest createFromParcel(Parcel in) {
      return new TaxRequest(in);
    }

    @Override
    public TaxRequest[] newArray(int size) {
      return new TaxRequest[size];
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

  public float getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(float totalValue) {
    this.totalValue = totalValue;
  }
}
