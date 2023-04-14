package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.pdp.substitute.SubstitutesData;

public class ProductDetails implements ValidItem, Parcelable {
  @SerializedName("review")
  @Expose
  private ReviewDetails review;

  @SerializedName("productData")
  @Expose
  private ProductDataDetails productData;

  @Expose
  @SerializedName("substituteData")
  private SubstitutesData substituteData;

  public ProductDetails(ReviewDetails review, ProductDataDetails productData) {
    this.review = review;
    this.productData = productData;
  }

  public ProductDetails(ReviewDetails review, ProductDataDetails productData, SubstitutesData substituteData) {
    this.review = review;
    this.productData = productData;
    this.substituteData = substituteData;
  }

  protected ProductDetails(Parcel in) {
    review = in.readParcelable(ReviewDetails.class.getClassLoader());
    productData = in.readParcelable(ProductDataDetails.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(review, flags);
    dest.writeParcelable(productData, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
    @Override
    public ProductDetails createFromParcel(Parcel in) {
      return new ProductDetails(in);
    }

    @Override
    public ProductDetails[] newArray(int size) {
      return new ProductDetails[size];
    }
  };

  public ReviewDetails getReview() {
    return review;
  }

  public void setReview(ReviewDetails review) {
    this.review = review;
  }

  public ProductDataDetails getProductData() {
    return productData;
  }

  public void setProductData(ProductDataDetails productData) {
    this.productData = productData;
  }

  public SubstitutesData getSubstituteData() {
    return substituteData;
  }

  public void setSubstituteData(SubstitutesData substituteData) {
    this.substituteData = substituteData;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
