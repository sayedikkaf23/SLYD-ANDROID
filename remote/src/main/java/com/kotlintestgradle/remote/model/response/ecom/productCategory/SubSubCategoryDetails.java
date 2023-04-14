package com.kotlintestgradle.remote.model.response.ecom.productCategory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class SubSubCategoryDetails implements ValidItem, Parcelable {

  @SerializedName("subSubCategoryName")
  @Expose
  private String subSubCategoryName;

  @SerializedName("imageUrl")
  @Expose
  private String imageUrl;

  @SerializedName("id")
  @Expose
  private String id;

  public SubSubCategoryDetails(String subSubCategoryName, String imageUrl, String id) {
    this.subSubCategoryName = subSubCategoryName;
    this.imageUrl = imageUrl;
    this.id = id;
  }

  protected SubSubCategoryDetails(Parcel in) {
    subSubCategoryName = in.readString();
    imageUrl = in.readString();
    id = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(subSubCategoryName);
    dest.writeString(imageUrl);
    dest.writeString(id);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SubSubCategoryDetails> CREATOR =
      new Creator<SubSubCategoryDetails>() {
        @Override
        public SubSubCategoryDetails createFromParcel(Parcel in) {
          return new SubSubCategoryDetails(in);
        }

        @Override
        public SubSubCategoryDetails[] newArray(int size) {
          return new SubSubCategoryDetails[size];
        }
      };

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
  public boolean isValid() {
    return true;
  }
}
