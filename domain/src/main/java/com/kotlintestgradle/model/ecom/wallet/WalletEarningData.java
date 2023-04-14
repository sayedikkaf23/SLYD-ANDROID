package com.kotlintestgradle.model.ecom.wallet;

public class WalletEarningData {
  private String balance;
  private String createddate;
  private String walletearningid;
  private String usertype;
  private String statustext;
  private String userid;
  private String status;

  public WalletEarningData(String balance, String createddate, String walletearningid,
      String usertype, String statustext, String userid, String status) {
    this.balance = balance;
    this.createddate = createddate;
    this.walletearningid = walletearningid;
    this.usertype = usertype;
    this.statustext = statustext;
    this.userid = userid;
    this.status = status;
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

  public String getWalletearningid() {
    return walletearningid;
  }

  public void setWalletearningid(String walletearningid) {
    this.walletearningid = walletearningid;
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
