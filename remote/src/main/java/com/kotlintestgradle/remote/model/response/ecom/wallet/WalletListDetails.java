package com.kotlintestgradle.remote.model.response.ecom.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class WalletListDetails implements Parcelable, ValidItem {
  @SerializedName("data")
  @Expose
  private WalletDataDetails data;

  @SerializedName("message")
  @Expose
  private String message;

  public WalletListDetails(WalletDataDetails data, String message) {
    this.data = data;
    this.message = message;
  }

  protected WalletListDetails(Parcel in) {
    data = in.readParcelable(WalletDataDetails.class.getClassLoader());
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(data, flags);
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WalletListDetails> CREATOR = new Creator<WalletListDetails>() {
    @Override
    public WalletListDetails createFromParcel(Parcel in) {
      return new WalletListDetails(in);
    }

    @Override
    public WalletListDetails[] newArray(int size) {
      return new WalletListDetails[size];
    }
  };

  public WalletDataDetails getData ()
  {
    return data;
  }

  public void setData (WalletDataDetails data)
  {
    this.data = data;
  }

  public String getMessage ()
  {
    return message;
  }

  public void setMessage (String message)
  {
    this.message = message;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
