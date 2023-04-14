package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class ChargesItems implements Parcelable {

  private double amount;

  private String chargeId;

  public ChargesItems(double amount, String chargeId) {
    this.amount = amount;
    this.chargeId = chargeId;
  }

  protected ChargesItems(Parcel in) {
    amount = in.readDouble();
    chargeId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(amount);
    dest.writeString(chargeId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ChargesItems> CREATOR = new Creator<ChargesItems>() {
    @Override
    public ChargesItems createFromParcel(Parcel in) {
      return new ChargesItems(in);
    }

    @Override
    public ChargesItems[] newArray(int size) {
      return new ChargesItems[size];
    }
  };

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getChargeId() {
    return chargeId;
  }

  public void setChargeId(String chargeId) {
    this.chargeId = chargeId;
  }
}
