package com.kotlintestgradle.remote.model.response.ecom.homeSubCategory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class CategoryList implements Parcelable, ValidItem {

  @SerializedName("subCategory")
  @Expose
  private ArrayList<SubCategoryProductDetails> subCategory;

  @SerializedName("subCategoryName")
  @Expose
  private String subCategoryName;

  @SerializedName("catName")
  @Expose
  private String catName;

  public CategoryList(
      ArrayList<SubCategoryProductDetails> subCategory, String subCategoryName,
      String catName) {
    this.subCategory = subCategory;
    this.subCategoryName = subCategoryName;
    this.catName = catName;
  }

  protected CategoryList(Parcel in) {
    subCategoryName = in.readString();
    catName = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(subCategoryName);
    dest.writeString(catName);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CategoryList> CREATOR = new Creator<CategoryList>() {
    @Override
    public CategoryList createFromParcel(Parcel in) {
      return new CategoryList(in);
    }

    @Override
    public CategoryList[] newArray(int size) {
      return new CategoryList[size];
    }
  };

  public String getCatName() {
    return catName;
  }

  public void setCatName(String catName) {
    this.catName = catName;
  }

  public ArrayList<SubCategoryProductDetails> getSubCategory() {
    return subCategory;
  }

  public void setSubCategory(ArrayList<SubCategoryProductDetails> subCategory) {
    this.subCategory = subCategory;
  }

  public String getSubCategoryName() {
    return subCategoryName;
  }

  public void setSubCategoryName(String subCategoryName) {
    this.subCategoryName = subCategoryName;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
