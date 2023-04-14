package com.kotlintestgradle.remote.model.response.ecom.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class WalletTransactionsListDetails implements Parcelable, ValidItem {
  @SerializedName("data")
  @Expose
  private ArrayList<WalletTransactionItemDetails> data;
  @SerializedName("message")
  @Expose
  private String message;

  @SerializedName("totalCount")
  @Expose
  private String totalCount;
  @SerializedName("pageState")
  @Expose
  private String pageState;

  protected WalletTransactionsListDetails(Parcel in) {
    data = in.createTypedArrayList(WalletTransactionItemDetails.CREATOR);
    message = in.readString();
    totalCount = in.readString();
    pageState = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(data);
    dest.writeString(message);
    dest.writeString(totalCount);
    dest.writeString(pageState);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WalletTransactionsListDetails> CREATOR =
      new Creator<WalletTransactionsListDetails>() {
        @Override
        public WalletTransactionsListDetails createFromParcel(Parcel in) {
          return new WalletTransactionsListDetails(in);
        }

        @Override
        public WalletTransactionsListDetails[] newArray(int size) {
          return new WalletTransactionsListDetails[size];
        }
      };

  public ArrayList<WalletTransactionItemDetails> getData() {
    return data;
  }

  public void setData(ArrayList<WalletTransactionItemDetails> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }

  public String getPageState() {
    return pageState;
  }

  public void setPageState(String pageState) {
    this.pageState = pageState;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
