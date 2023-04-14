package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ImagesDetails implements ValidItem, Parcelable {

  @SerializedName("imageText")
  @Expose
  private String imageText;

  @SerializedName("image")
  @Expose
  private String image;

  @SerializedName("thumbnail")
  @Expose
  private String thumbnail;

  @SerializedName("mobile")
  @Expose
  private String mobile;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("keyword")
  @Expose
  private String keyword;

  @SerializedName("large")
  @Expose
  private String large;

  @SerializedName("extraLarge")
  @Expose
  private String extraLarge;

  @SerializedName("small")
  @Expose
  private String small;

  @SerializedName("altText")
  @Expose
  private String altText;

  @SerializedName("medium")
  @Expose
  private String medium;

  public ImagesDetails(String imageText, String image, String thumbnail, String mobile,
      String description, String title, String keyword) {
    this.imageText = imageText;
    this.image = image;
    this.thumbnail = thumbnail;
    this.mobile = mobile;
    this.description = description;
    this.title = title;
    this.keyword = keyword;
  }

  public ImagesDetails(String imageText, String large, String extraLarge, String small,
      String altText, String medium) {
    this.imageText = imageText;
    this.large = large;
    this.extraLarge = extraLarge;
    this.small = small;
    this.altText = altText;
    this.medium = medium;
  }

  public ImagesDetails(String image) {
    this.image = image;
  }

  protected ImagesDetails(Parcel in) {
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

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(imageText);
    dest.writeString(image);
    dest.writeString(thumbnail);
    dest.writeString(mobile);
    dest.writeString(description);
    dest.writeString(title);
    dest.writeString(keyword);
    dest.writeString(large);
    dest.writeString(extraLarge);
    dest.writeString(small);
    dest.writeString(altText);
    dest.writeString(medium);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ImagesDetails> CREATOR = new Creator<ImagesDetails>() {
    @Override
    public ImagesDetails createFromParcel(Parcel in) {
      return new ImagesDetails(in);
    }

    @Override
    public ImagesDetails[] newArray(int size) {
      return new ImagesDetails[size];
    }
  };

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

  public String getLarge() {
    return large;
  }

  public void setLarge(String large) {
    this.large = large;
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
  public boolean isValid() {
    return true;
  }

}
