package com.kotlintestgradle.remote.model.response.ecom.shoppinglistnames;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class MyListNameItemDetails implements Parcelable, ValidItem {
  @Expose
  @SerializedName("title")
  private String title;

  @Expose
  @SerializedName("image")
  private String image;

  @Expose
  @SerializedName("shoppingListId")
  private String shoppingListId;

  @Expose
  @SerializedName("isAdded")
  private boolean isAdded;

  public MyListNameItemDetails(String title, String image, String shoppingListId) {
    this.title = title;
    this.image = image;
    this.shoppingListId = shoppingListId;
  }

  protected MyListNameItemDetails(Parcel in) {
    title = in.readString();
    image = in.readString();
    shoppingListId = in.readString();
    isAdded = in.readByte() != 0;
  }

  public static final Creator<MyListNameItemDetails> CREATOR =
      new Creator<MyListNameItemDetails>() {
        @Override
        public MyListNameItemDetails createFromParcel(Parcel in) {
          return new MyListNameItemDetails(in);
        }

        @Override
        public MyListNameItemDetails[] newArray(int size) {
          return new MyListNameItemDetails[size];
        }
      };

  public boolean isAdded() {
    return isAdded;
  }

  public void setAdded(boolean added) {
    isAdded = added;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getShoppingListId() {
    return shoppingListId;
  }

  public void setShoppingListId(String shoppingListId) {
    this.shoppingListId = shoppingListId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(title);
    parcel.writeString(image);
    parcel.writeString(shoppingListId);
    parcel.writeByte((byte) (isAdded ? 1 : 0));
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
