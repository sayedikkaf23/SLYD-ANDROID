package com.kotlintestgradle.remote.model.response.ecom.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class WalletEarningDetails implements Parcelable, ValidItem {
  @SerializedName("balance")
  @Expose
  private String balance;
  @SerializedName("createddate")
  @Expose
  private String createddate;
  @SerializedName("walletearningid")
  @Expose
  private String walletearningid;
  @SerializedName("usertype")
  @Expose
  private String usertype;
  @SerializedName("statustext")
  @Expose
  private String statustext;
  @SerializedName("userid")
  @Expose
  private String userid;
  @SerializedName("status")
  @Expose
  private String status;

  public WalletEarningDetails(String balance, String createddate, String walletearningid,
      String usertype, String statustext, String userid, String status) {
    this.balance = balance;
    this.createddate = createddate;
    this.walletearningid = walletearningid;
    this.usertype = usertype;
    this.statustext = statustext;
    this.userid = userid;
    this.status = status;
  }

  protected WalletEarningDetails(Parcel in) {
    balance = in.readString();
    createddate = in.readString();
    walletearningid = in.readString();
    usertype = in.readString();
    statustext = in.readString();
    userid = in.readString();
    status = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(balance);
    dest.writeString(createddate);
    dest.writeString(walletearningid);
    dest.writeString(usertype);
    dest.writeString(statustext);
    dest.writeString(userid);
    dest.writeString(status);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WalletEarningDetails> CREATOR = new Creator<WalletEarningDetails>() {
    @Override
    public WalletEarningDetails createFromParcel(Parcel in) {
      return new WalletEarningDetails(in);
    }

    @Override
    public WalletEarningDetails[] newArray(int size) {
      return new WalletEarningDetails[size];
    }
  };

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

  public String getWalletearningid ()
  {
    return walletearningid;
  }

  public void setWalletearningid (String walletearningid)
  {
    this.walletearningid = walletearningid;
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
