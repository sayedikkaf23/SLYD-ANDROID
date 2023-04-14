package com.kotlintestgradle.remote.model.response.ecom.getratable;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class UserReview  implements Parcelable, ValidItem {
  @SerializedName("reviewDescription")
  @Expose
  private String reviewDescription;
  @SerializedName("rating")
  @Expose
  private String rating;
  @SerializedName("productName")
  @Expose
  private String productName;
  @SerializedName("reviewTitle")
  @Expose
  private String reviewTitle;
  @SerializedName("attribute")
  @Expose
  private ArrayList<RatedAttributes> attribute;
  @SerializedName("image")
  @Expose
  private ArrayList<String> image;

  public UserReview(String reviewDescription, String rating, String productName,
      String reviewTitle, ArrayList<RatedAttributes> attribute,ArrayList<String> image) {
    this.reviewDescription = reviewDescription;
    this.rating = rating;
    this.productName = productName;
    this.reviewTitle = reviewTitle;
    this.image = image;
    this.attribute=attribute;
  }

  protected UserReview(Parcel in) {
    reviewDescription = in.readString();
    rating = in.readString();
    productName = in.readString();
    reviewTitle = in.readString();
    attribute = in.createTypedArrayList(RatedAttributes.CREATOR);
    image = in.createStringArrayList();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(reviewDescription);
    dest.writeString(rating);
    dest.writeString(productName);
    dest.writeString(reviewTitle);
    dest.writeTypedList(attribute);
    dest.writeStringList(image);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<UserReview> CREATOR = new Creator<UserReview>() {
    @Override
    public UserReview createFromParcel(Parcel in) {
      return new UserReview(in);
    }

    @Override
    public UserReview[] newArray(int size) {
      return new UserReview[size];
    }
  };

  public ArrayList<String> getImage() {
    return image;
  }

  public void setImage(ArrayList<String> image) {
    this.image = image;
  }

  public String getReviewDescription() {
    return reviewDescription;
  }

  public void setReviewDescription(String reviewDescription) {
    this.reviewDescription = reviewDescription;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  public ArrayList<RatedAttributes> getAttribute() {
    return attribute;
  }

  public void setAttribute(
      ArrayList<RatedAttributes> attribute) {
    this.attribute = attribute;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
