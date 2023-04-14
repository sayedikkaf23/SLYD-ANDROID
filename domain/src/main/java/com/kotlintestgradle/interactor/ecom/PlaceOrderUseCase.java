package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.slots.DeliverySlot;
import com.kotlintestgradle.repository.PlaceOrderRepository;
import io.reactivex.Single;
import java.util.ArrayList;
import javax.inject.Inject;

public class PlaceOrderUseCase extends
    UseCase<PlaceOrderUseCase.RequestValues, PlaceOrderUseCase.ResponseValues> {
  private PlaceOrderRepository mRepository;

  @Inject
  public PlaceOrderUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, PlaceOrderRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.placeOrder(requestValues.cartId, requestValues.addressId,
        requestValues.billingAddressId,
        requestValues.paymentType, requestValues.cardId, requestValues.payByWallet,
        requestValues.coupon, requestValues.couponId, requestValues.discount,
        requestValues.latitude,
        requestValues.longitude, requestValues.ipAddress, requestValues.extraNote,
        requestValues.storeType, requestValues.orderType,requestValues.userId,
        requestValues.currencySymbol,requestValues.currencyCode);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String cartId;
    private String addressId;
    private String billingAddressId;
    private int paymentType;
    private String cardId;
    private boolean payByWallet;
    private String coupon;
    private String couponId;
    private double discount;
    private double latitude;
    private double longitude;
    private String ipAddress;
    private String extraNote;
    private int storeType;
    private int orderType;
    private int type;
    private String requestedTimePickup = "";
    private String requestedTime = "";
    private boolean contactlessDelivery;
    private String contactlessDeliveryReason;
    private String tip;
    private ArrayList<DeliverySlot> pickUpSlotId = new ArrayList<>();
    private ArrayList<DeliverySlot> deliverySlotId = new ArrayList<>();
    private ArrayList<String> orderImages = new ArrayList<>();
    private String userId;
    private String currencySymbol;
    private String currencyCode;

    public RequestValues(String cartId, String addressId, String billingAddressId, int paymentType,
        String cardId,
        boolean payByWallet, String coupon, String couponId, double discount, double latitude,
        double longitude,
        String ipAddress, String extraNote, int storeType, int orderType,String userId,String currencySymbol,
        String currencyCode) {
      this.cartId = cartId;
      this.addressId = addressId;
      this.billingAddressId = billingAddressId;
      this.paymentType = paymentType;
      this.cardId = cardId;
      this.payByWallet = payByWallet;
      this.coupon = coupon;
      this.couponId = couponId;
      this.discount = discount;
      this.latitude = latitude;
      this.longitude = longitude;
      this.ipAddress = ipAddress;
      this.extraNote = extraNote;
      this.storeType = storeType;
      this.orderType = orderType;
      this.userId = userId;
      this.currencySymbol = currencySymbol;
      this.currencyCode = currencyCode;
    }

    public RequestValues(String cartId, String addressId, String billingAddressId, int paymentType,
        String requestedTimePickup, String requestedTime, ArrayList<DeliverySlot> pickUpSlotId,
        ArrayList<DeliverySlot> deliverySlotId, ArrayList<String> orderImages, String cardId,
        boolean payByWallet, String coupon, String couponId, double discount, int type,
        double latitude,
        double longitude, String ipAddress, String extraNote, int storeType, int orderType,
        boolean contactlessDelivery, String contactlessDeliveryReason, String tip) {
      this.cartId = cartId;
      this.addressId = addressId;
      this.billingAddressId = billingAddressId;
      this.paymentType = paymentType;
      this.cardId = cardId;
      this.payByWallet = payByWallet;
      this.coupon = coupon;
      this.couponId = couponId;
      this.discount = discount;
      this.latitude = latitude;
      this.longitude = longitude;
      this.ipAddress = ipAddress;
      this.extraNote = extraNote;
      this.storeType = storeType;
      this.orderType = orderType;
      this.requestedTimePickup = requestedTimePickup;
      this.requestedTime = requestedTime;
      this.pickUpSlotId = pickUpSlotId;
      this.deliverySlotId = deliverySlotId;
      this.orderImages = orderImages;
      this.type = type;
      this.contactlessDelivery = contactlessDelivery;
      this.contactlessDeliveryReason = contactlessDeliveryReason;
      this.tip = tip;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private CommonData mData;

    public ResponseValues(CommonData data) {
      this.mData = data;
    }

    public CommonData getData() {
      return mData;
    }

    public void setData(CommonData data) {
      this.mData = data;
    }
  }
}
