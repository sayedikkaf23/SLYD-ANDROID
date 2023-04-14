package com.kotlintestgradle.model.orderdetails.pharmacy;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderStatus implements Parcelable {

  private String productOrderId;
  private String storeOrderId;
  private int productStatus;
  private double lat=0.0,longi=0.0;
  private  int heading=0;

  public OrderStatus(String productOrderId, String storeOrderId, int productStatus,double lat,double longi,int heading) {
    this.productOrderId = productOrderId;
    this.storeOrderId = storeOrderId;
    this.productStatus = productStatus;
    this.lat = lat;
    this.longi = longi;
    this.heading= heading;
  }

  protected OrderStatus(Parcel in) {
    productOrderId = in.readString();
    storeOrderId = in.readString();
    lat = in.readDouble();
    longi = in.readDouble();
    heading= in.readInt();
    productStatus = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(productOrderId);
    dest.writeString(storeOrderId);
    dest.writeInt(productStatus);
    dest.writeInt(heading);
    dest.writeDouble(lat);
    dest.writeDouble(longi);
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLongi() {
    return longi;
  }

  public void setLongi(double longi) {
    this.longi = longi;
  }

  public int getHeading() {
    return heading;
  }

  public void setHeading(int heading) {
    this.heading = heading;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderStatus> CREATOR = new Creator<OrderStatus>() {
    @Override
    public OrderStatus createFromParcel(Parcel in) {
      return new OrderStatus(in);
    }

    @Override
    public OrderStatus[] newArray(int size) {
      return new OrderStatus[size];
    }
  };

  public String getProductOrderId() {
    return productOrderId;
  }

  public void setProductOrderId(String productOrderId) {
    this.productOrderId = productOrderId;
  }

  public int getProductStatus() {
    return productStatus;
  }

  public void setProductStatus(int productStatus) {
    this.productStatus = productStatus;
  }

  public String getStoreOrderId() {
    return storeOrderId;
  }

  public void setStoreOrderId(String storeOrderId) {
    this.storeOrderId = storeOrderId;
  }


}
