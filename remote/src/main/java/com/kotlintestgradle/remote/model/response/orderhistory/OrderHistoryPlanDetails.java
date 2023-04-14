package com.kotlintestgradle.remote.model.response.orderhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kotlintestgradle.remote.core.ValidItem;

public class OrderHistoryPlanDetails implements Parcelable, ValidItem {

  @Expose
  @SerializedName("appCommissionTypeText")
  private String appCommissionTypeText;
  @Expose
  @SerializedName("appCommission")
  private String appCommission;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("appCommissionType")
  private String appCommissionType;

  public OrderHistoryPlanDetails(String appCommissionTypeText, String appCommission,
      String name, String appCommissionType) {
    this.appCommissionTypeText = appCommissionTypeText;
    this.appCommission = appCommission;
    this.name = name;
    this.appCommissionType = appCommissionType;
  }

  protected OrderHistoryPlanDetails(Parcel in) {
    appCommissionTypeText = in.readString();
    appCommission = in.readString();
    name = in.readString();
    appCommissionType = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(appCommissionTypeText);
    dest.writeString(appCommission);
    dest.writeString(name);
    dest.writeString(appCommissionType);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<OrderHistoryPlanDetails> CREATOR =
      new Creator<OrderHistoryPlanDetails>() {
        @Override
        public OrderHistoryPlanDetails createFromParcel(Parcel in) {
          return new OrderHistoryPlanDetails(in);
        }

        @Override
        public OrderHistoryPlanDetails[] newArray(int size) {
          return new OrderHistoryPlanDetails[size];
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
  public boolean isValid() {
    return true;
  }
}
