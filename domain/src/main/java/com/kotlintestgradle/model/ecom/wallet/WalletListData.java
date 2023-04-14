package com.kotlintestgradle.model.ecom.wallet;

public class WalletListData {
  private WalletData data;
  private String message;

  public WalletListData(WalletData data, String message) {
    this.data = data;
    this.message = message;
  }

  public WalletData getData() {
    return data;
  }

  public void setData(WalletData data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
