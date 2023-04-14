package com.kotlintestgradle.model.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderHistoryPlanData implements Parcelable {
  private String appCommissionTypeText;

  private String appCommission;

  private String name;

  private String appCommissionType;

  public OrderHistoryPlanData(String appCommissionTypeText, String appCommission,
      String name, String appCommissionType) {
    this.appCommissionTypeText = appCommissionTypeText;
    this.appCommission = appCommission;
    this.name = name;
    this.appCommissionType = appCommissionType;
  }

  protected OrderHistoryPlanData(Parcel in) {
    appCommissionTypeText = in.readString();
    appCommission = in.readString();
    name = in.readString();
    appCommissionType = in.readString();
  }

  public static final Creator<OrderHistoryPlanData> CREATOR = new Creator<OrderHistoryPlanData>() {
    @Override
    public OrderHistoryPlanData createFromParcel(Parcel in) {
      return new OrderHistoryPlanData(in);
    }

    @Override
    public OrderHistoryPlanData[] newArray(int size) {
      return new OrderHistoryPlanData[size];
    }
  };

  public String getAppCommissionTypeText() {
    return appCommissionTypeText;
  }

  public void setAppCommissionTypeText(String appCommissionTypeText) {
    this.appCommissionTypeText = appCommissionTypeText;
  }

  public String getAppCommission() {
    return appCommission;
  }

  public void setAppCommission(String appCommission) {
    this.appCommission = appCommission;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAppCommissionType() {
    return appCommissionType;
  }

  public void setAppCommissionType(String appCommissionType) {
    this.appCommissionType = appCommissionType;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(appCommissionTypeText);
    dest.writeString(appCommission);
    dest.writeString(name);
    dest.writeString(appCommissionType);
  }
}
