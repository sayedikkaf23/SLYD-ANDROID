package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class PayByDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("wallet")
  private String wallet;
  @Expose
  @SerializedName("cash")
  private String cash;
  @Expose
  @SerializedName("card")
  private String card;

  public PayByDetails(String wallet, String cash, String card) {
    this.wallet = wallet;
    this.cash = cash;
    this.card = card;
  }

  protected PayByDetails(Parcel in) {
    wallet = in.readString();
    cash = in.readString();
    card = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(wallet);
    dest.writeString(cash);
    dest.writeString(card);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PayByDetails> CREATOR = new Creator<PayByDetails>() {
    @Override
    public PayByDetails createFromParcel(Parcel in) {
      return new PayByDetails(in);
    }

    @Override
    public PayByDetails[] newArray(int size) {
      return new PayByDetails[size];
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
  public boolean isValid() {
    return true;
  }
}
