package com.kotlintestgradle.remote.model.response.ecom.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.SubCategoryProductDetails;
import java.util.ArrayList;

public class MyListItemDetails implements Parcelable , ValidItem {

  @Expose
  @SerializedName("title")
  private String title;

  @Expose
  @SerializedName("shoppingId")
  private String shoppingId;

  @Expose
  @SerializedName("penCount")
  private int penCount;

  @Expose
  @SerializedName("backgroundImage")
  private String backgroundImage;

  @Expose
  @SerializedName("products")
  private ArrayList<SubCategoryProductDetails> products;

  public MyListItemDetails(String title, String shoppingId, int penCount,
      String backgroundImage,
      ArrayList<SubCategoryProductDetails> products) {
    this.title = title;
    this.shoppingId = shoppingId;
    this.penCount = penCount;
    this.backgroundImage = backgroundImage;
    this.products = products;
  }

  protected MyListItemDetails(Parcel in) {
    title = in.readString();
    shoppingId = in.readString();
    penCount = in.readInt();
    backgroundImage = in.readString();
    products = in.createTypedArrayList(SubCategoryProductDetails.CREATOR);
  }

  public static final Creator<MyListItemDetails> CREATOR = new Creator<MyListItemDetails>() {
    @Override
    public MyListItemDetails createFromParcel(Parcel in) {
      return new MyListItemDetails(in);
    }

    @Override
    public MyListItemDetails[] newArray(int size) {
      return new MyListItemDetails[size];
    }
  };

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getShoppingId() {
    return shoppingId;
  }

  public void setShoppingId(String shoppingId) {
    this.shoppingId = shoppingId;
  }

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public String getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public ArrayList<SubCategoryProductDetails> getProducts() {
    return products;
  }

  public void setProducts(
      ArrayList<SubCategoryProductDetails> products) {
    this.products = products;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(title);
    parcel.writeString(shoppingId);
    parcel.writeInt(penCount);
    parcel.writeString(backgroundImage);
    parcel.writeTypedList(products);
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
