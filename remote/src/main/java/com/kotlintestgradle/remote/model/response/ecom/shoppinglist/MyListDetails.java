package com.kotlintestgradle.remote.model.response.ecom.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class MyListDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("data")
  private ArrayList<MyListItemDetails> data;

  public MyListDetails(
      ArrayList<MyListItemDetails> data) {
    this.data = data;
  }

  protected MyListDetails(Parcel in) {
  }

  public static final Creator<MyListDetails> CREATOR = new Creator<MyListDetails>() {
    @Override
    public MyListDetails createFromParcel(Parcel in) {
      return new MyListDetails(in);
    }

    @Override
    public MyListDetails[] newArray(int size) {
      return new MyListDetails[size];
    }
  };

  public ArrayList<MyListItemDetails> getData() {
    return data;
  }

  public void setData(
      ArrayList<MyListItemDetails> data) {
    this.data = data;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
