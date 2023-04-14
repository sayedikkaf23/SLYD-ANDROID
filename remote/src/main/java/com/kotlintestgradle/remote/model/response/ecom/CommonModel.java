package com.kotlintestgradle.remote.model.response.ecom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class CommonModel implements ValidItem,Parcelable {

  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("shoppingId")
  @Expose
  private String shoppingId;

  public CommonModel(String message) {
    this.message = message;
  }

  public CommonModel(String message, String shoppingId) {
    this.message = message;
    this.shoppingId = shoppingId;
  }

  protected CommonModel(Parcel in) {
    message = in.readString();
    shoppingId = in.readString();
  }

  public static final Creator<CommonModel> CREATOR = new Creator<CommonModel>() {
    @Override
    public CommonModel createFromParcel(Parcel in) {
      return new CommonModel(in);
    }

    @Override
    public CommonModel[] newArray(int size) {
      return new CommonModel[size];
    }
  };

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(message);
    dest.writeString(shoppingId);
  }

  public String getShoppingId() {
    return shoppingId;
  }

  public void setShoppingId(String shoppingId) {
    this.shoppingId = shoppingId;
  }
}
