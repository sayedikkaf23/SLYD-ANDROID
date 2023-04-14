package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class InnerAttributesDetails implements ValidItem, Parcelable {

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("value")
  @Expose
  private String value;

  public InnerAttributesDetails(String name, String value) {
    this.name = name;
    this.value = value;
  }

  protected InnerAttributesDetails(Parcel in) {
    name = in.readString();
    value = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<InnerAttributesDetails> CREATOR =
      new Creator<InnerAttributesDetails>() {
        @Override
        public InnerAttributesDetails createFromParcel(Parcel in) {
          return new InnerAttributesDetails(in);
        }

        @Override
        public InnerAttributesDetails[] newArray(int size) {
          return new InnerAttributesDetails[size];
        }
      };

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
