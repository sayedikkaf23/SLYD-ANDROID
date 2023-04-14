package com.kotlintestgradle.remote.model.response.ecom.productCategory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import com.kotlintestgradle.remote.model.response.ecom.common.CategoryDetailsModel;
import java.util.ArrayList;

public class CategoryDetail implements ValidItem,Parcelable{

  @SerializedName("data")
  @Expose
  private ArrayList<CategoryDetailsModel> data;

  @SerializedName("message")
  @Expose
  private String message;

  protected CategoryDetail(Parcel in) {
    data = in.createTypedArrayList(CategoryDetailsModel.CREATOR);
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(data);
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CategoryDetail> CREATOR = new Creator<CategoryDetail>() {
    @Override
    public CategoryDetail createFromParcel(Parcel in) {
      return new CategoryDetail(in);
    }

    @Override
    public CategoryDetail[] newArray(int size) {
      return new CategoryDetail[size];
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

  @Override
  public boolean isValid() {
    return true;
  }
}
