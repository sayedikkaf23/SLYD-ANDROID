package com.kotlintestgradle.model.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class UserReviewData implements Parcelable {
  @SuppressWarnings("unused")
  public static final Creator<UserReviewData> CREATOR =
      new Creator<UserReviewData>() {
        @Override
        public UserReviewData createFromParcel(Parcel in) {
          return new UserReviewData(in);
        }

        @Override
        public UserReviewData[] newArray(int size) {
          return new UserReviewData[size];
        }
      };
  private ArrayList<String> images;
  private String rating;
  private String name;
  private String reviewDesc;
  private String reviewId;
  private String timestamp;
  private String reviewTitle;
  private int likes;
  private int disLikes;
  private boolean userLikes;
  private boolean userdisLikes;

  public UserReviewData(ArrayList<String> images, String rating,
      String name, String reviewDesc, String reviewId, String timestamp,
      String reviewTitle, int likes, int disLikes, boolean userLikes, boolean userdisLikes) {
    this.images = images;
    this.rating = rating;
    this.name = name;
    this.reviewDesc = reviewDesc;
    this.reviewId = reviewId;
    this.timestamp = timestamp;
    this.reviewTitle = reviewTitle;
    this.likes = likes;
    this.disLikes = disLikes;
    this.userLikes = userLikes;
    this.userdisLikes = userdisLikes;
  }

  protected UserReviewData(Parcel in) {
    if (in.readByte() == 0x01) {
      images = new ArrayList<String>();
      in.readList(images, String.class.getClassLoader());
    } else {
      images = null;
    }
    rating = in.readString();
    name = in.readString();
    reviewDesc = in.readString();
    reviewId = in.readString();
    timestamp = in.readString();
    reviewTitle = in.readString();
    likes = in.readInt();
    disLikes = in.readInt();
    userLikes = in.readByte() != 0x00;
    userdisLikes = in.readByte() != 0x00;
  }

  public Boolean getLikeSel() {
    return userLikes;
  }

  public void setLikeSel(Boolean likeSel) {
    this.userLikes = likeSel;
  }

  public Boolean getDisLikeSel() {
    return userdisLikes;
  }

  public void setDisLikeSel(Boolean disLikeSel) {
    this.userdisLikes = disLikeSel;
  }

  public ArrayList<String> getImages() {
    return images;
  }

  public void setImages(ArrayList<String> images) {
    this.images = images;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getReviewDesc() {
    return reviewDesc;
  }

  public void setReviewDesc(String reviewDesc) {
    this.reviewDesc = reviewDesc;
  }

  public String getReviewId() {
    return reviewId;
  }

  public void setReviewId(String reviewId) {
    this.reviewId = reviewId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public int getDisLikes() {
    return disLikes;
  }

  public void setDisLikes(int disLikes) {
    this.disLikes = disLikes;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (images == null) {
      dest.writeByte((byte) (0x00));
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeList(images);
    }
    dest.writeString(rating);
    dest.writeString(name);
    dest.writeString(reviewDesc);
    dest.writeString(reviewId);
    dest.writeString(timestamp);
    dest.writeString(reviewTitle);
    dest.writeInt(likes);
    dest.writeInt(disLikes);
    dest.writeByte((byte) (userLikes ? 0x01 : 0x00));
    dest.writeByte((byte) (userdisLikes ? 0x01 : 0x00));
  }
}
