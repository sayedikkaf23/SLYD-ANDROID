package com.kotlintestgradle.remote.model.response.ecom.pdp.substitute;

import android.os.Parcel;
import android.os.Parcelable;
import com.kotlintestgradle.remote.core.ValidItem;

public class SubstituteOffers implements Parcelable, ValidItem {

  public SubstituteOffers() {
  }

  protected SubstituteOffers(Parcel in) {
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SubstituteOffers> CREATOR = new Creator<SubstituteOffers>() {
    @Override
    public SubstituteOffers createFromParcel(Parcel in) {
      return new SubstituteOffers(in);
    }

    @Override
    public SubstituteOffers[] newArray(int size) {
      return new SubstituteOffers[size];
    }
  };

  @Override
  public boolean isValid() {
    return true;
  }
}