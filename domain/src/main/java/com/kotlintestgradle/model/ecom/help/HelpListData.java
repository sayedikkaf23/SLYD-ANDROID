package com.kotlintestgradle.model.ecom.help;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class HelpListData implements Parcelable {
  private ArrayList<HelpItemData> data;
  private String message;

  public HelpListData(
      ArrayList<HelpItemData> data, String message) {
    this.data = data;
    this.message = message;
  }

  protected HelpListData(Parcel in) {
    data = in.createTypedArrayList(HelpItemData.CREATOR);
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

  public static final Creator<HelpListData> CREATOR = new Creator<HelpListData>() {
    @Override
    public HelpListData createFromParcel(Parcel in) {
      return new HelpListData(in);
    }

    @Override
    public HelpListData[] newArray(int size) {
      return new HelpListData[size];
    }
  };

  public ArrayList<HelpItemData> getData() {
    return data;
  }

  public void setData(ArrayList<HelpItemData> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
