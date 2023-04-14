package com.kotlintestgradle.remote.model.response.ecom.placesuggestion;

import android.os.Parcel;
import android.os.Parcelable;

public class PlacesDetails implements Parcelable {
  private String fullAddress;
  private String placeId;
  private double lat;
  private double lang;

  public PlacesDetails(String fullAddress, String placeId, double lat, double lang) {
    this.fullAddress = fullAddress;
    this.placeId = placeId;
    this.lat = lat;
    this.lang = lang;
  }

  protected PlacesDetails(Parcel in) {
    fullAddress = in.readString();
    placeId = in.readString();
    lat = in.readDouble();
    lang = in.readDouble();
  }

  public static final Creator<PlacesDetails> CREATOR = new Creator<PlacesDetails>() {
    @Override
    public PlacesDetails createFromParcel(Parcel in) {
      return new PlacesDetails(in);
    }

    @Override
    public PlacesDetails[] newArray(int size) {
      return new PlacesDetails[size];
    }
  };

  public String getFullAddress() {
    return fullAddress;
  }

  public void setFullAddress(String fullAddress) {
    this.fullAddress = fullAddress;
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLang() {
    return lang;
  }

  public void setLang(double lang) {
    this.lang = lang;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(fullAddress);
    parcel.writeString(placeId);
    parcel.writeDouble(lat);
    parcel.writeDouble(lang);
  }
}
