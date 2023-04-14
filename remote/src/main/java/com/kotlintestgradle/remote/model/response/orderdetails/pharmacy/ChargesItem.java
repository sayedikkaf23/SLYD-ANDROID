package com.kotlintestgradle.remote.model.response.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ChargesItem implements Parcelable, ValidItem {

  @Expose
  @SerializedName("amount")
  private double amount;

  @Expose
  @SerializedName("chargeId")
  private String chargeId;

  protected ChargesItem(Parcel in) {
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

  public static final Creator<ChargesItem> CREATOR = new Creator<ChargesItem>() {
    @Override
    public ChargesItem createFromParcel(Parcel in) {
      return new ChargesItem(in);
    }

    @Override
    public ChargesItem[] newArray(int size) {
      return new ChargesItem[size];
    }
  };

  public double getAmount() {
    return amount;
  }

  public String getChargeId() {
    return chargeId;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setChargeId(String chargeId) {
    this.chargeId = chargeId;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}