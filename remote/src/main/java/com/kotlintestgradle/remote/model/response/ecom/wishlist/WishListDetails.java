package com.kotlintestgradle.remote.model.response.ecom.wishlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.ProductsDetails;
import java.util.ArrayList;

public class WishListDetails implements ValidItem, Parcelable {
  @SerializedName("penCount")
  @Expose
  private int penCount;

  @SerializedName("data")
  @Expose
  private ArrayList<ProductsDetails> data;

  @SerializedName("message")
  @Expose
  private String message;

  public WishListDetails(int penCount,
      ArrayList<ProductsDetails> data, String message) {
    this.penCount = penCount;
    this.data = data;
    this.message = message;
  }

  protected WishListDetails(Parcel in) {
    penCount = in.readInt();
    data = in.createTypedArrayList(ProductsDetails.CREATOR);
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(penCount);
    dest.writeTypedList(data);
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WishListDetails> CREATOR = new Creator<WishListDetails>() {
    @Override
    public WishListDetails createFromParcel(Parcel in) {
      return new WishListDetails(in);
    }

    @Override
    public WishListDetails[] newArray(int size) {
      return new WishListDetails[size];
    }
  };

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public ArrayList<ProductsDetails> getData() {
    return data;
  }

  public void setData(
      ArrayList<ProductsDetails> data) {
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
