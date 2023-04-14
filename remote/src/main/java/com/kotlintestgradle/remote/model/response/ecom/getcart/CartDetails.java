package com.kotlintestgradle.remote.model.response.ecom.getcart;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class CartDetails implements Parcelable, ValidItem {
  @Expose
  @SerializedName("notes")
  private String notes;
  @Expose
  @SerializedName("cartTotalTax")
  private String cartTotalTax;
  @Expose
  @SerializedName("storeTypeId")
  private String storeTypeId;
  @Expose
  @SerializedName("subTotal")
  private String subTotal;
  @Expose
  @SerializedName("cartLogs")
  private CartLogsDetails cartLogs;
  @Expose
  @SerializedName("cartType")
  private String cartType;
  @Expose
  @SerializedName("seqId")
  private String seqId;
  @Expose
  @SerializedName("taxData")
  private ArrayList<CartTaxDetails> taxData;
  @Expose
  @SerializedName("storeType")
  private String storeType;
  @Expose
  @SerializedName("grandTotal")
  private String grandTotal;
  @Expose
  @SerializedName("currencySymbol")
  private String currencySymbol;
  @Expose
  @SerializedName("storeCategory")
  private String storeCategory;
  @Expose
  @SerializedName("taxList")
  private ArrayList<CartTaxDetails> taxList;
  @Expose
  @SerializedName("deliveryFee")
  private String deliveryFee;
  @Expose
  @SerializedName("userTypeMsg")
  private String userTypeMsg;
  @Expose
  @SerializedName("storeCategoryId")
  private String storeCategoryId;
  @Expose
  @SerializedName("subtotal")
  private String subtotal;
  @Expose
  @SerializedName("totalCartquantity")
  private String totalCartquantity;
  @Expose
  @SerializedName("cartTypeInTxt")
  private String cartTypeInTxt;
  @Expose
  @SerializedName("totalDiscount")
  private String totalDiscount;
  @Expose
  @SerializedName("_id")
  private String _id;
  @Expose
  @SerializedName("userType")
  private String userType;
  @Expose
  @SerializedName("totalAppliedTaxOnCart")
  private String totalAppliedTaxOnCart;
  @Expose
  @SerializedName("user")
  private CartUserDetails user;
  @Expose
  @SerializedName("currencyCode")
  private String currencyCode;
  @Expose
  @SerializedName("accounting")
  private CartAccountingDetails accounting;
  @Expose
  @SerializedName("sellers")
  private ArrayList<CartSellerDetails> sellers;

  public CartDetails(String notes, String cartTotalTax, String storeTypeId, String subTotal,
      CartLogsDetails cartLogs, String cartType, String seqId,
      ArrayList<CartTaxDetails> taxData, String storeType, String grandTotal,
      String currencySymbol, String storeCategory,
      ArrayList<CartTaxDetails> taxList, String deliveryFee, String userTypeMsg,
      String storeCategoryId, String subtotal, String totalCartquantity,
      String cartTypeInTxt, String totalDiscount, String _id, String userType,
      String totalAppliedTaxOnCart,
      CartUserDetails user, String currencyCode,
      ArrayList<CartSellerDetails> sellers,CartAccountingDetails accounting) {
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
    this._id = _id;
    this.userType = userType;
    this.totalAppliedTaxOnCart = totalAppliedTaxOnCart;
    this.user = user;
    this.currencyCode = currencyCode;
    this.sellers = sellers;
    this.accounting=accounting;
  }

  protected CartDetails(Parcel in) {
    notes = in.readString();
    cartTotalTax = in.readString();
    storeTypeId = in.readString();
    subTotal = in.readString();
    cartType = in.readString();
    seqId = in.readString();
    storeType = in.readString();
    grandTotal = in.readString();
    currencySymbol = in.readString();
    storeCategory = in.readString();
    deliveryFee = in.readString();
    userTypeMsg = in.readString();
    storeCategoryId = in.readString();
    subtotal = in.readString();
    totalCartquantity = in.readString();
    cartTypeInTxt = in.readString();
    totalDiscount = in.readString();
    _id = in.readString();
    userType = in.readString();
    totalAppliedTaxOnCart = in.readString();
    currencyCode = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(notes);
    dest.writeString(cartTotalTax);
    dest.writeString(storeTypeId);
    dest.writeString(subTotal);
    dest.writeString(cartType);
    dest.writeString(seqId);
    dest.writeString(storeType);
    dest.writeString(grandTotal);
    dest.writeString(currencySymbol);
    dest.writeString(storeCategory);
    dest.writeString(deliveryFee);
    dest.writeString(userTypeMsg);
    dest.writeString(storeCategoryId);
    dest.writeString(subtotal);
    dest.writeString(totalCartquantity);
    dest.writeString(cartTypeInTxt);
    dest.writeString(totalDiscount);
    dest.writeString(_id);
    dest.writeString(userType);
    dest.writeString(totalAppliedTaxOnCart);
    dest.writeString(currencyCode);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CartDetails> CREATOR = new Creator<CartDetails>() {
    @Override
    public CartDetails createFromParcel(Parcel in) {
      return new CartDetails(in);
    }

    @Override
    public CartDetails[] newArray(int size) {
      return new CartDetails[size];
    }
  };

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public CartAccountingDetails getAccounting() {
    return accounting;
  }

  public void setAccounting(
      CartAccountingDetails accounting) {
    this.accounting = accounting;
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

  public CartLogsDetails getCartLogs() {
    return cartLogs;
  }

  public void setCartLogs(CartLogsDetails cartLogs) {
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

  public ArrayList<CartTaxDetails> getTaxData() {
    return taxData;
  }

  public void setTaxData(
      ArrayList<CartTaxDetails> taxData) {
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

  public ArrayList<CartTaxDetails> getTaxList() {
    return taxList;
  }

  public void setTaxList(
      ArrayList<CartTaxDetails> taxList) {
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

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
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

  public CartUserDetails getUser() {
    return user;
  }

  public void setUser(CartUserDetails user) {
    this.user = user;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public ArrayList<CartSellerDetails> getSellers() {
    return sellers;
  }

  public void setSellers(
      ArrayList<CartSellerDetails> sellers) {
    this.sellers = sellers;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
