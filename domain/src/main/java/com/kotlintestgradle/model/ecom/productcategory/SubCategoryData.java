package com.kotlintestgradle.model.ecom.productcategory;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class SubCategoryData implements Parcelable {
  public static final Creator<SubCategoryData> CREATOR = new Creator<SubCategoryData>() {
    @Override
    public SubCategoryData createFromParcel(Parcel in) {
      return new SubCategoryData(in);
    }

    @Override
    public SubCategoryData[] newArray(int size) {
      return new SubCategoryData[size];
    }
  };
  private ArrayList<SubSubCategoryData> subSubCategory;
  private String imageUrl;
  private String id;
  private String subCategoryName;
  private boolean isClicked = false;
  private String subSubCatName;
   private int subCatCount;
  public SubCategoryData(int subCatCount, String imageUrl, String id,
      String subCategoryName, String subSubCatName) {
    this.subCatCount=subCatCount;
    this.imageUrl = imageUrl;
    this.id = id;
    this.subCategoryName = subCategoryName;
    this.subSubCatName = subSubCatName;
  }

  protected SubCategoryData(Parcel in) {
    imageUrl = in.readString();
    id = in.readString();
    subCatCount=in.readInt();
    subCategoryName = in.readString();
    subSubCategory = new ArrayList<>();
    in.readTypedList(subSubCategory, SubSubCategoryData.CREATOR);
  }

  public String getSubSubCatName() {
    return subSubCatName;
  }

  public void setSubSubCatName(String subSubCatName) {
    this.subSubCatName = subSubCatName;
  }

  public boolean isClicked() {
    return isClicked;
  }

  public void setClicked(boolean clicked) {
    isClicked = clicked;
  }

  public ArrayList<SubSubCategoryData> getSubSubCategory() {
    return subSubCategory;
  }

  public void setSubSubCategory(ArrayList<SubSubCategoryData> subSubCategory) {
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
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(imageUrl);
    dest.writeString(id);
    dest.writeInt(subCatCount);
    dest.writeString(subCategoryName);
    dest.writeTypedList(subSubCategory);
  }
}
