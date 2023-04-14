package com.kotlintestgradle.model.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;

public class Rating implements Parcelable {
  @SuppressWarnings("unused")
  public static final Creator<Rating> CREATOR = new Creator<Rating>() {
    @Override
    public Rating createFromParcel(Parcel in) {
      return new Rating(in);
    }

    @Override
    public Rating[] newArray(int size) {
      return new Rating[size];
    }
  };
  private int ratingRange;
  private int ratingCount;
  private int percentage;
  private int totalNoOfRating;

  public Rating(int ratingRange, int ratingCount, int totalNoOfRating) {
    this.ratingRange = ratingRange;
    this.ratingCount = ratingCount;
    this.totalNoOfRating = totalNoOfRating;
    Double perc = 0.0;
    if (ratingCount > 0 && totalNoOfRating > 0) {
      perc = ((double) ratingCount / totalNoOfRating) * 100;
    } else {
      this.percentage = 0;
    }
    this.percentage = Integer.valueOf(perc.intValue());
  }

  protected Rating(Parcel in) {
    ratingRange = in.readInt();
    ratingCount = in.readInt();
    percentage = in.readInt();
    totalNoOfRating = in.readInt();
  }

  public int getRatingRange() {
    return ratingRange;
  }

  public void setRatingRange(int ratingRange) {
    this.ratingRange = ratingRange;
  }

  public int getRatingCount() {
    return ratingCount;
  }

  public void setRatingCount(int ratingCount) {
    this.ratingCount = ratingCount;
  }

  public int getPercentage() {
    return percentage;
  }

  public void setPercentage(int percentage) {
    this.percentage = percentage;
  }

  public int getTotalRating() {
    return totalNoOfRating;
  }

  public void setTotalRating(int totalRating) {
    totalNoOfRating = totalRating;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(ratingRange);
    dest.writeInt(ratingCount);
    dest.writeInt(percentage);
    dest.writeInt(totalNoOfRating);
  }
}
