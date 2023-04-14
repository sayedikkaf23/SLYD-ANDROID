package com.kotlintestgradle.model.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {

  public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
    @Override
    public ImageData createFromParcel(Parcel in) {
      return new ImageData(in);
    }

    @Override
    public ImageData[] newArray(int size) {
      return new ImageData[size];
    }
  };
  private String imageText;
  private String image;
  private String thumbnail;
  private String mobile;
  private String description;
  private String title;
  private String keyword;
  private String large;
  private String extraLarge;
  private String small;
  private String altText;
  private String medium;

  public ImageData() {

  }

  public ImageData(String imageText, String image, String thumbnail, String mobile,
      String description, String title, String keyword) {
    this.imageText = imageText;
    this.image = image;
    this.thumbnail = thumbnail;
    this.mobile = mobile;
    this.description = description;
    this.title = title;
    this.keyword = keyword;
  }

  public ImageData(String imageText, String image, String thumbnail, String mobile, String description,
                   String title, String keyword, String large, String extraLarge, String small,
                   String altText, String medium) {
    this.imageText = imageText;
    this.image = image;
    this.thumbnail = thumbnail;
    this.mobile = mobile;
    this.description = description;
    this.title = title;
    this.keyword = keyword;
    this.large = large;
    this.extraLarge = extraLarge;
    this.small = small;
    this.altText = altText;
    this.medium = medium;
  }

  public ImageData(String image, String thumbnail, String mobile) {
    this.image = image;
    this.thumbnail = thumbnail;
    this.mobile = mobile;
  }

  public ImageData(String large, String extraLarge, String small, String altText,
      String medium) {
    this.large = large;
    this.extraLarge = extraLarge;
    this.small = small;
    this.altText = altText;
    this.medium = medium;
  }

  public ImageData(String medium) {
    this.medium = medium;
    this.small = medium;
  }

  public ImageData(Parcel in) {
    imageText = in.readString();
    image = in.readString();
    thumbnail = in.readString();
    mobile = in.readString();
    description = in.readString();
    title = in.readString();
    keyword = in.readString();
    large = in.readString();
    extraLarge = in.readString();
    small = in.readString();
    altText = in.readString();
    medium = in.readString();
  }

  public String getLarge() {
    return large;
  }

  public void setLarge(String large) {
    this.large = large;
  }

  public String getExtraLarge() {
    return extraLarge;
  }

  public void setExtraLarge(String extraLarge) {
    this.extraLarge = extraLarge;
  }

  public String getSmall() {
    return small;
  }

  public void setSmall(String small) {
    this.small = small;
  }

  public String getAltText() {
    return altText;
  }

  public void setAltText(String altText) {
    this.altText = altText;
  }

  public String getMedium() {
    return medium;
  }

  public void setMedium(String medium) {
    this.medium = medium;
  }

  public String getImageText() {
    return imageText;
  }

  public void setImageText(String imageText) {
    this.imageText = imageText;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(imageText);
    parcel.writeString(image);
    parcel.writeString(thumbnail);
    parcel.writeString(mobile);
    parcel.writeString(description);
    parcel.writeString(title);
    parcel.writeString(keyword);
    parcel.writeString(large);
    parcel.writeString(extraLarge);
    parcel.writeString(small);
    parcel.writeString(altText);
    parcel.writeString(medium);
  }
}
