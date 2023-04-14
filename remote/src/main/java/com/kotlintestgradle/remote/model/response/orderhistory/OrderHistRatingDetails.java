package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistRatingDetails  implements Parcelable, ValidItem {

  @Expose
  @SerializedName("reviewDescription")
  private String reviewDescription;
  @Expose
  @SerializedName("isRated")
  private boolean isRated;
  @Expose
  @SerializedName("rating")
  private float rating;
  @Expose
  @SerializedName("reviewTitle")
  private String reviewTitle;

  public OrderHistRatingDetails(String reviewDescription, boolean isRated, int rating,
      String reviewTitle) {
    this.reviewDescription = reviewDescription;
    this.isRated = isRated;
    this.rating = rating;
    this.reviewTitle = reviewTitle;
  }

  protected OrderHistRatingDetails(Parcel in) {
    reviewDescription = in.readString();
    isRated = in.readByte() != 0;
    rating = in.readFloat();
    reviewTitle = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(reviewDescription);
    dest.writeByte((byte) (isRated ? 1 : 0));
    dest.writeFloat(rating);
    dest.writeString(reviewTitle);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistRatingDetails> CREATOR =
      new Creator<OrderHistRatingDetails>() {
        @Override
        public OrderHistRatingDetails createFromParcel(Parcel in) {
          return new OrderHistRatingDetails(in);
        }

        @Override
        public OrderHistRatingDetails[] newArray(int size) {
          return new OrderHistRatingDetails[size];
        }
      };

  public String getReviewDescription() {
    return reviewDescription;
  }

  public void setReviewDescription(String reviewDescription) {
    this.reviewDescription = reviewDescription;
  }

  public boolean isRated() {
    return isRated;
  }

  public void setRated(boolean rated) {
    isRated = rated;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
