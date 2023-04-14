package com.kotlintestgradle.model.ecom.wallet;

import java.util.ArrayList;

public class WalletData {
  private ArrayList<WalletEarningData> walletEarningData;
  private ArrayList<WalletAmtDetails> walletData;

  public WalletData(
      ArrayList<WalletEarningData> walletEarningData,
      ArrayList<WalletAmtDetails> walletData) {
    this.walletEarningData = walletEarningData;
    this.walletData = walletData;
  }

  public ArrayList<WalletEarningData> getWalletEarningData() {
    return walletEarningData;
  }

  public void setWalletEarningData(ArrayList<WalletEarningData> walletEarningData) {
    this.walletEarningData = walletEarningData;
  }

  public ArrayList<WalletAmtDetails> getWalletData() {
    return walletData;
  }

  public void setWalletData(ArrayList<WalletAmtDetails> walletData) {
    this.walletData = walletData;
  }
}
