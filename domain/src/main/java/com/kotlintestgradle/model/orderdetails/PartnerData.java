package com.kotlintestgradle.model.orderdetails;

import android.os.Parcel;
import android.os.Parcelable;

public class PartnerData implements Parcelable {

  private String id;

  private String name;


  private String trackingId;

  public PartnerData(String id, String name, String trackingId) {
    this.id = id;
    this.name = name;
    this.trackingId = trackingId;
  }

  protected PartnerData(Parcel in) {
    id = in.readString();
    name = in.readString();
    trackingId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
    dest.writeString(trackingId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PartnerData> CREATOR = new Creator<PartnerData>() {
    @Override
    public PartnerData createFromParcel(Parcel in) {
      return new PartnerData(in);
    }

    @Override
    public PartnerData[] newArray(int size) {
      return new PartnerData[size];
    }
  };

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }
}
