package com.kotlintestgradle.remote.model.response.ecom.getratable;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class SellerDetails implements Parcelable, ValidItem {
  @SerializedName("reviewDescription")
  @Expose
  private String reviewDescription;
  @SerializedName("rating")
  @Expose
  private String rating;
  @SerializedName("storeName")
  @Expose
  private String storeName;
  @SerializedName("attribute")
  @Expose
  private ArrayList<RatedAttributes> attribute;
  @SerializedName("storeId")
  @Expose
  private String storeId;
  @SerializedName("reviewTitle")
  @Expose
  private String reviewTitle;
  @SerializedName("sellerReview")
  @Expose
  private String sellerReview;

  protected SellerDetails(Parcel in) {
    reviewDescription = in.readString();
    rating = in.readString();
    storeName = in.readString();
    attribute = in.createTypedArrayList(RatedAttributes.CREATOR);
    storeId = in.readString();
    reviewTitle = in.readString();
    sellerReview = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(reviewDescription);
    dest.writeString(rating);
    dest.writeString(storeName);
    dest.writeTypedList(attribute);
    dest.writeString(storeId);
    dest.writeString(reviewTitle);
    dest.writeString(sellerReview);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SellerDetails> CREATOR = new Creator<SellerDetails>() {
    @Override
    public SellerDetails createFromParcel(Parcel in) {
      return new SellerDetails(in);
    }

    @Override
    public SellerDetails[] newArray(int size) {
      return new SellerDetails[size];
    }
  };

  public String getReviewDescription ()
  {
    return reviewDescription;
  }

  public void setReviewDescription (String reviewDescription)
  {
    this.reviewDescription = reviewDescription;
  }

  public String getRating ()
  {
    return rating;
  }

  public void setRating (String rating)
  {
    this.rating = rating;
  }

  public String getStoreName ()
  {
    return storeName;
  }

  public void setStoreName (String storeName)
  {
    this.storeName = storeName;
  }

  public ArrayList<RatedAttributes> getAttribute ()
  {
    return attribute;
  }

  public void setAttribute (ArrayList<RatedAttributes> attribute)
  {
    this.attribute = attribute;
  }

  public String getStoreId ()
  {
    return storeId;
  }

  public void setStoreId (String storeId)
  {
    this.storeId = storeId;
  }

  public String getReviewTitle ()
  {
    return reviewTitle;
  }

  public void setReviewTitle (String reviewTitle)
  {
    this.reviewTitle = reviewTitle;
  }

  public String getSellerReview() {
    return sellerReview;
  }

  public void setSellerReview(String sellerReview) {
    this.sellerReview = sellerReview;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
