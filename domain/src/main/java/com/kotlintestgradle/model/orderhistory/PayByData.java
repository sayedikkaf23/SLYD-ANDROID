package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class PayByData implements Parcelable {
  private String wallet;

  private String cash;

  private String card;

  public PayByData(String wallet, String cash, String card) {
    this.wallet = wallet;
    this.cash = cash;
    this.card = card;
  }

  protected PayByData(Parcel in) {
    wallet = in.readString();
    cash = in.readString();
    card = in.readString();
  }

  public static final Creator<PayByData> CREATOR = new Creator<PayByData>() {
    @Override
    public PayByData createFromParcel(Parcel in) {
      return new PayByData(in);
    }

    @Override
    public PayByData[] newArray(int size) {
      return new PayByData[size];
    }
  };

  public String getWallet() {
    return wallet;
  }

  public void setWallet(String wallet) {
    this.wallet = wallet;
  }

  public String getCash() {
    return cash;
  }

  public void setCash(String cash) {
    this.cash = cash;
  }

  public String getCard() {
    return card;
  }

  public void setCard(String card) {
    this.card = card;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(wallet);
    dest.writeString(cash);
    dest.writeString(card);
  }
}
