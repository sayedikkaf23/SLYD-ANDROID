package com.kotlintestgradle.remote.model.response.ecom.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class WalletDataDetails implements Parcelable , ValidItem {
  @SerializedName("walletEarningData")
  @Expose
  private ArrayList<WalletEarningDetails> walletEarningData;
  @SerializedName("walletData")
  @Expose
  private ArrayList<WalletDetails> walletData;

  public WalletDataDetails(
      ArrayList<WalletEarningDetails> walletEarningData,
      ArrayList<WalletDetails> walletData) {
    this.walletEarningData = walletEarningData;
    this.walletData = walletData;
  }

  protected WalletDataDetails(Parcel in) {
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WalletDataDetails> CREATOR = new Creator<WalletDataDetails>() {
    @Override
    public WalletDataDetails createFromParcel(Parcel in) {
      return new WalletDataDetails(in);
    }

    @Override
    public WalletDataDetails[] newArray(int size) {
      return new WalletDataDetails[size];
    }
  };

  public ArrayList<WalletEarningDetails> getWalletEarningData ()
  {
    return walletEarningData;
  }

  public void setWalletEarningData (ArrayList<WalletEarningDetails> walletEarningData)
  {
    this.walletEarningData = walletEarningData;
  }

  public ArrayList<WalletDetails> getWalletData ()
  {
    return walletData;
  }

  public void setWalletData (ArrayList<WalletDetails> walletData)
  {
    this.walletData = walletData;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
