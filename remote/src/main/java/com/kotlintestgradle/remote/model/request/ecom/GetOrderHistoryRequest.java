package com.kotlintestgradle.remote.model.request.ecom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import static com.kotlintestgradle.remote.RemoteConstants.LIMIT;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_BY;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TIME;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.SEARCH;
import static com.kotlintestgradle.remote.RemoteConstants.SKIP;
import static com.kotlintestgradle.remote.RemoteConstants.STATUS;
import static com.kotlintestgradle.remote.RemoteConstants.STORE_TYPE;

public class GetOrderHistoryRequest implements Parcelable {

  private int limit;
  private int skip;
  private int status;
  private String cityId;
  private int orderType;
  private int storeType;
  private int orderBy;
  private String search;
  private String orderTime;
  public GetOrderHistoryRequest(int limit, int skip, int status, int orderType,
                                int storeType, int orderBy, String search, String orderTime) {
    this.limit = limit;
    this.skip = skip;
    this.status = status;
    this.orderType = orderType;
    this.storeType = storeType;
    this.orderBy = orderBy;
    this.search = search;
    this.orderTime=orderTime;
  }

  protected GetOrderHistoryRequest(Parcel in) {
    limit = in.readInt();
    skip = in.readInt();
    status = in.readInt();
    cityId = in.readString();
    orderType = in.readInt();
    storeType = in.readInt();
    orderBy = in.readInt();
    search = in.readString();
    orderTime = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(limit);
    dest.writeInt(skip);
    dest.writeInt(status);
    dest.writeString(cityId);
    dest.writeInt(orderType);
    dest.writeInt(storeType);
    dest.writeInt(orderBy);
    dest.writeString(search);
    dest.writeString(orderTime);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<GetOrderHistoryRequest> CREATOR =
      new Creator<GetOrderHistoryRequest>() {
        @Override
        public GetOrderHistoryRequest createFromParcel(Parcel in) {
          return new GetOrderHistoryRequest(in);
        }

        @Override
        public GetOrderHistoryRequest[] newArray(int size) {
          return new GetOrderHistoryRequest[size];
        }
      };

  public HashMap<String, Object> getQuery() {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put(LIMIT, limit);
    hashMap.put(SKIP, skip);
    hashMap.put(STATUS, status);
    /*hashMap.put(CITY_ID, cityId);*/
    hashMap.put(ORDER_TYPE, orderType);
    hashMap.put(STORE_TYPE, storeType);
    hashMap.put(ORDER_BY, orderBy);
    hashMap.put(SEARCH, search);
    hashMap.put(ORDER_TIME,orderTime);
    return hashMap;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public int getSkip() {
    return skip;
  }

  public void setSkip(int skip) {
    this.skip = skip;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public int getOrderType() {
    return orderType;
  }

  public void setOrderType(int orderType) {
    this.orderType = orderType;
  }

  public int getStoreType() {
    return storeType;
  }

  public void setStoreType(int storeType) {
    this.storeType = storeType;
  }

  public int getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(int orderBy) {
    this.orderBy = orderBy;
  }

  public String getOrderTime() {
    return search;
  }

  public void setOrderTime(String search) {
    this.search = search;
  }
}
