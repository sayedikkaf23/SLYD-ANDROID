package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class UserReviewDetails implements ValidItem, Parcelable {
  @SerializedName("images")
  @Expose
  private ArrayList<String> images;
  @SerializedName("rating")
  @Expose
  private String rating;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("reviewDesc")
  @Expose
  private String reviewDesc;
  @SerializedName("reviewId")
  @Expose
  private String reviewId;
  @SerializedName("timestamp")
  @Expose
  private String timestamp;
  @SerializedName("reviewTitle")
  @Expose
  private String reviewTitle;
  @SerializedName("likes")
  @Expose
  private int likes;
  @SerializedName("disLikes")
  @Expose
  private int disLikes;
  @SerializedName("userLikes")
  @Expose
  private boolean userLikes;
  @SerializedName("userdisLikes")
  @Expose
  private boolean userdisLikes;

  public UserReviewDetails(ArrayList<String> images, String rating, String name, String reviewDesc,
      String reviewId, String timestamp, String reviewTitle, int likes, int disLikes,
      boolean userLikes, boolean userdisLikes) {
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

  protected UserReviewDetails(Parcel in) {
    images = in.createStringArrayList();
    rating = in.readString();
    name = in.readString();
    reviewDesc = in.readString();
    reviewId = in.readString();
    timestamp = in.readString();
    reviewTitle = in.readString();
    likes = in.readInt();
    disLikes = in.readInt();
    userLikes = in.readByte() != 0;
    userdisLikes = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringList(images);
    dest.writeString(rating);
    dest.writeString(name);
    dest.writeString(reviewDesc);
    dest.writeString(reviewId);
    dest.writeString(timestamp);
    dest.writeString(reviewTitle);
    dest.writeInt(likes);
    dest.writeInt(disLikes);
    dest.writeByte((byte) (userLikes ? 1 : 0));
    dest.writeByte((byte) (userdisLikes ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<UserReviewDetails> CREATOR = new Creator<UserReviewDetails>() {
    @Override
    public UserReviewDetails createFromParcel(Parcel in) {
      return new UserReviewDetails(in);
    }

    @Override
    public UserReviewDetails[] newArray(int size) {
      return new UserReviewDetails[size];
    }
  };

  public ArrayList<String> getImages() {
    return images;
  }

  public void setImages(ArrayList<String> images) {
    this.images = images;
  }

  public boolean isUserLikes() {
    return userLikes;
  }

  public void setUserLikes(boolean userLikes) {
    this.userLikes = userLikes;
  }

  public boolean isUserdisLikes() {
    return userdisLikes;
  }

  public void setUserdisLikes(boolean userdisLikes) {
    this.userdisLikes = userdisLikes;
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
  public boolean isValid() {
    return true;
  }
}
