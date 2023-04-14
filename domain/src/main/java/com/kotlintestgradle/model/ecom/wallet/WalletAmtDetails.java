package com.kotlintestgradle.model.ecom.wallet;

public class WalletAmtDetails {
  private String walletid;
  private String balance;
  private String createddate;
  private String usertype;
  private String statustext;
  private String currency;
  private String userid;
  private String status;

  public WalletAmtDetails(String walletid, String balance, String createddate,
      String usertype, String statustext, String currency, String userid, String status) {
    this.walletid = walletid;
    this.balance = balance;
    this.createddate = createddate;
    this.usertype = usertype;
    this.statustext = statustext;
    this.currency = currency;
    this.userid = userid;
    this.status = status;
  }

  public String getWalletid() {
    return walletid;
  }

  public void setWalletid(String walletid) {
    this.walletid = walletid;
  }

  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public String getCreateddate() {
    return createddate;
  }

  public void setCreateddate(String createddate) {
    this.createddate = createddate;
  }

  public String getUsertype() {
    return usertype;
  }

  public void setUsertype(String usertype) {
    this.usertype = usertype;
  }

  public String getStatustext() {
    return statustext;
  }

  public void setStatustext(String statustext) {
    this.statustext = statustext;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
