package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class AttributeRating implements Parcelable, ValidItem {
  @SerializedName("attributeId")
  @Expose
  private String attributeId;
  @SerializedName("rating")
  @Expose
  private Float rating;
  @SerializedName("attributeName")
  @Expose
  private String attributeName;
  @SerializedName("TotalStarRating")
  @Expose
  private Float TotalStarRating;

  public AttributeRating(String attributeId, Float rating, String attributeName,
      Float totalStarRating) {
    this.attributeId = attributeId;
    this.rating = rating;
    this.attributeName = attributeName;
    TotalStarRating = totalStarRating;
  }

  protected AttributeRating(Parcel in) {
    attributeId = in.readString();
    if (in.readByte() == 0) {
      rating = null;
    } else {
      rating = in.readFloat();
    }
    attributeName = in.readString();
    if (in.readByte() == 0) {
      TotalStarRating = null;
    } else {
      TotalStarRating = in.readFloat();
    }
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(attributeId);
    if (rating == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeFloat(rating);
    }
    dest.writeString(attributeName);
    if (TotalStarRating == null) {
      dest.writeByte((byte) 0);
    } else {
      dest.writeByte((byte) 1);
      dest.writeFloat(TotalStarRating);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<AttributeRating> CREATOR = new Creator<AttributeRating>() {
    @Override
    public AttributeRating createFromParcel(Parcel in) {
      return new AttributeRating(in);
    }

    @Override
    public AttributeRating[] newArray(int size) {
      return new AttributeRating[size];
    }
  };

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public Float getRating() {
    return rating;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public Float getTotalStarRating() {
    return TotalStarRating;
  }

  public void setTotalStarRating(Float TotalStarRating) {
    this.TotalStarRating = TotalStarRating;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
