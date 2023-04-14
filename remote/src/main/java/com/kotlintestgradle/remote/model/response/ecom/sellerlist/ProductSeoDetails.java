package com.kotlintestgradle.remote.model.response.ecom.sellerlist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class ProductSeoDetails implements Parcelable, ValidItem {
  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("metatags")
  @Expose
  private String metatags;

  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("slug")
  @Expose
  private String slug;

  public ProductSeoDetails(String description, String metatags, String title, String slug) {
    this.description = description;
    this.metatags = metatags;
    this.title = title;
    this.slug = slug;
  }

  protected ProductSeoDetails(Parcel in) {
    description = in.readString();
    metatags = in.readString();
    title = in.readString();
    slug = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(description);
    dest.writeString(metatags);
    dest.writeString(title);
    dest.writeString(slug);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<ProductSeoDetails> CREATOR = new Creator<ProductSeoDetails>() {
    @Override
    public ProductSeoDetails createFromParcel(Parcel in) {
      return new ProductSeoDetails(in);
    }

    @Override
    public ProductSeoDetails[] newArray(int size) {
      return new ProductSeoDetails[size];
    }
  };

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMetatags() {
    return metatags;
  }

  public void setMetatags(String metatags) {
    this.metatags = metatags;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
