package com.kotlintestgradle.remote.model.response.ecom.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class SizeChart implements Parcelable, ValidItem {
  @SerializedName("size")
  @Expose
  private ArrayList<String> size;
  @SerializedName("name")
  @Expose
  private String name;

  protected SizeChart(Parcel in) {
    size = in.createStringArrayList();
    name = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringList(size);
    dest.writeString(name);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<SizeChart> CREATOR = new Creator<SizeChart>() {
    @Override
    public SizeChart createFromParcel(Parcel in) {
      return new SizeChart(in);
    }

    @Override
    public SizeChart[] newArray(int size) {
      return new SizeChart[size];
    }
  };

  public ArrayList<String> getSize() {
    return size;
  }

  public void setSize(ArrayList<String> size) {
    this.size = size;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
