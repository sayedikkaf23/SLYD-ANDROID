package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class CategoryDetails implements ValidItem, Parcelable {

  @SerializedName("data")
  @Expose
  private ArrayList<CategoryDetailsModel> data;

  @SerializedName("message")
  @Expose
  private String message;

  @SerializedName("type")
  @Expose
  private int type;

  public CategoryDetails(ArrayList<CategoryDetailsModel> data, String message, int type) {
    this.data = data;
    this.message = message;
    this.type = type;
  }

  protected CategoryDetails(Parcel in) {
    message = in.readString();
    type = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(message);
    dest.writeInt(type);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CategoryDetails> CREATOR = new Creator<CategoryDetails>() {
    @Override
    public CategoryDetails createFromParcel(Parcel in) {
      return new CategoryDetails(in);
    }

    @Override
    public CategoryDetails[] newArray(int size) {
      return new CategoryDetails[size];
    }
  };

  public ArrayList<CategoryDetailsModel> getData() {
    return data;
  }

  public void setData(ArrayList<CategoryDetailsModel> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
