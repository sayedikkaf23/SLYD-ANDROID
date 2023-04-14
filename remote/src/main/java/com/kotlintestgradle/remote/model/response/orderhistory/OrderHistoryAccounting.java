package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartTaxDetails;

import java.util.ArrayList;

public class OrderHistoryAccounting implements Parcelable, ValidItem {

  @Expose
  @SerializedName("unitPrice")
  private String unitPrice;
  @Expose
  @SerializedName("taxableAmount")
  private String taxableAmount;
  @Expose
  @SerializedName("finalUnitPrice")
  private String finalUnitPrice;
  @Expose
  @SerializedName("appEarningWithTax")
  private String appEarningWithTax;
  @Expose
  @SerializedName("payBy")
  private PayByDetails payBy;
  @Expose
  @SerializedName("currencySymbol")
  private String currencySymbol;
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
  @SerializedName("storeEarning")
  private String storeEarning;
  @Expose
  @SerializedName("offerDiscount")
  private String offerDiscount;
  @Expose
  @SerializedName("deliveryFee")
  private String deliveryFee;
  @Expose
  @SerializedName("taxAmount")
  private String taxAmount;
  @Expose
  @SerializedName("promoDiscount")
  private String promoDiscount;
  @Expose
  @SerializedName("currencyCode")
  private String currencyCode;
  @Expose
  @SerializedName("finalTotal")
  private String finalTotal;
  @Expose
  @SerializedName("appEarning")
  private String appEarning;

  public OrderHistoryAccounting(String unitPrice, String taxableAmount,
                                String finalUnitPrice, String appEarningWithTax,
                                PayByDetails payBy, String currencySymbol, String addOnsAmount,
                                ArrayList<CartTaxDetails> tax, String subTotal, String storeEarning,
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

  protected OrderHistoryAccounting(Parcel in) {
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
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistoryAccounting> CREATOR =
      new Creator<OrderHistoryAccounting>() {
        @Override
        public OrderHistoryAccounting createFromParcel(Parcel in) {
          return new OrderHistoryAccounting(in);
        }

        @Override
        public OrderHistoryAccounting[] newArray(int size) {
          return new OrderHistoryAccounting[size];
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

  public PayByDetails getPayBy() {
    return payBy;
  }

  public void setPayBy(PayByDetails payBy) {
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
  public boolean isValid() {
    return true;
  }
}
