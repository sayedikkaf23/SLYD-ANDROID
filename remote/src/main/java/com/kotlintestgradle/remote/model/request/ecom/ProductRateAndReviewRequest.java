package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ProductRateAndReviewRequest implements Parcelable {
  @SerializedName("type")
  @Expose
  private int type;
  @SerializedName("reviewType")
  @Expose
  private int reviewType;

  @SerializedName("productId")
  @Expose
  private String productId;

  @SerializedName("attributeId")
  @Expose
  private String attributeId;

  @SerializedName("reviewTitle")
  @Expose
  private String reviewTitle;

  @SerializedName("sellerReview")
  @Expose
  private String sellerReview;

  @SerializedName("reviewDescription")
  @Expose
  private String reviewDescription;

  @SerializedName("rating")
  @Expose
  private double rating;

  @SerializedName("images")
  @Expose
  private ArrayList<String> images;

  @SerializedName("city")
  @Expose
  private String city;

  @SerializedName("country")
  @Expose
  private String country;

  @SerializedName("sellerId")
  @Expose
  private String sellerId;

  @SerializedName("driverId")
  @Expose
  private String driverId;

  @SerializedName("driverReview")
  @Expose
  private String driverReview;

  @SerializedName("orderId")
  @Expose
  private String storeOrderId;

  public ProductRateAndReviewRequest(int type,int reviewType, String productId, String attributeId,
      String reviewTitle, String reviewDescription, double rating,
      ArrayList<String> images, String city, String country,String sellerReview,String sellerId,String storeOrderId,String driverId,String driverReview) {
    this.type = type;
    this.reviewType=reviewType;
    if (productId!=null && !productId.isEmpty())
    this.productId = productId;
    this.attributeId = attributeId;
    this.reviewTitle = reviewTitle;
    this.reviewDescription = reviewDescription;
    this.rating = rating;
    this.images = images;
    this.city = city;
    this.country = country;
    this.sellerReview=sellerReview;
    this.storeOrderId=storeOrderId;
    if (sellerId!=null && !sellerId.isEmpty())
    this.sellerId=sellerId;
    this.driverId=driverId;
    this.driverReview=driverReview;
  }

  protected ProductRateAndReviewRequest(Parcel in) {
    type = in.readInt();
    reviewType = in.readInt();
    productId = in.readString();
    attributeId = in.readString();
    reviewTitle = in.readString();
    sellerReview = in.readString();
    reviewDescription = in.readString();
    rating = in.readDouble();
    images = in.createStringArrayList();
    city = in.readString();
    country = in.readString();
    sellerId = in.readString();
    storeOrderId = in.readString();
    driverId = in.readString();
    driverReview = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(type);
    dest.writeInt(reviewType);
    dest.writeString(productId);
    dest.writeString(attributeId);
    dest.writeString(reviewTitle);
    dest.writeString(sellerReview);
    dest.writeString(reviewDescription);
    dest.writeDouble(rating);
    dest.writeStringList(images);
    dest.writeString(city);
    dest.writeString(country);
    dest.writeString(sellerId);
    dest.writeString(storeOrderId);
    dest.writeString(driverId);
    dest.writeString(driverReview);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductRateAndReviewRequest> CREATOR =
      new Creator<ProductRateAndReviewRequest>() {
        @Override
        public ProductRateAndReviewRequest createFromParcel(Parcel in) {
          return new ProductRateAndReviewRequest(in);
        }

        @Override
        public ProductRateAndReviewRequest[] newArray(int size) {
          return new ProductRateAndReviewRequest[size];
        }
      };

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(String attributeId) {
    this.attributeId = attributeId;
  }

  public String getReviewTitle() {
    return reviewTitle;
  }

  public void setReviewTitle(String reviewTitle) {
    this.reviewTitle = reviewTitle;
  }

  public String getReviewDescription() {
    return reviewDescription;
  }

  public void setReviewDescription(String reviewDescription) {
    this.reviewDescription = reviewDescription;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public ArrayList<String> getImages() {
    return images;
  }

  public void setImages(ArrayList<String> images) {
    this.images = images;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public int getReviewType() {
    return reviewType;
  }

  public void setReviewType(int reviewType) {
    this.reviewType = reviewType;
  }

  public String getSellerReview() {
    return sellerReview;
  }

  public String getSellerId() {
    return sellerId;
  }

  public void setSellerId(String sellerId) {
    this.sellerId = sellerId;
  }

  public void setSellerReview(String sellerReview) {
    this.sellerReview = sellerReview;
  }
}
