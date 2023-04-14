package com.kotlintestgradle.remote.model.response.ecom.shoppinglistnames;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class MyListNamesDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("data")
  private ArrayList<MyListNameItemDetails> data;

  @Expose
  @SerializedName("message")
  private String message;

  public MyListNamesDetails(
      ArrayList<MyListNameItemDetails> data, String message) {
    this.data = data;
    this.message = message;
  }

  protected MyListNamesDetails(Parcel in) {
    data = in.createTypedArrayList(MyListNameItemDetails.CREATOR);
    message = in.readString();
  }

  public static final Creator<MyListNamesDetails> CREATOR = new Creator<MyListNamesDetails>() {
    @Override
    public MyListNamesDetails createFromParcel(Parcel in) {
      return new MyListNamesDetails(in);
    }

    @Override
    public MyListNamesDetails[] newArray(int size) {
      return new MyListNamesDetails[size];
    }
  };

  public ArrayList<MyListNameItemDetails> getData() {
    return data;
  }

  public void setData(
      ArrayList<MyListNameItemDetails> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeTypedList(data);
    parcel.writeString(message);
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
