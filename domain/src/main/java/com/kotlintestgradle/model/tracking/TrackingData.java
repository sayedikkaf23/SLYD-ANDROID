package com.kotlintestgradle.model.tracking;

import java.util.ArrayList;

public class TrackingData {
  private ArrayList<TrackingItemData> orderStatus;

  public TrackingData(
      ArrayList<TrackingItemData> orderStatus) {
    this.orderStatus = orderStatus;
  }

  public ArrayList<TrackingItemData> getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(
      ArrayList<TrackingItemData> orderStatus) {
    this.orderStatus = orderStatus;
  }
}
