package com.kotlintestgradle.remote.model.response.ecom.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class WalletDetails implements Parcelable, ValidItem {
  @SerializedName("walletid")
  @Expose
  private String walletid;
  @SerializedName("balance")
  @Expose
  private String balance;
  @SerializedName("createddate")
  @Expose
  private String createddate;
  @SerializedName("usertype")
  @Expose
  private String usertype;
  @SerializedName("statustext")
  @Expose
  private String statustext;
  @SerializedName("currency")
  @Expose
  private String currency;
  @SerializedName("userid")
  @Expose
  private String userid;
  @SerializedName("status")
  @Expose
  private String status;

  public WalletDetails(String walletid, String balance, String createddate, String usertype,
      String statustext, String currency, String userid, String status) {
    this.walletid = walletid;
    this.balance = balance;
    this.createddate = createddate;
    this.usertype = usertype;
    this.statustext = statustext;
    this.currency = currency;
    this.userid = userid;
    this.status = status;
  }

  protected WalletDetails(Parcel in) {
    walletid = in.readString();
    balance = in.readString();
    createddate = in.readString();
    usertype = in.readString();
    statustext = in.readString();
    currency = in.readString();
    userid = in.readString();
    status = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(walletid);
    dest.writeString(balance);
    dest.writeString(createddate);
    dest.writeString(usertype);
    dest.writeString(statustext);
    dest.writeString(currency);
    dest.writeString(userid);
    dest.writeString(status);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WalletDetails> CREATOR = new Creator<WalletDetails>() {
    @Override
    public WalletDetails createFromParcel(Parcel in) {
      return new WalletDetails(in);
    }

    @Override
    public WalletDetails[] newArray(int size) {
      return new WalletDetails[size];
    }
  };

  public String getWalletid ()
  {
    return walletid;
  }

  public void setWalletid (String walletid)
  {
    this.walletid = walletid;
  }

  public String getBalance ()
  {
    return balance;
  }

  public void setBalance (String balance)
  {
    this.balance = balance;
  }

  public String getCreateddate ()
  {
    return createddate;
  }

  public void setCreateddate (String createddate)
  {
    this.createddate = createddate;
  }

  public String getUsertype ()
  {
    return usertype;
  }

  public void setUsertype (String usertype)
  {
    this.usertype = usertype;
  }

  public String getStatustext ()
  {
    return statustext;
  }

  public void setStatustext (String statustext)
  {
    this.statustext = statustext;
  }

  public String getCurrency ()
  {
    return currency;
  }

  public void setCurrency (String currency)
  {
    this.currency = currency;
  }

  public String getUserid ()
  {
    return userid;
  }

  public void setUserid (String userid)
  {
    this.userid = userid;
  }

  public String getStatus ()
  {
    return status;
  }

  public void setStatus (String status)
  {
    this.status = status;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
