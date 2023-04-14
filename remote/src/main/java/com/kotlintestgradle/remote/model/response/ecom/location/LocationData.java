package com.kotlintestgradle.remote.model.response.ecom.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class LocationData implements Parcelable, ValidItem
{
  @Expose
  @SerializedName("lat")
  private double latitude;

  @Expose
  @SerializedName("long")
  private double longitude;

  public LocationData(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  protected LocationData(Parcel in) {
    latitude = in.readDouble();
    longitude = in.readDouble();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(latitude);
    dest.writeDouble(longitude);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
    @Override
    public LocationData createFromParcel(Parcel in) {
      return new LocationData(in);
    }

    @Override
    public LocationData[] newArray(int size) {
      return new LocationData[size];
    }
  };

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
