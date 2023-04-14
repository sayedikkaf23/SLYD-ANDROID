package com.kotlintestgradle.remote.model.response.ecom.productCategory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class SubCategoryDetails implements ValidItem, Parcelable {

  @SerializedName("subSubCategory")
  @Expose
  private ArrayList<SubSubCategoryDetails> subSubCategory;

  @SerializedName("imageUrl")
  @Expose
  private String imageUrl;

  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("subCategoryName")
  @Expose
  private String subCategoryName;
  @SerializedName("subCatCount")
  @Expose
  private int subCatCount;

  @SerializedName("subSubCategoryName")
  @Expose
  private String subSubCategoryName;

  public SubCategoryDetails(ArrayList<SubSubCategoryDetails> subSubCategory, String imageUrl,
      String id, String subCategoryName) {
    this.subSubCategory = subSubCategory;
    this.imageUrl = imageUrl;
    this.id = id;
    this.subCategoryName = subCategoryName;
  }

  protected SubCategoryDetails(Parcel in) {
    imageUrl = in.readString();
    id = in.readString();
    subCategoryName = in.readString();
    subSubCategoryName = in.readString();
    subCatCount=in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(imageUrl);
    dest.writeString(id);
    dest.writeString(subCategoryName);
    dest.writeString(subSubCategoryName);
    dest.writeInt(subCatCount);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SubCategoryDetails> CREATOR = new Creator<SubCategoryDetails>() {
    @Override
    public SubCategoryDetails createFromParcel(Parcel in) {
      return new SubCategoryDetails(in);
    }

    @Override
    public SubCategoryDetails[] newArray(int size) {
      return new SubCategoryDetails[size];
    }
  };

  public String getSubSubCategoryName() {
    return subSubCategoryName;
  }

  public void setSubSubCategoryName(String subSubCategoryName) {
    this.subSubCategoryName = subSubCategoryName;
  }

  public ArrayList<SubSubCategoryDetails> getSubSubCategory() {
    return subSubCategory;
  }

  public void setSubSubCategory(ArrayList<SubSubCategoryDetails> subSubCategory) {
    this.subSubCategory = subSubCategory;
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

  public String getSubCategoryName() {
    return subCategoryName;
  }

  public void setSubCategoryName(String subCategoryName) {
    this.subCategoryName = subCategoryName;
  }

  public int getSubCatCount() {
    return subCatCount;
  }

  public void setSubCatCount(int subCatCount) {
    this.subCatCount = subCatCount;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
