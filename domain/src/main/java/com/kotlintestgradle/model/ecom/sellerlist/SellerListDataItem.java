package com.kotlintestgradle.model.ecom.sellerlist;

import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.pdp.SupplierData;
import java.util.ArrayList;

public class SellerListDataItem {

  private ArrayList<ImageData> images;

  private ProductSeoData productSeo;

  private ExchangePolicyData exchangePolicy;

  private ArrayList<SupplierData> supplier;

  private ReturnPolicyData returnPolicy;

  private String currencySymbol;

  private WarrantyData warranty;

  private String currency;

  private String mTotalStarRating;

  private ReplacementPolicyData replacementPolicy;

  private TermAndConditionData termAndcondition;

  private String productName;

  public SellerListDataItem(ArrayList<ImageData> images,
      ProductSeoData productSeo,
      ExchangePolicyData exchangePolicy,
      ArrayList<SupplierData> supplier,
      ReturnPolicyData returnPolicy, String currencySymbol,
      WarrantyData warranty, String currency, String totalStarRating,
      ReplacementPolicyData replacementPolicy,
      TermAndConditionData termAndcondition, String productName) {
    this.images = images;
    this.productSeo = productSeo;
    this.exchangePolicy = exchangePolicy;
    this.supplier = supplier;
    this.returnPolicy = returnPolicy;
    this.currencySymbol = currencySymbol;
    this.warranty = warranty;
    this.currency = currency;
    mTotalStarRating = totalStarRating;
    this.replacementPolicy = replacementPolicy;
    this.termAndcondition = termAndcondition;
    this.productName = productName;
  }

  public ArrayList<ImageData> getImages() {
    return images;
  }

  public void setImages(ArrayList<ImageData> images) {
    this.images = images;
  }

  public ProductSeoData getProductSeo() {
    return productSeo;
  }

  public void setProductSeo(ProductSeoData productSeo) {
    this.productSeo = productSeo;
  }

  public ExchangePolicyData getExchangePolicy() {
    return exchangePolicy;
  }

  public void setExchangePolicy(ExchangePolicyData exchangePolicy) {
    this.exchangePolicy = exchangePolicy;
  }

  public ArrayList<SupplierData> getSupplier() {
    return supplier;
  }

  public void setSupplier(ArrayList<SupplierData> supplier) {
    this.supplier = supplier;
  }

  public ReturnPolicyData getReturnPolicy() {
    return returnPolicy;
  }

  public void setReturnPolicy(ReturnPolicyData returnPolicy) {
    this.returnPolicy = returnPolicy;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public WarrantyData getWarranty() {
    return warranty;
  }

  public void setWarranty(WarrantyData warranty) {
    this.warranty = warranty;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getTotalStarRating() {
    return mTotalStarRating;
  }

  public void setTotalStarRating(String totalStarRating) {
    mTotalStarRating = totalStarRating;
  }

  public ReplacementPolicyData getReplacementPolicy() {
    return replacementPolicy;
  }

  public void setReplacementPolicy(
      ReplacementPolicyData replacementPolicy) {
    this.replacementPolicy = replacementPolicy;
  }

  public TermAndConditionData getTermAndcondition() {
    return termAndcondition;
  }

  public void setTermAndcondition(
      TermAndConditionData termAndcondition) {
    this.termAndcondition = termAndcondition;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }
}
