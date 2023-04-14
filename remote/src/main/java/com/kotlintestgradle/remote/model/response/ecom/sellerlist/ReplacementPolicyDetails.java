package com.kotlintestgradle.remote.model.response.ecom.sellerlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ReplacementPolicyDetails implements Parcelable, ValidItem {
  @SerializedName("noofdays")
  @Expose
  private int noofdays;

  @SerializedName("isReplacement")
  @Expose
  private boolean isReplacement;

  public ReplacementPolicyDetails(int noofdays, boolean isReplacement) {
    this.noofdays = noofdays;
    this.isReplacement = isReplacement;
  }

  protected ReplacementPolicyDetails(Parcel in) {
    noofdays = in.readInt();
    isReplacement = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(noofdays);
    dest.writeByte((byte) (isReplacement ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ReplacementPolicyDetails> CREATOR =
      new Creator<ReplacementPolicyDetails>() {
        @Override
        public ReplacementPolicyDetails createFromParcel(Parcel in) {
          return new ReplacementPolicyDetails(in);
        }

        @Override
        public ReplacementPolicyDetails[] newArray(int size) {
          return new ReplacementPolicyDetails[size];
        }
      };

  public int getNoofdays() {
    return noofdays;
  }

  public void setNoofdays(int noofdays) {
    this.noofdays = noofdays;
  }

  public boolean isReplacement() {
    return isReplacement;
  }

  public void setReplacement(boolean replacement) {
    isReplacement = replacement;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
