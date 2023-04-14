package com.kotlintestgradle.data.mapper;


import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.tracking.TrackingData;
import com.kotlintestgradle.model.tracking.TrackingItemData;
import com.kotlintestgradle.remote.model.response.tracking.TrackingItemDetails;
import com.kotlintestgradle.remote.model.response.tracking.TrackingListOrderStatusData;

import java.util.ArrayList;

public class TrackingMapper {
  public TrackingData mapper(
      TrackingListOrderStatusData trackingListData) {
    TrackingData trackingData = null;
    if (trackingListData != null) {
      trackingData = new TrackingData(convertToTrackingItemData(trackingListData.getOrderStatus()));
    }
    return trackingData;
  }

  private ArrayList<TrackingItemData> convertToTrackingItemData(
      ArrayList<TrackingItemDetails> trackingItemDetailsArrayList) {
    ArrayList<TrackingItemData> trackingItemData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(trackingItemDetailsArrayList)) {
      for (TrackingItemDetails itemDetails : trackingItemDetailsArrayList) {
        TrackingItemData itemData = new TrackingItemData(itemDetails.getStatusText(),
            itemDetails.getTime(), itemDetails.getFormatedDate(), itemDetails.getStatus());
        trackingItemData.add(itemData);
      }
    }
    return trackingItemData;
  }
}
