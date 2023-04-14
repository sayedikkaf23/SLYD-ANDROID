package com.kotlintestgradle.remote.model.response.ecom.getratable;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class RatedAttributes implements Parcelable, ValidItem {
  @SerializedName("attributeName")
  @Expose
  private String attributeName;

  @SerializedName("attributeId")
  @Expose
  private String attributeId;

  @SerializedName("attributeRating")
  @Expose
  private int attributeRating;

  public RatedAttributes(String attributeName, String attributeId, int attributeRating) {
    this.attributeName = attributeName;
    this.attributeId = attributeId;
    this.attributeRating = attributeRating;
  }

  protected RatedAttributes(Parcel in) {
    attributeName = in.readString();
    attributeId = in.readString();
    attributeRating = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(attributeName);
    dest.writeString(attributeId);
    dest.writeInt(attributeRating);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<RatedAttributes> CREATOR = new Creator<RatedAttributes>() {
    @Override
    public RatedAttributes createFromParcel(Parcel in) {
      return new RatedAttributes(in);
    }

    @Override
    public RatedAttributes[] newArray(int size) {
      return new RatedAttributes[size];
    }
  };

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public int getAttributeRating() {
    return attributeRating;
  }

  public void setAttributeRating(int attributeRating) {
    this.attributeRating = attributeRating;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
