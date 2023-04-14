package com.kotlintestgradle.model.ecom.getcart;

import java.util.ArrayList;

public class CartData {
  private String notes;
  private String cartTotalTax;
  private String storeTypeId;
  private String subTotal;
  private CartLogsData cartLogs;
  private String cartType;
  private String seqId;
  private ArrayList<CartTaxData> taxData;
  private String storeType;
  private String grandTotal;
  private String currencySymbol;
  private String storeCategory;
  private ArrayList<CartTaxData> taxList;
  private String deliveryFee;
  private String userTypeMsg;
  private String storeCategoryId;
  private String subtotal;
  private String totalCartquantity;
  private String cartTypeInTxt;
  private String totalDiscount;
  private String mId;
  private String userType;
  private String totalAppliedTaxOnCart;
  private CartUserData user;
  private String currencyCode;
  private CartAccoutingData accounting;
  private ArrayList<CartSellerData> sellers;
  private int action;
  private int newQuantity;
  private String productId;
  private boolean isLoggedIn;

  public CartData() {
  }

  public CartData(int action, String productId, int newQuantity) {
    this.action = action;
    this.productId = productId;
    this.newQuantity = newQuantity;
  }

  public CartData(String notes, String cartTotalTax, String storeTypeId, String subTotal,
      CartLogsData cartLogs, String cartType, String seqId,
      ArrayList<CartTaxData> taxData, String storeType, String grandTotal,
      String currencySymbol, String storeCategory,
      ArrayList<CartTaxData> taxList, String deliveryFee, String userTypeMsg,
      String storeCategoryId, String subtotal, String totalCartquantity,
      String cartTypeInTxt, String totalDiscount, String id, String userType,
      String totalAppliedTaxOnCart, CartUserData user, String currencyCode,
      ArrayList<CartSellerData> sellers, CartAccoutingData accounting) {
    this.notes = notes;
    this.cartTotalTax = cartTotalTax;
    this.storeTypeId = storeTypeId;
    this.subTotal = subTotal;
    this.cartLogs = cartLogs;
    this.cartType = cartType;
    this.seqId = seqId;
    this.taxData = taxData;
    this.storeType = storeType;
    this.grandTotal = grandTotal;
    this.currencySymbol = currencySymbol;
    this.storeCategory = storeCategory;
    this.taxList = taxList;
    this.deliveryFee = deliveryFee;
    this.userTypeMsg = userTypeMsg;
    this.storeCategoryId = storeCategoryId;
    this.subtotal = subtotal;
    this.totalCartquantity = totalCartquantity;
    this.cartTypeInTxt = cartTypeInTxt;
    this.totalDiscount = totalDiscount;
    this.mId = id;
    this.userType = userType;
    this.totalAppliedTaxOnCart = totalAppliedTaxOnCart;
    this.user = user;
    this.currencyCode = currencyCode;
    this.sellers = sellers;
    this.accounting = accounting;
  }

  public CartAccoutingData getAccounting() {
    return accounting;
  }

  public void setAccounting(CartAccoutingData accounting) {
    this.accounting = accounting;
  }

  public int getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public int getNewQuantity() {
    return newQuantity;
  }

  public void setNewQuantity(int newQuantity) {
    this.newQuantity = newQuantity;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getCartTotalTax() {
    return cartTotalTax;
  }

  public void setCartTotalTax(String cartTotalTax) {
    this.cartTotalTax = cartTotalTax;
  }

  public String getStoreTypeId() {
    return storeTypeId;
  }

  public void setStoreTypeId(String storeTypeId) {
    this.storeTypeId = storeTypeId;
  }

  public String getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(String subTotal) {
    this.subTotal = subTotal;
  }

  public CartLogsData getCartLogs() {
    return cartLogs;
  }

  public void setCartLogs(CartLogsData cartLogs) {
    this.cartLogs = cartLogs;
  }

  public String getCartType() {
    return cartType;
  }

  public void setCartType(String cartType) {
    this.cartType = cartType;
  }

  public String getSeqId() {
    return seqId;
  }

  public void setSeqId(String seqId) {
    this.seqId = seqId;
  }

  public ArrayList<CartTaxData> getTaxData() {
    return taxData;
  }

  public void setTaxData(ArrayList<CartTaxData> taxData) {
    this.taxData = taxData;
  }

  public String getStoreType() {
    return storeType;
  }

  public void setStoreType(String storeType) {
    this.storeType = storeType;
  }

  public String getGrandTotal() {
    return grandTotal;
  }

  public void setGrandTotal(String grandTotal) {
    this.grandTotal = grandTotal;
  }

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getStoreCategory() {
    return storeCategory;
  }

  public void setStoreCategory(String storeCategory) {
    this.storeCategory = storeCategory;
  }

  public ArrayList<CartTaxData> getTaxList() {
    return taxList;
  }

  public void setTaxList(ArrayList<CartTaxData> taxList) {
    this.taxList = taxList;
  }

  public String getDeliveryFee() {
    return deliveryFee;
  }

  public void setDeliveryFee(String deliveryFee) {
    this.deliveryFee = deliveryFee;
  }

  public String getUserTypeMsg() {
    return userTypeMsg;
  }

  public void setUserTypeMsg(String userTypeMsg) {
    this.userTypeMsg = userTypeMsg;
  }

  public String getStoreCategoryId() {
    return storeCategoryId;
  }

  public void setStoreCategoryId(String storeCategoryId) {
    this.storeCategoryId = storeCategoryId;
  }

  public String getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(String subtotal) {
    this.subtotal = subtotal;
  }

  public String getTotalCartquantity() {
    return totalCartquantity;
  }

  public void setTotalCartquantity(String totalCartquantity) {
    this.totalCartquantity = totalCartquantity;
  }

  public String getCartTypeInTxt() {
    return cartTypeInTxt;
  }

  public void setCartTypeInTxt(String cartTypeInTxt) {
    this.cartTypeInTxt = cartTypeInTxt;
  }

  public String getTotalDiscount() {
    return totalDiscount;
  }

  public void setTotalDiscount(String totalDiscount) {
    this.totalDiscount = totalDiscount;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    this.mId = id;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getTotalAppliedTaxOnCart() {
    return totalAppliedTaxOnCart;
  }

  public void setTotalAppliedTaxOnCart(String totalAppliedTaxOnCart) {
    this.totalAppliedTaxOnCart = totalAppliedTaxOnCart;
  }

  public CartUserData getUser() {
    return user;
  }

  public void setUser(CartUserData user) {
    this.user = user;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public ArrayList<CartSellerData> getSellers() {
    return sellers;
  }

  public void setSellers(ArrayList<CartSellerData> sellers) {
    this.sellers = sellers;
  }

  public boolean isLoggedIn() {
    return isLoggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    isLoggedIn = loggedIn;
  }
}
