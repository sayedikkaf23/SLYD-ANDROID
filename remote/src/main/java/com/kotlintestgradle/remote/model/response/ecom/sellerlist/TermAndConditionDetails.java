package com.kotlintestgradle.remote.model.response.ecom.sellerlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class TermAndConditionDetails implements Parcelable, ValidItem {

  @SerializedName("en")
  @Expose
  private String en;

  @SerializedName("es")
  @Expose
  private String es;

  public TermAndConditionDetails(String en, String es) {
    this.en = en;
    this.es = es;
  }

  protected TermAndConditionDetails(Parcel in) {
    en = in.readString();
    es = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(en);
    dest.writeString(es);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<TermAndConditionDetails> CREATOR =
      new Creator<TermAndConditionDetails>() {
        @Override
        public TermAndConditionDetails createFromParcel(Parcel in) {
          return new TermAndConditionDetails(in);
        }

        @Override
        public TermAndConditionDetails[] newArray(int size) {
          return new TermAndConditionDetails[size];
        }
      };

  public String getEn() {
    return en;
  }

  public void setEn(String en) {
    this.en = en;
  }

  public String getEs() {
    return es;
  }

  public void setEs(String es) {
    this.es = es;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
