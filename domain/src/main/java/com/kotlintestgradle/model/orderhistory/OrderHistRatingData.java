package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistRatingData implements Parcelable {

  private String reviewDescription;

  private boolean isRated;

  private float rating;

  private String reviewTitle;

  public OrderHistRatingData(String reviewDescription, boolean isRated, float rating,
      String reviewTitle) {
    this.reviewDescription = reviewDescription;
    this.isRated = isRated;
    this.rating = rating;
    this.reviewTitle = reviewTitle;
  }

  protected OrderHistRatingData(Parcel in) {
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

  public static final Creator<OrderHistRatingData> CREATOR = new Creator<OrderHistRatingData>() {
    @Override
    public OrderHistRatingData createFromParcel(Parcel in) {
      return new OrderHistRatingData(in);
    }

    @Override
    public OrderHistRatingData[] newArray(int size) {
      return new OrderHistRatingData[size];
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
}
