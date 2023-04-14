package com.kotlintestgradle.model.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class CartAccoutingData implements Parcelable {
  private String unitPrice;
  private double offerDiscount;
  private String taxableAmount;
  private String deliveryFee;
  private String finalUnitPrice;
  private String addOnsAmount;
  private ArrayList<CartTaxData> tax;
  private String subTotal;
  private String taxAmount;
  private String promoDiscount;
  private String finalTotal;
  private int totalQuantity;
  private String tipAmount;

  public CartAccoutingData(String unitPrice, double offerDiscount, String taxableAmount,
      String deliveryFee, String finalUnitPrice, String addOnsAmount,
      ArrayList<CartTaxData> tax, String subTotal, String taxAmount, String promoDiscount,
      String finalTotal,int totalQuantity) {
    this.unitPrice = unitPrice;
    this.offerDiscount = offerDiscount;
    this.taxableAmount = taxableAmount;
    this.deliveryFee = deliveryFee;
    this.finalUnitPrice = finalUnitPrice;
    this.addOnsAmount = addOnsAmount;
    this.tax = tax;
    this.subTotal = subTotal;
    this.taxAmount = taxAmount;
    this.promoDiscount = promoDiscount;
    this.finalTotal = finalTotal;
    this.totalQuantity = totalQuantity;
  }

  protected CartAccoutingData(Parcel in) {
    unitPrice = in.readString();
    offerDiscount = in.readDouble();
    taxableAmount = in.readString();
    deliveryFee = in.readString();
    finalUnitPrice = in.readString();
    addOnsAmount = in.readString();
    tax = in.createTypedArrayList(CartTaxData.CREATOR);
    subTotal = in.readString();
    taxAmount = in.readString();
    promoDiscount = in.readString();
    finalTotal = in.readString();
    totalQuantity = in.readInt();
    tipAmount = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unitPrice);
    dest.writeDouble(offerDiscount);
    dest.writeString(taxableAmount);
    dest.writeString(deliveryFee);
    dest.writeString(finalUnitPrice);
    dest.writeString(addOnsAmount);
    dest.writeTypedList(tax);
    dest.writeString(subTotal);
    dest.writeString(taxAmount);
    dest.writeString(promoDiscount);
    dest.writeString(finalTotal);
    dest.writeInt(totalQuantity);
    dest.writeString(tipAmount);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartAccoutingData> CREATOR = new Creator<CartAccoutingData>() {
    @Override
    public CartAccoutingData createFromParcel(Parcel in) {
      return new CartAccoutingData(in);
    }

    @Override
    public CartAccoutingData[] newArray(int size) {
      return new CartAccoutingData[size];
    }
  };

  public int getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(int totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public String getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(String unitPrice) {
    this.unitPrice = unitPrice;
  }

  public double getOfferDiscount() {
    return offerDiscount;
  }

  public void setOfferDiscount(double offerDiscount) {
    this.offerDiscount = offerDiscount;
  }

  public String getTaxableAmount() {
    return taxableAmount;
  }

  public void setTaxableAmount(String taxableAmount) {
    this.taxableAmount = taxableAmount;
  }

  public String getDeliveryFee() {
    return deliveryFee;
  }

  public void setDeliveryFee(String deliveryFee) {
    this.deliveryFee = deliveryFee;
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

  public String getPromoDiscount() {
    return promoDiscount;
  }

  public void setPromoDiscount(String promoDiscount) {
    this.promoDiscount = promoDiscount;
  }

  public String getFinalTotal() {
    return finalTotal;
  }

  public void setFinalTotal(String finalTotal) {
    this.finalTotal = finalTotal;
  }

  public String getTipAmount() {
    return tipAmount;
  }

  public void setTipAmount(String tipAmount) {
    this.tipAmount = tipAmount;
  }
}
