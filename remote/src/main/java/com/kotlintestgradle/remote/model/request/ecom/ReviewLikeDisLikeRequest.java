package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewLikeDisLikeRequest implements Parcelable {
  @SerializedName("reviewId")
  @Expose
  private String reviewId;

  @SerializedName("like")
  @Expose
  private boolean like;

  @SerializedName("dislike")
  @Expose
  private boolean dislike;

  public ReviewLikeDisLikeRequest(String reviewId, boolean like) {
    this.reviewId = reviewId;
    this.like = like;
  }

  public ReviewLikeDisLikeRequest(boolean dislike, String reviewId) {
    this.reviewId = reviewId;
    this.dislike = dislike;
  }

  protected ReviewLikeDisLikeRequest(Parcel in) {
    reviewId = in.readString();
    like = in.readByte() != 0;
    dislike = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(reviewId);
    dest.writeByte((byte) (like ? 1 : 0));
    dest.writeByte((byte) (dislike ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ReviewLikeDisLikeRequest> CREATOR =
      new Creator<ReviewLikeDisLikeRequest>() {
        @Override
        public ReviewLikeDisLikeRequest createFromParcel(Parcel in) {
          return new ReviewLikeDisLikeRequest(in);
        }

        @Override
        public ReviewLikeDisLikeRequest[] newArray(int size) {
          return new ReviewLikeDisLikeRequest[size];
        }
      };

  public String getReviewId() {
    return reviewId;
  }

  public void setReviewId(String reviewId) {
    this.reviewId = reviewId;
  }

  public boolean isLike() {
    return like;
  }

  public void setLike(boolean like) {
    this.like = like;
  }

  public boolean isDislike() {
    return dislike;
  }

  public void setDislike(boolean dislike) {
    this.dislike = dislike;
  }
}
