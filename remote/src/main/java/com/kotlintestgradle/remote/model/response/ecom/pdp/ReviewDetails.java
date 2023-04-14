package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class ReviewDetails implements ValidItem, Parcelable {

  @SerializedName("userReviews")
  @Expose
  private ArrayList<UserReviewDetails> userReviews;

  @SerializedName("images")
  @Expose
  private ArrayList<String> images;

  @SerializedName("penCount")
  @Expose
  private int penCount;

  @SerializedName("TotalNoOfReviews")
  @Expose
  private int TotalNoOfReviews;

  @SerializedName("FiveStarRating")
  @Expose
  private int FiveStarRating;

  @SerializedName("TotalNoOfRatings")
  @Expose
  private int TotalNoOfRatings;

  @SerializedName("OneStarRating")
  @Expose
  private int OneStarRating;

  @SerializedName("ThreeStarRating")
  @Expose
  private int ThreeStarRating;

  @SerializedName("TotalStarRating")
  @Expose
  private float TotalStarRating;

  @SerializedName("TwoStarRating")
  @Expose
  private int TwoStarRating;

  @SerializedName("FourStartRating")
  @Expose
  private int FourStartRating;

  @SerializedName("attributeRating")
  @Expose
  private ArrayList<AttributeRating> attributeRating;

  public ReviewDetails(ArrayList<UserReviewDetails> userReviews, ArrayList<String> images,
      int penCount, int totalNoOfReviews, int fiveStarRating, int totalNoOfRatings,
      int oneStarRating, int threeStarRating, float totalStarRating, int twoStarRating,
      int fourStartRating, ArrayList<AttributeRating> attributeRating) {
    this.userReviews = userReviews;
    this.images = images;
    this.penCount = penCount;
    TotalNoOfReviews = totalNoOfReviews;
    FiveStarRating = fiveStarRating;
    TotalNoOfRatings = totalNoOfRatings;
    OneStarRating = oneStarRating;
    ThreeStarRating = threeStarRating;
    TotalStarRating = totalStarRating;
    TwoStarRating = twoStarRating;
    FourStartRating = fourStartRating;
    this.attributeRating = attributeRating;
  }

  protected ReviewDetails(Parcel in) {
    images = in.createStringArrayList();
    penCount = in.readInt();
    TotalNoOfReviews = in.readInt();
    FiveStarRating = in.readInt();
    TotalNoOfRatings = in.readInt();
    OneStarRating = in.readInt();
    ThreeStarRating = in.readInt();
    TotalStarRating = in.readFloat();
    TwoStarRating = in.readInt();
    FourStartRating = in.readInt();
    attributeRating = in.createTypedArrayList(AttributeRating.CREATOR);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringList(images);
    dest.writeInt(penCount);
    dest.writeInt(TotalNoOfReviews);
    dest.writeInt(FiveStarRating);
    dest.writeInt(TotalNoOfRatings);
    dest.writeInt(OneStarRating);
    dest.writeInt(ThreeStarRating);
    dest.writeFloat(TotalStarRating);
    dest.writeInt(TwoStarRating);
    dest.writeInt(FourStartRating);
    dest.writeTypedList(attributeRating);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ReviewDetails> CREATOR = new Creator<ReviewDetails>() {
    @Override
    public ReviewDetails createFromParcel(Parcel in) {
      return new ReviewDetails(in);
    }

    @Override
    public ReviewDetails[] newArray(int size) {
      return new ReviewDetails[size];
    }
  };

  public ArrayList<UserReviewDetails> getUserReviews() {
    return userReviews;
  }

  public void setUserReviews(ArrayList<UserReviewDetails> userReviews) {
    this.userReviews = userReviews;
  }

  public ArrayList<String> getImages() {
    return images;
  }

  public void setImages(ArrayList<String> images) {
    this.images = images;
  }

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public int getTotalNoOfReviews() {
    return TotalNoOfReviews;
  }

  public void setTotalNoOfReviews(int totalNoOfReviews) {
    TotalNoOfReviews = totalNoOfReviews;
  }

  public int getFiveStarRating() {
    return FiveStarRating;
  }

  public void setFiveStarRating(int fiveStarRating) {
    FiveStarRating = fiveStarRating;
  }

  public int getTotalNoOfRatings() {
    return TotalNoOfRatings;
  }

  public void setTotalNoOfRatings(int totalNoOfRatings) {
    TotalNoOfRatings = totalNoOfRatings;
  }

  public int getOneStarRating() {
    return OneStarRating;
  }

  public void setOneStarRating(int oneStarRating) {
    OneStarRating = oneStarRating;
  }

  public int getThreeStarRating() {
    return ThreeStarRating;
  }

  public void setThreeStarRating(int threeStarRating) {
    ThreeStarRating = threeStarRating;
  }

  public float getTotalStarRating() {
    return TotalStarRating;
  }

  public void setTotalStarRating(float totalStarRating) {
    TotalStarRating = totalStarRating;
  }

  public int getTwoStarRating() {
    return TwoStarRating;
  }

  public void setTwoStarRating(int twoStarRating) {
    TwoStarRating = twoStarRating;
  }

  public int getFourStartRating() {
    return FourStartRating;
  }

  public void setFourStartRating(int fourStartRating) {
    FourStartRating = fourStartRating;
  }

  public ArrayList<AttributeRating> getAttributeRating() {
    return attributeRating;
  }

  public void setAttributeRating(ArrayList<AttributeRating> attributeRating) {
    this.attributeRating = attributeRating;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
