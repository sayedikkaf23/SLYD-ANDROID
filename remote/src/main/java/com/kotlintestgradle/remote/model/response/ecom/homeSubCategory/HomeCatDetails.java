package com.kotlintestgradle.remote.model.response.ecom.homeSubCategory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class HomeCatDetails implements Parcelable, ValidItem {

  @SerializedName("categoryData")
  @Expose
  private ArrayList<CategoryList> categoryData;

  @SerializedName("message")
  @Expose
  private String message;

  @SerializedName("catName")
  @Expose
  private String catName;

  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("imageUrl")
  @Expose
  private String imageUrl;

  @SerializedName("bannerImageUrl")
  @Expose
  private String bannerImageUrl;

  @SerializedName("type")
  @Expose
  private int type;

  @SerializedName("seqId")
  @Expose
  private int seqId;

  public HomeCatDetails(
      ArrayList<CategoryList> categoryData, String message, String catName, String id,
      String imageUrl, String bannerImageUrl, int type, int seqId) {
    this.categoryData = categoryData;
    this.message = message;
    this.catName = catName;
    this.id = id;
    this.imageUrl = imageUrl;
    this.bannerImageUrl = bannerImageUrl;
    this.type = type;
    this.seqId = seqId;
  }

  protected HomeCatDetails(Parcel in) {
    categoryData = in.createTypedArrayList(CategoryList.CREATOR);
    message = in.readString();
    catName = in.readString();
    id = in.readString();
    imageUrl = in.readString();
    bannerImageUrl = in.readString();
    type = in.readInt();
    seqId = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(categoryData);
    dest.writeString(message);
    dest.writeString(catName);
    dest.writeString(id);
    dest.writeString(imageUrl);
    dest.writeString(bannerImageUrl);
    dest.writeInt(type);
    dest.writeInt(seqId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<HomeCatDetails> CREATOR = new Creator<HomeCatDetails>() {
    @Override
    public HomeCatDetails createFromParcel(Parcel in) {
      return new HomeCatDetails(in);
    }

    @Override
    public HomeCatDetails[] newArray(int size) {
      return new HomeCatDetails[size];
    }
  };

  public ArrayList<CategoryList> getCategoryData() {
    return categoryData;
  }

  public void setCategoryData(
      ArrayList<CategoryList> categoryData) {
    this.categoryData = categoryData;
  }

  public String getCatName() {
    return catName;
  }

  public void setCatName(String catName) {
    this.catName = catName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getBannerImageUrl() {
    return bannerImageUrl;
  }

  public void setBannerImageUrl(String bannerImageUrl) {
    this.bannerImageUrl = bannerImageUrl;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getSeqId() {
    return seqId;
  }

  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  public ArrayList<CategoryList> getData() {
    return categoryData;
  }

  public void setData(ArrayList<CategoryList> data) {
    this.categoryData = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
