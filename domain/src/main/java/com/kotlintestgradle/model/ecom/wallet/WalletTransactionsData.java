package com.kotlintestgradle.model.ecom.wallet;

import java.util.ArrayList;

public class WalletTransactionsData {
  private ArrayList<WalletTransactionItemData> data;
  private String message;
  private String totalCount;
  private String pageState;

  public WalletTransactionsData(
      ArrayList<WalletTransactionItemData> data, String message, String totalCount,
      String pageState) {
    this.data = data;
    this.message = message;
    this.totalCount = totalCount;
    this.pageState = pageState;
  }

  public ArrayList<WalletTransactionItemData> getData() {
    return data;
  }

  public void setData(ArrayList<WalletTransactionItemData> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }

  public String getPageState() {
    return pageState;
  }

  public void setPageState(String pageState) {
    this.pageState = pageState;
  }
}
