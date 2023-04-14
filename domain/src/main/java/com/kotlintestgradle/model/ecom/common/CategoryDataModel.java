package com.kotlintestgradle.model.ecom.common;

import com.kotlintestgradle.model.ecom.productcategory.SubCategoryData;
import java.util.ArrayList;

public class CategoryDataModel {

  private String websiteBannerImageUrl;

  private String imageUrl;

  private String catName;

  private String bannerImageUrl;

  private String websiteImageUrl;

  private String id;

  private ArrayList<SubCategoryData> subCategory;

  private int penCount;

  public CategoryDataModel(String websiteBannerImageUrl, String imageUrl, String catName,
      String bannerImageUrl, String websiteImageUrl, String id) {
    this.websiteBannerImageUrl = websiteBannerImageUrl;
    this.imageUrl = imageUrl;
    this.catName = catName;
    this.bannerImageUrl = bannerImageUrl;
    this.websiteImageUrl = websiteImageUrl;
    this.id = id;
  }

  public CategoryDataModel(String websiteBannerImageUrl, String imageUrl, String catName,
      String bannerImageUrl, String websiteImageUrl, String id,
      ArrayList<SubCategoryData> subCategory, int penCount) {
    this.websiteBannerImageUrl = websiteBannerImageUrl;
    this.imageUrl = imageUrl;
    this.catName = catName;
    this.bannerImageUrl = bannerImageUrl;
    this.websiteImageUrl = websiteImageUrl;
    this.id = id;
    this.subCategory = subCategory;
    this.penCount = penCount;
  }

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public ArrayList<SubCategoryData> getSubCategory() {
    return subCategory;
  }

  public void setSubCategory(ArrayList<SubCategoryData> subCategory) {
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
}
