package com.kotlintestgradle.remote.model.response.help;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class HelpListDetails implements Parcelable, ValidItem {
  @SerializedName("data")
  @Expose
  private ArrayList<HelpItemDetails> data;
  @SerializedName("message")
  @Expose
  private String message;

  public HelpListDetails(
      ArrayList<HelpItemDetails> data, String message) {
    this.data = data;
    this.message = message;
  }

  protected HelpListDetails(Parcel in) {
    message = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(message);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<HelpListDetails> CREATOR = new Creator<HelpListDetails>() {
    @Override
    public HelpListDetails createFromParcel(Parcel in) {
      return new HelpListDetails(in);
    }

    @Override
    public HelpListDetails[] newArray(int size) {
      return new HelpListDetails[size];
    }
  };

  public ArrayList<HelpItemDetails> getData() {
    return data;
  }

  public void setData(ArrayList<HelpItemDetails> data) {
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
