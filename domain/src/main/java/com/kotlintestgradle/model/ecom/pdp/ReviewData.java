package com.kotlintestgradle.model.ecom.pdp;

import java.util.ArrayList;

public class ReviewData {
  private int mFourStartRating;
  private int penCount;
  private int mTwoStarRating;
  private int mTotalNoOfRatings;
  private int mThreeStarRating;
  private float mTotalStarRating;
  private int mFiveStarRating;
  private int mOneStarRating;
  private int mTotalNoOfReviews;
  private ArrayList<AttributeRatingData> attributeRating;
  private ArrayList<UserReviewData> userReviews;
  private ArrayList<String> images;
  private ArrayList<Rating> ratings;

  public ReviewData(int fourStartRating, int penCount, int twoStarRating,
      int totalNoOfRatings, int threeStarRating, float totalStarRating,
      int fiveStarRating, int oneStarRating, int totalNoOfReviews,
      ArrayList<AttributeRatingData> attributeRating, ArrayList<UserReviewData> userReviews,
      ArrayList<String> images, ArrayList<Rating> ratings) {
    mFourStartRating = fourStartRating;
    this.penCount = penCount;
    mTwoStarRating = twoStarRating;
    mTotalNoOfRatings = totalNoOfRatings;
    mThreeStarRating = threeStarRating;
    mTotalStarRating = totalStarRating;
    mFiveStarRating = fiveStarRating;
    mOneStarRating = oneStarRating;
    mTotalNoOfReviews = totalNoOfReviews;
    this.attributeRating = attributeRating;
    this.userReviews = userReviews;
    this.images = images;
    this.ratings = ratings;
  }

  public ArrayList<Rating> getRatings() {
    return ratings;
  }

  public void setRatings(ArrayList<Rating> ratings) {
    this.ratings = ratings;
  }

  public int getFourStartRating() {
    return mFourStartRating;
  }

  public void setFourStartRating(int fourStartRating) {
    mFourStartRating = fourStartRating;
  }

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public int getTwoStarRating() {
    return mTwoStarRating;
  }

  public void setTwoStarRating(int twoStarRating) {
    mTwoStarRating = twoStarRating;
  }

  public int getTotalNoOfRatings() {
    return mTotalNoOfRatings;
  }

  public void setTotalNoOfRatings(int totalNoOfRatings) {
    mTotalNoOfRatings = totalNoOfRatings;
  }

  public int getThreeStarRating() {
    return mThreeStarRating;
  }

  public void setThreeStarRating(int threeStarRating) {
    mThreeStarRating = threeStarRating;
  }

  public float getTotalStarRating() {
    return mTotalStarRating;
  }

  public void setTotalStarRating(float totalStarRating) {
    mTotalStarRating = totalStarRating;
  }

  public int getFiveStarRating() {
    return mFiveStarRating;
  }

  public void setFiveStarRating(int fiveStarRating) {
    mFiveStarRating = fiveStarRating;
  }

  public int getOneStarRating() {
    return mOneStarRating;
  }

  public void setOneStarRating(int oneStarRating) {
    mOneStarRating = oneStarRating;
  }

  public int getTotalNoOfReviews() {
    return mTotalNoOfReviews;
  }

  public void setTotalNoOfReviews(int totalNoOfReviews) {
    mTotalNoOfReviews = totalNoOfReviews;
  }

  public ArrayList<AttributeRatingData> getAttributeRating() {
    return attributeRating;
  }

  public void setAttributeRating(ArrayList<AttributeRatingData> attributeRating) {
    this.attributeRating = attributeRating;
  }

  public ArrayList<UserReviewData> getUserReviews() {
    return userReviews;
  }

  public void setUserReviews(ArrayList<UserReviewData> userReviews) {
    this.userReviews = userReviews;
  }

  public ArrayList<String> getImages() {
    return images;
  }

  public void setImages(ArrayList<String> images) {
    this.images = images;
  }
}
