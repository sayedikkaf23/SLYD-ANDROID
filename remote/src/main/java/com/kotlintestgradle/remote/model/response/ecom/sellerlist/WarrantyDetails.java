package com.kotlintestgradle.remote.model.response.ecom.sellerlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class WarrantyDetails  implements Parcelable, ValidItem {

  @SerializedName("en")
  @Expose
  private String en;

  @SerializedName("ar")
  @Expose
  private String ar;

  public WarrantyDetails(String en, String ar) {
    this.en = en;
    this.ar = ar;
  }

  protected WarrantyDetails(Parcel in) {
    en = in.readString();
    ar = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(en);
    dest.writeString(ar);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<WarrantyDetails> CREATOR = new Creator<WarrantyDetails>() {
    @Override
    public WarrantyDetails createFromParcel(Parcel in) {
      return new WarrantyDetails(in);
    }

    @Override
    public WarrantyDetails[] newArray(int size) {
      return new WarrantyDetails[size];
    }
  };

  public String getEn() {
    return en;
  }

  public void setEn(String en) {
    this.en = en;
  }

  public String getAr() {
    return ar;
  }

  public void setAr(String ar) {
    this.ar = ar;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
