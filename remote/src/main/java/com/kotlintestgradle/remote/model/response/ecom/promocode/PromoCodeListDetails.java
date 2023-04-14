package com.kotlintestgradle.remote.model.response.ecom.promocode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;
import java.util.ArrayList;

public class PromoCodeListDetails implements Parcelable, ValidItem {

  @SerializedName("data")
  @Expose
  private ArrayList<PromoCodeListData> data;
  @SerializedName("message")
  @Expose
  private String message;

  protected PromoCodeListDetails(Parcel in) {
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

  public static final Creator<PromoCodeListDetails> CREATOR = new Creator<PromoCodeListDetails>() {
    @Override
    public PromoCodeListDetails createFromParcel(Parcel in) {
      return new PromoCodeListDetails(in);
    }

    @Override
    public PromoCodeListDetails[] newArray(int size) {
      return new PromoCodeListDetails[size];
    }
  };

  public ArrayList<PromoCodeListData> getData ()
  {
    return data;
  }

  public void setData (ArrayList<PromoCodeListData> data)
  {
    this.data = data;
  }

  public String getMessage ()
  {
    return message;
  }

  public void setMessage (String message)
  {
    this.message = message;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
