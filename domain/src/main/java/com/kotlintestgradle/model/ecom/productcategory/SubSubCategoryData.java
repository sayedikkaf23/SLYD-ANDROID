package com.kotlintestgradle.model.ecom.productcategory;

import android.os.Parcel;
import android.os.Parcelable;

public class SubSubCategoryData implements Parcelable {
  public static final Creator<SubSubCategoryData> CREATOR = new Creator<SubSubCategoryData>() {
    @Override
    public SubSubCategoryData createFromParcel(Parcel in) {
      return new SubSubCategoryData(in);
    }

    @Override
    public SubSubCategoryData[] newArray(int size) {
      return new SubSubCategoryData[size];
    }
  };
  private String subSubCategoryName;
  private String imageUrl;
  private String id;

  public SubSubCategoryData(String subSubCategoryName, String imageUrl, String id) {
    this.subSubCategoryName = subSubCategoryName;
    this.imageUrl = imageUrl;
    this.id = id;
  }

  protected SubSubCategoryData(Parcel in) {
    subSubCategoryName = in.readString();
    imageUrl = in.readString();
    id = in.readString();
  }

  public String getSubSubCategoryName() {
    return subSubCategoryName;
  }

  public void setSubSubCategoryName(String subSubCategoryName) {
    this.subSubCategoryName = subSubCategoryName;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(subSubCategoryName);
    dest.writeString(imageUrl);
    dest.writeString(id);
  }
}
