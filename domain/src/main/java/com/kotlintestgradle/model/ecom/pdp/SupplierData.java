package com.kotlintestgradle.model.ecom.pdp;

import com.kotlintestgradle.model.ecom.common.FinalPriceListData;
import com.kotlintestgradle.model.ecom.common.SellerData;
import java.util.ArrayList;

public class SupplierData {
  private String productId;
  private String retailerQty;
  private String retailerPrice;
  private String currencySymbol;
  private String distributorPrice;
  private String currency;
  private String id;
  private String supplierName;
  private String rating;
  private String totalRating;
  private String distributorQty;
  private SellerData supplier;
  private FinalPriceListData finalPriceList;
  private ArrayList<ReviewParameterData> reviewParameter;
  private String sellerSince;

  public SupplierData(String productId, String retailerQty, String retailerPrice,
      String currencySymbol, String distributorPrice, String currency, String id,
      String distributorQty, String sellerSince) {
    this.productId = productId;
    this.retailerQty = retailerQty;
    this.retailerPrice = retailerPrice;
    this.currencySymbol = currencySymbol;
    this.distributorPrice = distributorPrice;
    this.currency = currency;
    this.id = id;
    this.distributorQty = distributorQty;
    this.sellerSince = sellerSince;
  }

  public SupplierData(String productId, String retailerQty, String retailerPrice,
      String currencySymbol, String distributorPrice, String currency, String id,
      String distributorQty, String supplierName, String rating, String totalRating,
      SellerData supplier,
      FinalPriceListData finalPriceList, ArrayList<ReviewParameterData> reviewParameter,
      String sellerSince) {
    this.productId = productId;
    this.retailerQty = retailerQty;
    this.retailerPrice = retailerPrice;
    this.currencySymbol = currencySymbol;
    this.distributorPrice = distributorPrice;
    this.currency = currency;
    this.id = id;
    this.distributorQty = distributorQty;
    this.supplierName = supplierName;
    this.rating = rating;
    this.totalRating = totalRating;
    this.supplier = supplier;
    this.finalPriceList = finalPriceList;
    this.reviewParameter = reviewParameter;
    this.sellerSince = sellerSince;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  public String getSellerSince() {
    return sellerSince;
  }

  public void setSellerSince(String sellerSince) {
    this.sellerSince = sellerSince;
  }

  public SellerData getSupplier() {
    return supplier;
  }

  public void setSupplier(SellerData supplier) {
    this.supplier = supplier;
  }

  public FinalPriceListData getFinalPriceList() {
    return finalPriceList;
  }

  public void setFinalPriceList(FinalPriceListData finalPriceList) {
    this.finalPriceList = finalPriceList;
  }

  public ArrayList<ReviewParameterData> getReviewParameter() {
    return reviewParameter;
  }

  public void setReviewParameter(ArrayList<ReviewParameterData> reviewParameter) {
    this.reviewParameter = reviewParameter;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getTotalRating() {
    return totalRating;
  }

  public void setTotalRating(String totalRating) {
    this.totalRating = totalRating;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getRetailerQty() {
    return retailerQty;
  }

  public void setRetailerQty(String retailerQty) {
    this.retailerQty = retailerQty;
  }

  public String getRetailerPrice() {
    return retailerPrice;
  }

  public void setRetailerPrice(String retailerPrice) {
    this.retailerPrice = retailerPrice;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getDistributorPrice() {
    return distributorPrice;
  }

  public void setDistributorPrice(String distributorPrice) {
    this.distributorPrice = distributorPrice;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDistributorQty() {
    return distributorQty;
  }

  public void setDistributorQty(String distributorQty) {
    this.distributorQty = distributorQty;
  }
}
