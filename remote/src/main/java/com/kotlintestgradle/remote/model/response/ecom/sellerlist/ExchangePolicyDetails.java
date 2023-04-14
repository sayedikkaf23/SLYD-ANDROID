package com.kotlintestgradle.remote.model.response.ecom.sellerlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ExchangePolicyDetails  implements Parcelable, ValidItem {

  @SerializedName("noofdays")
  @Expose
  private int noofdays;

  @SerializedName("isExchange")
  @Expose
  private boolean isExchange;

  public ExchangePolicyDetails(int noofdays, boolean isExchange) {
    this.noofdays = noofdays;
    this.isExchange = isExchange;
  }

  protected ExchangePolicyDetails(Parcel in) {
    noofdays = in.readInt();
    isExchange = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(noofdays);
    dest.writeByte((byte) (isExchange ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ExchangePolicyDetails> CREATOR =
      new Creator<ExchangePolicyDetails>() {
        @Override
        public ExchangePolicyDetails createFromParcel(Parcel in) {
          return new ExchangePolicyDetails(in);
        }

        @Override
        public ExchangePolicyDetails[] newArray(int size) {
          return new ExchangePolicyDetails[size];
        }
      };

  public int getNoofdays() {
    return noofdays;
  }

  public void setNoofdays(int noofdays) {
    this.noofdays = noofdays;
  }

  public boolean isExchange() {
    return isExchange;
  }

  public void setExchange(boolean exchange) {
    isExchange = exchange;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
