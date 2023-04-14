package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.model.ecom.deliveryfee.Data;
import com.kotlintestgradle.model.ecom.deliveryfee.DeliveryFee;
import com.kotlintestgradle.remote.model.response.ecom.deliveryfee.DeliveryData;

public class DeliveryFeeMapper {

  public DeliveryFee mapper(DeliveryData data) {
    return new DeliveryFee(convertToDeliveryFeeData(data),
            null);
  }

  private Data convertToDeliveryFeeData(DeliveryData data) {
    return new Data(data.isDeliveryByDeliveryPartner(),
            data.getDeliveryFee(), data.isDeliveryByFleetDriver());
  }

}
