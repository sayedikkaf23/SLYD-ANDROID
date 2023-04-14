package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeAddressDefaultRequest implements Parcelable {

  @SerializedName("addressId")
  @Expose
  private String addressId;

  public MakeAddressDefaultRequest(String addressId) {
    this.addressId = addressId;
  }

  protected MakeAddressDefaultRequest(Parcel in) {
    addressId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(addressId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<MakeAddressDefaultRequest> CREATOR =
      new Creator<MakeAddressDefaultRequest>() {
        @Override
        public MakeAddressDefaultRequest createFromParcel(Parcel in) {
          return new MakeAddressDefaultRequest(in);
        }

        @Override
        public MakeAddressDefaultRequest[] newArray(int size) {
          return new MakeAddressDefaultRequest[size];
        }
      };

  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }
}
