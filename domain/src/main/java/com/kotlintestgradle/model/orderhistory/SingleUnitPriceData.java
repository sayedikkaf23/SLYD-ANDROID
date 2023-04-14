package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;


import com.kotlintestgradle.model.ecom.getcart.CartTaxData;

import java.util.ArrayList;

public class SingleUnitPriceData implements Parcelable {

  private String unitPrice;

  private String offerDiscount;

  private String taxableAmount;

  private String finalUnitPrice;

  private String addOnsAmount;

  private ArrayList<CartTaxData> tax;

  private String subTotal;

  private String taxAmount;

  public SingleUnitPriceData(String unitPrice, String offerDiscount, String taxableAmount,
      String finalUnitPrice, String addOnsAmount,
      ArrayList<CartTaxData> tax, String subTotal, String taxAmount) {
    this.unitPrice = unitPrice;
    this.offerDiscount = offerDiscount;
    this.taxableAmount = taxableAmount;
    this.finalUnitPrice = finalUnitPrice;
    this.addOnsAmount = addOnsAmount;
    this.tax = tax;
    this.subTotal = subTotal;
    this.taxAmount = taxAmount;
  }

  protected SingleUnitPriceData(Parcel in) {
    unitPrice = in.readString();
    offerDiscount = in.readString();
    taxableAmount = in.readString();
    finalUnitPrice = in.readString();
    addOnsAmount = in.readString();
    tax = in.createTypedArrayList(CartTaxData.CREATOR);
    subTotal = in.readString();
    taxAmount = in.readString();
  }

  public static final Creator<SingleUnitPriceData> CREATOR = new Creator<SingleUnitPriceData>() {
    @Override
    public SingleUnitPriceData createFromParcel(Parcel in) {
      return new SingleUnitPriceData(in);
    }

    @Override
    public SingleUnitPriceData[] newArray(int size) {
      return new SingleUnitPriceData[size];
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

  public ArrayList<CartTaxData> getTax() {
    return tax;
  }

  public void setTax(ArrayList<CartTaxData> tax) {
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
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unitPrice);
    dest.writeString(offerDiscount);
    dest.writeString(taxableAmount);
    dest.writeString(finalUnitPrice);
    dest.writeString(addOnsAmount);
    dest.writeTypedList(tax);
    dest.writeString(subTotal);
    dest.writeString(taxAmount);
  }
}
