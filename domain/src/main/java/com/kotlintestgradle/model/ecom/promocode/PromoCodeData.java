package com.kotlintestgradle.model.ecom.promocode;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class PromoCodeData implements Parcelable {
  private String mStartTime;
  private String code;
  private String promo_id;
  private String mEndTime;
  private ArrayList<PromoCodeDes> description;
  private String title;

  public PromoCodeData(String startTime, String code, String promo_id,String endTime,
      ArrayList<PromoCodeDes> description, String title) {
    this.mStartTime = startTime;
    this.code = code;
    this.promo_id=promo_id;
    this.mEndTime = endTime;
    this.description = description;
    this.title = title;
  }

  protected PromoCodeData(Parcel in) {
    mStartTime = in.readString();
    code = in.readString();
    promo_id = in.readString();
    mEndTime = in.readString();
    title = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mStartTime);
    dest.writeString(code);
    dest.writeString(promo_id);
    dest.writeString(mEndTime);
    dest.writeString(title);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PromoCodeData> CREATOR = new Creator<PromoCodeData>() {
    @Override
    public PromoCodeData createFromParcel(Parcel in) {
      return new PromoCodeData(in);
    }

    @Override
    public PromoCodeData[] newArray(int size) {
      return new PromoCodeData[size];
    }
  };

  public String getStartTime() {
    return mStartTime;
  }

  public void setStartTime(String startTime) {
    this.mStartTime = startTime;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getEndTime() {
    return mEndTime;
  }

  public void setEndTime(String endTime) {
    this.mEndTime = endTime;
  }

  public ArrayList<PromoCodeDes> getDescription() {
    return description;
  }

  public void setDescription(ArrayList<PromoCodeDes> description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPromo_id() {
    return promo_id;
  }

  public void setPromo_id(String promo_id) {
    this.promo_id = promo_id;
  }
}
