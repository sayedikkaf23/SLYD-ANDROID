package com.kotlintestgradle.remote.model.response.ecom.pdp;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ColorsDetails implements ValidItem, Parcelable {

  @SerializedName("image")
  @Expose
  private String image;

  @SerializedName("isPrimary")
  @Expose
  private String isPrimary;

  @SerializedName("childProductId")
  @Expose
  private String childProductId;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("rgb")
  @Expose
  private String rgb;

  public ColorsDetails(String image, String isPrimary, String childProductId, String name,
      String rgb) {
    this.image = image;
    this.isPrimary = isPrimary;
    this.childProductId = childProductId;
    this.name = name;
    this.rgb = rgb;
  }

  protected ColorsDetails(Parcel in) {
    image = in.readString();
    isPrimary = in.readString();
    childProductId = in.readString();
    name = in.readString();
    rgb = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(image);
    dest.writeString(isPrimary);
    dest.writeString(childProductId);
    dest.writeString(name);
    dest.writeString(rgb);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ColorsDetails> CREATOR = new Creator<ColorsDetails>() {
    @Override
    public ColorsDetails createFromParcel(Parcel in) {
      return new ColorsDetails(in);
    }

    @Override
    public ColorsDetails[] newArray(int size) {
      return new ColorsDetails[size];
    }
  };

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(String isPrimary) {
    this.isPrimary = isPrimary;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRgb() {
    return rgb;
  }

  public void setRgb(String rgb) {
    this.rgb = rgb;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
