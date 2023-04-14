package com.kotlintestgradle.remote.model.response.ecom.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class WalletTransactionItemDetails  implements Parcelable, ValidItem {
  @SerializedName("walletid")
  @Expose
  private String walletid;
  @SerializedName("amount")
  @Expose
  private String amount;
  @SerializedName("notes")
  @Expose
  private String notes;
  @SerializedName("txntypetext")
  @Expose
  private String txntypetext;
  @SerializedName("txntimestamp")
  @Expose
  private String txntimestamp;
  @SerializedName("closingbal")
  @Expose
  private String closingbal;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("trigger")
  @Expose
  private String trigger;
  @SerializedName("openingbal")
  @Expose
  private String openingbal;
  @SerializedName("txntype")
  @Expose
  private String txntype;
  @SerializedName("currency")
  @Expose
  private String currency;
  @SerializedName("initiatedby")
  @Expose
  private String initiatedby;
  @SerializedName("txnid")
  @Expose
  private String txnid;

  protected WalletTransactionItemDetails(Parcel in) {
    walletid = in.readString();
    amount = in.readString();
    notes = in.readString();
    txntypetext = in.readString();
    txntimestamp = in.readString();
    closingbal = in.readString();
    description = in.readString();
    trigger = in.readString();
    openingbal = in.readString();
    txntype = in.readString();
    currency = in.readString();
    initiatedby = in.readString();
    txnid = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(walletid);
    dest.writeString(amount);
    dest.writeString(notes);
    dest.writeString(txntypetext);
    dest.writeString(txntimestamp);
    dest.writeString(closingbal);
    dest.writeString(description);
    dest.writeString(trigger);
    dest.writeString(openingbal);
    dest.writeString(txntype);
    dest.writeString(currency);
    dest.writeString(initiatedby);
    dest.writeString(txnid);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WalletTransactionItemDetails> CREATOR =
      new Creator<WalletTransactionItemDetails>() {
        @Override
        public WalletTransactionItemDetails createFromParcel(Parcel in) {
          return new WalletTransactionItemDetails(in);
        }

        @Override
        public WalletTransactionItemDetails[] newArray(int size) {
          return new WalletTransactionItemDetails[size];
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

  public String getAmount ()
  {
    return amount;
  }

  public void setAmount (String amount)
  {
    this.amount = amount;
  }

  public String getNotes ()
  {
    return notes;
  }

  public void setNotes (String notes)
  {
    this.notes = notes;
  }

  public String getTxntypetext ()
  {
    return txntypetext;
  }

  public void setTxntypetext (String txntypetext)
  {
    this.txntypetext = txntypetext;
  }

  public String getTxntimestamp ()
  {
    return txntimestamp;
  }

  public void setTxntimestamp (String txntimestamp)
  {
    this.txntimestamp = txntimestamp;
  }

  public String getClosingbal ()
  {
    return closingbal;
  }

  public void setClosingbal (String closingbal)
  {
    this.closingbal = closingbal;
  }

  public String getDescription ()
  {
    return description;
  }

  public void setDescription (String description)
  {
    this.description = description;
  }

  public String getTrigger ()
  {
    return trigger;
  }

  public void setTrigger (String trigger)
  {
    this.trigger = trigger;
  }

  public String getOpeningbal ()
  {
    return openingbal;
  }

  public void setOpeningbal (String openingbal)
  {
    this.openingbal = openingbal;
  }

  public String getTxntype ()
  {
    return txntype;
  }

  public void setTxntype (String txntype)
  {
    this.txntype = txntype;
  }

  public String getCurrency ()
  {
    return currency;
  }

  public void setCurrency (String currency)
  {
    this.currency = currency;
  }

  public String getInitiatedby ()
  {
    return initiatedby;
  }

  public void setInitiatedby (String initiatedby)
  {
    this.initiatedby = initiatedby;
  }

  public String getTxnid ()
  {
    return txnid;
  }

  public void setTxnid (String txnid)
  {
    this.txnid = txnid;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
