package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CartTaxDetails implements Parcelable, ValidItem {
  @Expose
  @SerializedName("totalValue")
  private String totalValue;

  @Expose
  @SerializedName("taxValue")
  private String taxValue;

  @Expose
  @SerializedName("taxId")
  private String taxId;

  @Expose
  @SerializedName("taxName")
  private String taxName;

  public CartTaxDetails(String totalValue, String taxValue, String taxId, String taxName) {
    this.totalValue = totalValue;
    this.taxValue = taxValue;
    this.taxId = taxId;
    this.taxName = taxName;
  }

  protected CartTaxDetails(Parcel in) {
    totalValue = in.readString();
    taxValue = in.readString();
    taxId = in.readString();
    taxName = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(totalValue);
    dest.writeString(taxValue);
    dest.writeString(taxId);
    dest.writeString(taxName);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartTaxDetails> CREATOR = new Creator<CartTaxDetails>() {
    @Override
    public CartTaxDetails createFromParcel(Parcel in) {
      return new CartTaxDetails(in);
    }

    @Override
    public CartTaxDetails[] newArray(int size) {
      return new CartTaxDetails[size];
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
  public boolean isValid() {
    return true;
  }
}
