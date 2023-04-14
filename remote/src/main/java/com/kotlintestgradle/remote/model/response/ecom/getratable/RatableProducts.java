package com.kotlintestgradle.remote.model.response.ecom.getratable;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class RatableProducts implements Parcelable, ValidItem {
  @SerializedName("userReview")
  @Expose
  private UserReview userReview;
  @SerializedName("productName")
  @Expose
  private String productName;
  @SerializedName("isOrder")
  @Expose
  private Boolean isOrder;
  @SerializedName("productId")
  @Expose
  private String productId;
  @SerializedName("image")
  @Expose
  private String image;


  public RatableProducts(String productName, String productId, String image, UserReview userReview, Boolean isOrder) {
    this.productName = productName;
    this.productId = productId;
    this.image = image;
    this.userReview = userReview;
    this.isOrder = isOrder;
  }

  protected RatableProducts(Parcel in) {
    productName = in.readString();
    byte tmpIsOrder = in.readByte();
    isOrder = tmpIsOrder == 0 ? null : tmpIsOrder == 1;
    productId = in.readString();
    image = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(productName);
    dest.writeByte((byte) (isOrder == null ? 0 : isOrder ? 1 : 2));
    dest.writeString(productId);
    dest.writeString(image);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<RatableProducts> CREATOR = new Creator<RatableProducts>() {
    @Override
    public RatableProducts createFromParcel(Parcel in) {
      return new RatableProducts(in);
    }

    @Override
    public RatableProducts[] newArray(int size) {
      return new RatableProducts[size];
    }
  };

  public UserReview getUserReview() {
    return userReview;
  }

  public void setUserReview(UserReview userReview) {
    this.userReview = userReview;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Boolean getOrder() {
    return isOrder;
  }

  public void setOrder(Boolean order) {
    isOrder = order;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }


  @Override
  public boolean isValid() {
    return true;
  }
}
