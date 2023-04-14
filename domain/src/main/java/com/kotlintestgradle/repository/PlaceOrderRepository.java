package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.PlaceOrderUseCase;
import com.kotlintestgradle.model.ecom.slots.DeliverySlot;
import io.reactivex.Single;
import java.util.ArrayList;

public interface PlaceOrderRepository {
  Single<PlaceOrderUseCase.ResponseValues> placeOrder(String cartId, String addressId,
      String billingAddressId,
      int paymentType, String cardId, boolean payByWallet, String coupon, String couponId,
      double discount,
      double latitude, double longitude, String ipAddress, String extraNote, int storeType,
      int orderType,String userId,String currencySymb,String currencyCode);
}
