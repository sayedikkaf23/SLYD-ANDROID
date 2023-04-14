package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartTaxDetails;

import java.util.ArrayList;

public class SingleUnitPriceDetails  implements Parcelable, ValidItem {

  @Expose
  @SerializedName("unitPrice")
  private String unitPrice;
  @Expose
  @SerializedName("offerDiscount")
  private String offerDiscount;
  @Expose
  @SerializedName("taxableAmount")
  private String taxableAmount;
  @Expose
  @SerializedName("finalUnitPrice")
  private String finalUnitPrice;
  @Expose
  @SerializedName("addOnsAmount")
  private String addOnsAmount;
  @Expose
  @SerializedName("tax")
  private ArrayList<CartTaxDetails> tax;
  @Expose
  @SerializedName("subTotal")
  private String subTotal;
  @Expose
  @SerializedName("taxAmount")
  private String taxAmount;

  public SingleUnitPriceDetails(String unitPrice, String offerDiscount,
                                String taxableAmount, String finalUnitPrice, String addOnsAmount,
                                ArrayList<CartTaxDetails> tax, String subTotal, String taxAmount) {
    this.unitPrice = unitPrice;
    this.offerDiscount = offerDiscount;
    this.taxableAmount = taxableAmount;
    this.finalUnitPrice = finalUnitPrice;
    this.addOnsAmount = addOnsAmount;
    this.tax = tax;
    this.subTotal = subTotal;
    this.taxAmount = taxAmount;
  }

  protected SingleUnitPriceDetails(Parcel in) {
    unitPrice = in.readString();
    offerDiscount = in.readString();
    taxableAmount = in.readString();
    finalUnitPrice = in.readString();
    addOnsAmount = in.readString();
    subTotal = in.readString();
    taxAmount = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unitPrice);
    dest.writeString(offerDiscount);
    dest.writeString(taxableAmount);
    dest.writeString(finalUnitPrice);
    dest.writeString(addOnsAmount);
    dest.writeString(subTotal);
    dest.writeString(taxAmount);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SingleUnitPriceDetails> CREATOR =
      new Creator<SingleUnitPriceDetails>() {
        @Override
        public SingleUnitPriceDetails createFromParcel(Parcel in) {
          return new SingleUnitPriceDetails(in);
        }

        @Override
        public SingleUnitPriceDetails[] newArray(int size) {
          return new SingleUnitPriceDetails[size];
        }
      };

  public String getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(String unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getOfferDiscount() {
    return offerDiscount;
  }

  public void setOfferDiscount(String offerDiscount) {
    this.offerDiscount = offerDiscount;
  }

  public String getTaxableAmount() {
    return taxableAmount;
  }

  public void setTaxableAmount(String taxableAmount) {
    this.taxableAmount = taxableAmount;
  }

  public String getFinalUnitPrice() {
    return finalUnitPrice;
  }

  public void setFinalUnitPrice(String finalUnitPrice) {
    this.finalUnitPrice = finalUnitPrice;
  }

  public String getAddOnsAmount() {
    return addOnsAmount;
  }

  public void setAddOnsAmount(String addOnsAmount) {
    this.addOnsAmount = addOnsAmount;
  }

  public ArrayList<CartTaxDetails> getTax() {
    return tax;
  }

  public void setTax(
      ArrayList<CartTaxDetails> tax) {
    this.tax = tax;
  }

  public String getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(String subTotal) {
    this.subTotal = subTotal;
  }

  public String getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(String taxAmount) {
    this.taxAmount = taxAmount;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
