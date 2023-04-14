package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class ProductDataDetails implements ValidItem, Parcelable {

  @SerializedName("data")
  @Expose
  private ArrayList<ProductModelDetails> data;

  @SerializedName("message")
  @Expose
  private String message;

  public ProductDataDetails(ArrayList<ProductModelDetails> data, String message) {
    this.data = data;
    this.message = message;
  }

  protected ProductDataDetails(Parcel in) {
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductDataDetails> CREATOR = new Creator<ProductDataDetails>() {
    @Override
    public ProductDataDetails createFromParcel(Parcel in) {
      return new ProductDataDetails(in);
    }

    @Override
    public ProductDataDetails[] newArray(int size) {
      return new ProductDataDetails[size];
    }
  };

  public ArrayList<ProductModelDetails> getData() {
    return data;
  }

  public void setData(ArrayList<ProductModelDetails> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
