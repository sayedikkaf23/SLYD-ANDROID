package com.kotlintestgradle.model.ecom.wallet;

import android.os.Parcel;
import android.os.Parcelable;

public class WalletTransactionItemData implements Parcelable {
  public static final Creator<WalletTransactionItemData> CREATOR =
      new Creator<WalletTransactionItemData>() {
        @Override
        public WalletTransactionItemData createFromParcel(Parcel in) {
          return new WalletTransactionItemData(in);
        }

        @Override
        public WalletTransactionItemData[] newArray(int size) {
          return new WalletTransactionItemData[size];
        }
      };
  private String walletid;
  private String amount;
  private String notes;
  private String txntypetext;
  private String txntimestamp;
  private String closingbal;
  private String description;
  private String trigger;
  private String openingbal;
  private String txntype;
  private String currency;
  private String initiatedby;
  private String txnid;

  public WalletTransactionItemData(String walletid, String amount, String notes,
      String txntypetext, String txntimestamp, String closingbal, String description,
      String trigger, String openingbal, String txntype, String currency,
      String initiatedby, String txnid) {
    this.walletid = walletid;
    this.amount = amount;
    this.notes = notes;
    this.txntypetext = txntypetext;
    this.txntimestamp = txntimestamp;
    this.closingbal = closingbal;
    this.description = description;
    this.trigger = trigger;
    this.openingbal = openingbal;
    this.txntype = txntype;
    this.currency = currency;
    this.initiatedby = initiatedby;
    this.txnid = txnid;
  }

  protected WalletTransactionItemData(Parcel in) {
    walletid = in.readString();
    amount = in.readString();
    notes = in.readString();
    txntypetext = in.readString();
    txntimestamp = in.readString();
    closingbal = in.readString();
    description = in.readString();
    trigger = in.readString();
    openingbal = in.readString();
    txntype = in.readString();
    currency = in.readString();
    initiatedby = in.readString();
    txnid = in.readString();
  }

  public String getWalletid() {
    return walletid;
  }

  public void setWalletid(String walletid) {
    this.walletid = walletid;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getTxntypetext() {
    return txntypetext;
  }

  public void setTxntypetext(String txntypetext) {
    this.txntypetext = txntypetext;
  }

  public String getTxntimestamp() {
    return txntimestamp;
  }

  public void setTxntimestamp(String txntimestamp) {
    this.txntimestamp = txntimestamp;
  }

  public String getClosingbal() {
    return closingbal;
  }

  public void setClosingbal(String closingbal) {
    this.closingbal = closingbal;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public String getOpeningbal() {
    return openingbal;
  }

  public void setOpeningbal(String openingbal) {
    this.openingbal = openingbal;
  }

  public String getTxntype() {
    return txntype;
  }

  public void setTxntype(String txntype) {
    this.txntype = txntype;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getInitiatedby() {
    return initiatedby;
  }

  public void setInitiatedby(String initiatedby) {
    this.initiatedby = initiatedby;
  }

  public String getTxnid() {
    return txnid;
  }

  public void setTxnid(String txnid) {
    this.txnid = txnid;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(walletid);
    dest.writeString(amount);
    dest.writeString(notes);
    dest.writeString(txntypetext);
    dest.writeString(txntimestamp);
    dest.writeString(closingbal);
    dest.writeString(description);
    dest.writeString(trigger);
    dest.writeString(openingbal);
    dest.writeString(txntype);
    dest.writeString(currency);
    dest.writeString(initiatedby);
    dest.writeString(txnid);
  }
}
