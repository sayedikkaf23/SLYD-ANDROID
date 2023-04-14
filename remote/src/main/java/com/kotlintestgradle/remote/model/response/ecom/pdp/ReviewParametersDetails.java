package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ReviewParametersDetails implements Parcelable, ValidItem {
  @SerializedName("attributeId")
  @Expose
  private String attributeId;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("rating")
  @Expose
  private String rating;
  @SerializedName("TotalStarRating")
  @Expose
  private Float TotalStarRating;

  protected ReviewParametersDetails(Parcel in) {
    attributeId = in.readString();
    name = in.readString();
    rating = in.readString();
    if (in.readByte() == 0) {
      TotalStarRating = null;
    } else {
      TotalStarRating = in.readFloat();
    }
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(attributeId);
    dest.writeString(name);
    dest.writeString(rating);
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

  public static final Creator<ReviewParametersDetails> CREATOR =
      new Creator<ReviewParametersDetails>() {
        @Override
        public ReviewParametersDetails createFromParcel(Parcel in) {
          return new ReviewParametersDetails(in);
        }

        @Override
        public ReviewParametersDetails[] newArray(int size) {
          return new ReviewParametersDetails[size];
        }
      };

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
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
