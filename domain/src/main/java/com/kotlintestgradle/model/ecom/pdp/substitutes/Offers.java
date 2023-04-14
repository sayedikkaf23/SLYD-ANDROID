package com.kotlintestgradle.model.ecom.pdp.substitutes;

import android.os.Parcel;
import android.os.Parcelable;

public class Offers implements Parcelable {

  public Offers() {
  }

  protected Offers(Parcel in) {
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Offers> CREATOR = new Creator<Offers>() {
    @Override
    public Offers createFromParcel(Parcel in) {
      return new Offers(in);
    }

    @Override
    public Offers[] newArray(int size) {
      return new Offers[size];
    }
  };
}
