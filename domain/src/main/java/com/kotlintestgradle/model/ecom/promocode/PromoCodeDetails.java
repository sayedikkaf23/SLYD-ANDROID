package com.kotlintestgradle.model.ecom.promocode;

import android.os.Parcel;
import java.util.ArrayList;

public class PromoCodeDetails {
  private ArrayList<PromoCodeData> data;
  private String message;

  public PromoCodeDetails(ArrayList<PromoCodeData> data, String message) {
    this.data = data;
    this.message = message;
  }

  protected PromoCodeDetails(Parcel in) {
    message = in.readString();
  }

  public ArrayList<PromoCodeData> getData() {
    return data;
  }

  public void setData(ArrayList<PromoCodeData> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
