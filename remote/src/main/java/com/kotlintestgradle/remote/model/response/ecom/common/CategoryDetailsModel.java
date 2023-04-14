package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.productCategory.SubCategoryDetails;
import java.util.ArrayList;

public class CategoryDetailsModel implements ValidItem, Parcelable {

  @SerializedName("websiteBannerImageUrl")
  @Expose
  private String websiteBannerImageUrl;

  @SerializedName("imageUrl")
  @Expose
  private String imageUrl;

  @SerializedName("catName")
  @Expose
  private String catName;

  @SerializedName("bannerImageUrl")
  @Expose
  private String bannerImageUrl;

  @SerializedName("websiteImageUrl")
  @Expose
  private String websiteImageUrl;

  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("subCategory")
  @Expose
  private ArrayList<SubCategoryDetails> subCategory;
  @SerializedName("penCount")
  @Expose
  private int penCount;

  public CategoryDetailsModel(String websiteBannerImageUrl, String imageUrl, String catName,
      String bannerImageUrl, String websiteImageUrl, String id) {
    this.websiteBannerImageUrl = websiteBannerImageUrl;
    this.imageUrl = imageUrl;
    this.catName = catName;
    this.bannerImageUrl = bannerImageUrl;
    this.websiteImageUrl = websiteImageUrl;
    this.id = id;
  }

  public CategoryDetailsModel(String websiteBannerImageUrl, String imageUrl, String catName,
      String bannerImageUrl, String websiteImageUrl, String id,
      ArrayList<SubCategoryDetails> subCategory) {
    this.websiteBannerImageUrl = websiteBannerImageUrl;
    this.imageUrl = imageUrl;
    this.catName = catName;
    this.bannerImageUrl = bannerImageUrl;
    this.websiteImageUrl = websiteImageUrl;
    this.id = id;
    this.subCategory = subCategory;
  }

  protected CategoryDetailsModel(Parcel in) {
    websiteBannerImageUrl = in.readString();
    imageUrl = in.readString();
    catName = in.readString();
    bannerImageUrl = in.readString();
    websiteImageUrl = in.readString();
    id = in.readString();
    penCount = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(websiteBannerImageUrl);
    dest.writeString(imageUrl);
    dest.writeString(catName);
    dest.writeString(bannerImageUrl);
    dest.writeString(websiteImageUrl);
    dest.writeString(id);
    dest.writeInt(penCount);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CategoryDetailsModel> CREATOR = new Creator<CategoryDetailsModel>() {
    @Override
    public CategoryDetailsModel createFromParcel(Parcel in) {
      return new CategoryDetailsModel(in);
    }

    @Override
    public CategoryDetailsModel[] newArray(int size) {
      return new CategoryDetailsModel[size];
    }
  };

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public ArrayList<SubCategoryDetails> getSubCategory() {
    return subCategory;
  }

  public void setSubCategory(ArrayList<SubCategoryDetails> subCategory) {
    this.subCategory = subCategory;
  }

  public String getWebsiteBannerImageUrl() {
    return websiteBannerImageUrl;
  }

  public void setWebsiteBannerImageUrl(String websiteBannerImageUrl) {
    this.websiteBannerImageUrl = websiteBannerImageUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getCatName() {
    return catName;
  }

  public void setCatName(String catName) {
    this.catName = catName;
  }

  public String getBannerImageUrl() {
    return bannerImageUrl;
  }

  public void setBannerImageUrl(String bannerImageUrl) {
    this.bannerImageUrl = bannerImageUrl;
  }

  public String getWebsiteImageUrl() {
    return websiteImageUrl;
  }

  public void setWebsiteImageUrl(String websiteImageUrl) {
    this.websiteImageUrl = websiteImageUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
