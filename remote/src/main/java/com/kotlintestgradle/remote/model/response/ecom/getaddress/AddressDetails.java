package com.kotlintestgradle.remote.model.response.ecom.getaddress;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

import java.util.ArrayList;

public class AddressDetails implements Parcelable , ValidItem {

  @SerializedName("data")
  @Expose
  private ArrayList<AddressListItemDetails> data;

  @SerializedName("message")
  @Expose
  private String message;

  protected AddressDetails(Parcel in) {
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

  public static final Creator<AddressDetails> CREATOR = new Creator<AddressDetails>() {
    @Override
    public AddressDetails createFromParcel(Parcel in) {
      return new AddressDetails(in);
    }

    @Override
    public AddressDetails[] newArray(int size) {
      return new AddressDetails[size];
    }
  };

  public ArrayList<AddressListItemDetails> getData() {
    return data;
  }

  public void setData(ArrayList<AddressListItemDetails> data) {
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
