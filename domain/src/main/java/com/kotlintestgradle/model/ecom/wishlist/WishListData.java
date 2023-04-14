package com.kotlintestgradle.model.ecom.wishlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import java.util.ArrayList;

public class WishListData implements Parcelable {

  private ArrayList<ProductsData> data;
  private int penCount;
  private String message;

  public WishListData(ArrayList<ProductsData> data, int penCount, String message) {
    this.data = data;
    this.penCount = penCount;
    this.message = message;
  }

  protected WishListData(Parcel in) {
    data = in.createTypedArrayList(ProductsData.CREATOR);
    penCount = in.readInt();
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(data);
    dest.writeInt(penCount);
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WishListData> CREATOR = new Creator<WishListData>() {
    @Override
    public WishListData createFromParcel(Parcel in) {
      return new WishListData(in);
    }

    @Override
    public WishListData[] newArray(int size) {
      return new WishListData[size];
    }
  };

  public ArrayList<ProductsData> getData() {
    return data;
  }

  public void setData(ArrayList<ProductsData> data) {
    this.data = data;
  }

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
