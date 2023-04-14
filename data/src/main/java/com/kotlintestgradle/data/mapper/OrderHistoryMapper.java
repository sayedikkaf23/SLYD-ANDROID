package com.kotlintestgradle.data.mapper;


import com.customer.domain.model.orderdetails.pharmacy.StoreRatingData;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.location.LocationData;
import com.kotlintestgradle.model.orderdetails.pharmacy.DriverDetailsData;
import com.kotlintestgradle.model.orderhistory.OrderHistOrderData;
import com.kotlintestgradle.model.orderhistory.OrderHistStoreOrderData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryItemData;
import com.kotlintestgradle.model.orderhistory.OrderHistorySellerLogoData;
import com.kotlintestgradle.model.orderhistory.OrdersHistorySellerData;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.DriverDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Location;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistOrderDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistStoreOrderDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryItemDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistorySellerLogoDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrdersHistorySellerDetails;

import java.util.ArrayList;

public class OrderHistoryMapper {
  public OrderHistoryData mapper(OrderHistoryDetails orderHistoryDetails) {
    return new OrderHistoryData(orderHistoryDetails.getCount(), orderHistoryDetails.getMessage(),
        convertToOrderHistItemData(orderHistoryDetails.getData()));
  }

  private ArrayList<OrderHistoryItemData> convertToOrderHistItemData(
      ArrayList<OrderHistoryItemDetails> historyItemDetails) {
    ArrayList<OrderHistoryItemData> orderHistoryItemData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(historyItemDetails)) {
      for (OrderHistoryItemDetails itemDetails : historyItemDetails) {
        OrderHistoryItemData itemData = new OrderHistoryItemData(itemDetails.getRequestedFor(),
            itemDetails.getOrderType(), itemDetails.getPaymentTypeText(), itemDetails.getOrderId(),
            CommonMapper.convertToTimeStampData(itemDetails.getTimestamps()),
            itemDetails.getCreatedTimeStamp(),
            itemDetails.getOrderTypeMsg(),
            CommonMapper.convertToAccountingData(itemDetails.getAccounting()),
            itemDetails.getPaymentType(), itemDetails.getRequestedForTimeStamp(),
            null,
            itemDetails.getCustomerId(),
            CommonMapper.convertCustomerData(itemDetails.getCustomerDetails()),
            itemDetails.getPayByWallet(), itemDetails.getStoreType(), itemDetails.getCoupon(),
            itemDetails.getCartId(), convertToStoreOrderData(itemDetails.getStoreOrders()),
            itemDetails.getStoreTypeMsg(), itemDetails.getCreatedDate(),
            convertToOrderData(itemDetails.getOrders()), itemDetails.get_id(),
            null,
            convertToSellerData(itemDetails.getSellers()),
            CommonMapper.convertToStatusData(itemDetails.getStatus())
        );
        orderHistoryItemData.add(itemData);
      }
    }
    return orderHistoryItemData;
  }

  private ArrayList<OrderHistOrderData> convertToOrderData(
      ArrayList<OrderHistOrderDetails> orderDetails) {
    ArrayList<OrderHistOrderData> histOrderData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(orderDetails)) {
      for (OrderHistOrderDetails details : orderDetails) {
        OrderHistOrderData orderData = new OrderHistOrderData(details.getStoreOrderId(),
            details.getProductOrderId());
        histOrderData.add(orderData);
      }
    }
    return histOrderData;
  }

  private ArrayList<OrderHistStoreOrderData> convertToStoreOrderData(
      ArrayList<OrderHistStoreOrderDetails> storeOrderDetails) {
    ArrayList<OrderHistStoreOrderData> histStoreOrderData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(storeOrderDetails)) {
      for (OrderHistStoreOrderDetails details : storeOrderDetails) {
        OrderHistStoreOrderData orderData = new OrderHistStoreOrderData(details.getRequestedFor(),
                details.getOrderType(), details.getPaymentTypeText(), details.getDriverTypeId(),
                CommonMapper.convertToTimeStampData(details.getTimestamps()),
                details.getCreatedTimeStamp(), details.getOrderTypeMsg(),
                CommonMapper.convertToAccountingData(details.getAccounting()), details.getPaymentType(),
                CommonMapper.convertToOrderHistProductData(details.getProducts()),
                details.getRequestedForTimeStamp(),
                details.getAutoDispatch(),
                CommonMapper.convertToPickUpAddressData(details.getDeliveryAddress()),
                details.getStorePhone(), details.getCustomerId(), details.getStoreName(),
                CommonMapper.convertCustomerData(details.getCustomerDetails()),
                details.getFreeDeliveryLimitPerStore(),
                details.getPayByWallet(), details.getStoreType(), details.getCartId(),
                details.getStoreId(), details.getDriverType(), details.getMasterOrderId(),
                details.getStoreTypeMsg(), details.getCreatedDate(),
                details.getStoreOrderId(), details.getAutoAcceptOrders(),
                CommonMapper.convertToPickUpAddressData(details.getPickupAddress()),
                CommonMapper.convertToStoreLogoData(details.getStoreLogo()), details.get_id(),
                null,
                CommonMapper.convertToPlanData(details.getStorePlan()),
                CommonMapper.convertToStatusData(details.getStatus()),convertToDriverDetails(details.getDriverDetails()),
                details.getStoreRattingData() !=null?
                        new com.customer.domain.model.orderdetails.pharmacy.StoreRatingData(details.getStoreRattingData().getRating(),
                                details.getStoreRattingData().getReviewTitle(),details.getStoreRattingData().isRated(),
                                details.getStoreRattingData().getReviewDescription()) : null, details.getDriverRattingData()!=null ?new StoreRatingData(details.getDriverRattingData().getRating(),
                details.getDriverRattingData().getReviewTitle(),details.getDriverRattingData().isRated(),
                details.getDriverRattingData().getReviewDescription()):null
        );
        histStoreOrderData.add(orderData);
      }
    }
    return histStoreOrderData;
  }

  private LocationData convertToLocation(Location location) {
    return new LocationData(location.getLon(), location.getLat());
  }

  private DriverDetailsData convertToDriverDetails(DriverDetails driverDetails) {
    return driverDetails!=null?new DriverDetailsData(driverDetails.getLastName(), driverDetails.getFirstName(),
            driverDetails.getDriverId(), driverDetails.getCountryCode(), driverDetails.getProfilePic(),
            driverDetails.getMobile(), convertToLocation(driverDetails.getLocation()), driverDetails.getMqttTopic(),
            driverDetails.getFcmTopic(), driverDetails.getStoreId(), driverDetails.getEmail(), driverDetails.getDriverType())
            :null;
  }

  private ArrayList<OrdersHistorySellerData> convertToSellerData(
      ArrayList<OrdersHistorySellerDetails> sellerDetails) {
    ArrayList<OrdersHistorySellerData> ordersHistorySellerData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(sellerDetails)) {
      for (OrdersHistorySellerDetails details : sellerDetails) {
        OrdersHistorySellerData sellerData = new OrdersHistorySellerData(
            details.getTargetAmtForFreeDelivery(),
            CommonMapper.convertToPlanData(details.getPlanData()),
            details.getFullFillMentCenterId(), details.getMinOrder(),
            details.getDriverType(), details.getFullfilledBy(), details.getContactPersonName(),
            details.getStoreTax(), details.getPhone(),
            CommonMapper.convertToPickUpAddressData(details.getPickupAddress()),
            details.getAutoDispatch(), details.getAutoAcceptOrders(),
            details.getIsInventoryCheck(), details.getName(),
            convertToLogoData(details.getLogo()), details.getContactPersonEmail()
        );
        ordersHistorySellerData.add(sellerData);
      }
    }
    return ordersHistorySellerData;
  }


  private OrderHistorySellerLogoData convertToLogoData(OrderHistorySellerLogoDetails logoDetails) {
    OrderHistorySellerLogoData sellerLogoData = null;
    if (logoDetails != null) {
      sellerLogoData = new OrderHistorySellerLogoData(logoDetails.getLogoImageThumb(),
          logoDetails.getLogoImageMobile());
    }
    return sellerLogoData;
  }
}
