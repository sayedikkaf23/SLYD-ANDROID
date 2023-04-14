package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;


import com.kotlintestgradle.model.ecom.getcart.CartTaxData;

import java.util.ArrayList;

public class OrderHistoryAccountingData implements Parcelable {

  private String unitPrice;

  private String taxableAmount;

  private String finalUnitPrice;

  private String appEarningWithTax;

  private PayByData payBy;

  private String currencySymbol;

  private String addOnsAmount;

  private ArrayList<CartTaxData> tax;

  private String subTotal;

  private String storeEarning;

  private String offerDiscount;

  private String deliveryFee;

  private String taxAmount;

  private String promoDiscount;

  private String currencyCode;

  private String finalTotal;

  private String appEarning;

  public OrderHistoryAccountingData(String unitPrice, String taxableAmount,
      String finalUnitPrice, String appEarningWithTax,
      PayByData payBy, String currencySymbol, String addOnsAmount,
      ArrayList<CartTaxData> tax, String subTotal, String storeEarning,
      String offerDiscount, String deliveryFee, String taxAmount, String promoDiscount,
      String currencyCode, String finalTotal, String appEarning) {
    this.unitPrice = unitPrice;
    this.taxableAmount = taxableAmount;
    this.finalUnitPrice = finalUnitPrice;
    this.appEarningWithTax = appEarningWithTax;
    this.payBy = payBy;
    this.currencySymbol = currencySymbol;
    this.addOnsAmount = addOnsAmount;
    this.tax = tax;
    this.subTotal = subTotal;
    this.storeEarning = storeEarning;
    this.offerDiscount = offerDiscount;
    this.deliveryFee = deliveryFee;
    this.taxAmount = taxAmount;
    this.promoDiscount = promoDiscount;
    this.currencyCode = currencyCode;
    this.finalTotal = finalTotal;
    this.appEarning = appEarning;
  }

  protected OrderHistoryAccountingData(Parcel in) {
    unitPrice = in.readString();
    taxableAmount = in.readString();
    finalUnitPrice = in.readString();
    appEarningWithTax = in.readString();
    currencySymbol = in.readString();
    addOnsAmount = in.readString();
    subTotal = in.readString();
    storeEarning = in.readString();
    offerDiscount = in.readString();
    deliveryFee = in.readString();
    taxAmount = in.readString();
    promoDiscount = in.readString();
    currencyCode = in.readString();
    finalTotal = in.readString();
    appEarning = in.readString();
    tax = in.createTypedArrayList(CartTaxData.CREATOR);
  }

  public static final Creator<OrderHistoryAccountingData> CREATOR =
      new Creator<OrderHistoryAccountingData>() {
        @Override
        public OrderHistoryAccountingData createFromParcel(Parcel in) {
          return new OrderHistoryAccountingData(in);
        }

        @Override
        public OrderHistoryAccountingData[] newArray(int size) {
          return new OrderHistoryAccountingData[size];
        }
      };

  public String getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(String unitPrice) {
    this.unitPrice = unitPrice;
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

  public String getAppEarningWithTax() {
    return appEarningWithTax;
  }

  public void setAppEarningWithTax(String appEarningWithTax) {
    this.appEarningWithTax = appEarningWithTax;
  }

  public PayByData getPayBy() {
    return payBy;
  }

  public void setPayBy(PayByData payBy) {
    this.payBy = payBy;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
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

  public String getStoreEarning() {
    return storeEarning;
  }

  public void setStoreEarning(String storeEarning) {
    this.storeEarning = storeEarning;
  }

  public String getOfferDiscount() {
    return offerDiscount;
  }

  public void setOfferDiscount(String offerDiscount) {
    this.offerDiscount = offerDiscount;
  }

  public String getDeliveryFee() {
    return deliveryFee;
  }

  public void setDeliveryFee(String deliveryFee) {
    this.deliveryFee = deliveryFee;
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

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getFinalTotal() {
    return finalTotal;
  }

  public void setFinalTotal(String finalTotal) {
    this.finalTotal = finalTotal;
  }

  public String getAppEarning() {
    return appEarning;
  }

  public void setAppEarning(String appEarning) {
    this.appEarning = appEarning;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(unitPrice);
    dest.writeString(taxableAmount);
    dest.writeString(finalUnitPrice);
    dest.writeString(appEarningWithTax);
    dest.writeString(currencySymbol);
    dest.writeString(addOnsAmount);
    dest.writeString(subTotal);
    dest.writeString(storeEarning);
    dest.writeString(offerDiscount);
    dest.writeString(deliveryFee);
    dest.writeString(taxAmount);
    dest.writeString(promoDiscount);
    dest.writeString(currencyCode);
    dest.writeString(finalTotal);
    dest.writeString(appEarning);
    dest.writeTypedList(tax);

  }
}
