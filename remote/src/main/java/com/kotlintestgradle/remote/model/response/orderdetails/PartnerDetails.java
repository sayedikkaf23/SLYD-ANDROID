package com.kotlintestgradle.remote.model.response.orderdetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class PartnerDetails implements Parcelable, ValidItem {
  @Expose
  @SerializedName("id")
  private String id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("trackingId")
  private String trackingId;

  public PartnerDetails(String id, String name, String trackingId) {
    this.id = id;
    this.name = name;
    this.trackingId = trackingId;
  }

  protected PartnerDetails(Parcel in) {
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

  public static final Creator<PartnerDetails> CREATOR = new Creator<PartnerDetails>() {
    @Override
    public PartnerDetails createFromParcel(Parcel in) {
      return new PartnerDetails(in);
    }

    @Override
    public PartnerDetails[] newArray(int size) {
      return new PartnerDetails[size];
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

  @Override
  public boolean isValid() {
    return true;
  }
}
