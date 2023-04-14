package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.model.orderdetails.DeliveryStatusData;
import com.kotlintestgradle.model.orderdetails.MasterOrderData;
import com.kotlintestgradle.model.orderdetails.OrderData;
import com.kotlintestgradle.model.orderdetails.PackageBoxData;
import com.kotlintestgradle.model.orderdetails.PartnerData;
import com.kotlintestgradle.model.orderdetails.pharmacy.AccountingData;
import com.kotlintestgradle.model.orderdetails.pharmacy.AttributesItemData;
import com.kotlintestgradle.model.orderdetails.pharmacy.BagsItemData;
import com.kotlintestgradle.model.orderdetails.pharmacy.BillingAddressData;
import com.kotlintestgradle.model.orderdetails.pharmacy.CardDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.ChargesItems;
import com.kotlintestgradle.model.orderdetails.pharmacy.CountsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.CustomerDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.DeliveryAddressData;
import com.kotlintestgradle.model.orderdetails.pharmacy.DeliveryDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.DeliverySlotDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.DriverDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.ImagesData;
import com.kotlintestgradle.model.orderdetails.pharmacy.LocationData;
import com.kotlintestgradle.model.orderdetails.pharmacy.LogoData;
import com.kotlintestgradle.model.orderdetails.pharmacy.MasterOrdersDetails;
import com.kotlintestgradle.model.orderdetails.pharmacy.MousData;
import com.kotlintestgradle.model.orderdetails.pharmacy.OfferDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.OrdersData;
import com.kotlintestgradle.model.orderdetails.pharmacy.OrdersItemData;
import com.kotlintestgradle.model.orderdetails.pharmacy.PackagingData;
import com.kotlintestgradle.model.orderdetails.pharmacy.PackingDetailItem;
import com.kotlintestgradle.model.orderdetails.pharmacy.PayByData;
import com.kotlintestgradle.model.orderdetails.pharmacy.PickupAddressData;
import com.kotlintestgradle.model.orderdetails.pharmacy.PickupSlotDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.PlansData;
import com.kotlintestgradle.model.orderdetails.pharmacy.ProductsItemData;
import com.kotlintestgradle.model.orderdetails.pharmacy.QuantityData;
import com.kotlintestgradle.model.orderdetails.pharmacy.RattingsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.SellersItemData;
import com.kotlintestgradle.model.orderdetails.pharmacy.ShippingDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.SingleUnitPriceData;
import com.kotlintestgradle.model.orderdetails.pharmacy.StatusData;
import com.kotlintestgradle.model.orderdetails.pharmacy.StoreLogoData;
import com.kotlintestgradle.model.orderdetails.pharmacy.StoreOrdersItemData;
import com.kotlintestgradle.model.orderdetails.pharmacy.StorePlanData;
import com.kotlintestgradle.model.orderdetails.pharmacy.SubstituteWith;
import com.kotlintestgradle.model.orderdetails.pharmacy.TimestampsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.VehicleDetailsData;
import com.kotlintestgradle.model.orderdetails.pharmacy.WalletDetailsData;
import com.kotlintestgradle.model.orderhistory.PickingData;
import com.kotlintestgradle.model.tracking.TrackingItemData;
import com.kotlintestgradle.remote.model.response.orderdetails.DeliveryStatusDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.MasterOrderDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.OrderDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.PackageBoxDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.PartnerDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Accounting;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.AttributesItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.BagsItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.BillingAddress;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.CardDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.ChargesItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Count;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.CustomerDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.DeliveryAddress;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.DeliveryDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.DeliverySlotDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.DriverDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Images;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Location;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Logo;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.MasterOrdersDetail;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.MouData;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.OfferDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.OrdersItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Packaging;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.PackingDetailsItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.PayBy;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.PickupAddress;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.PickupSlotDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.PlanData;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.ProductsItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Quantity;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.RattingData;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.SellersItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.ShippingDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.SingleUnitPrice;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Status;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.StoreLogo;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.StoreOrdersItem;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.StorePlan;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.SubstitutesWith;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.Timestamps;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.VehicleDetails;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.WalletDetails;
import com.kotlintestgradle.remote.model.response.tracking.TrackingItemDetails;

import java.util.ArrayList;
import java.util.List;

import static com.kotlintestgradle.data.utils.DataConstants.PHARMACY_FLOW;
import static com.kotlintestgradle.data.utils.DataUtils.isEmptyArray;

public class OrderDetailsMapper {
  private ArrayList<OrderData> convertToOrderData(ArrayList<OrderDetails> orderDetailsList) {
    ArrayList<OrderData> orderDataList = new ArrayList<>();
    if (!isEmptyArray(orderDetailsList)) {
      for (OrderDetails orderDetails : orderDetailsList) {
        orderDataList.add(convertToOrderData(orderDetails));
      }
    }
    return orderDataList;
  }

  public OrderData convertToOrderData(OrderDetails orderDetails) {
      return new OrderData(orderDetails.getRequestedFor(),
              orderDetails.getOrderType(),
              orderDetails.getPaymentTypeText(), orderDetails.getDriverTypeId(),
              CommonMapper.convertToTimeStampData(orderDetails.getTimestamps()),
              orderDetails.getCreatedTimeStamp(), orderDetails.getOrderTypeMsg(),
              CommonMapper.convertToAccountingData(orderDetails.getAccounting()),
              orderDetails.getPaymentType(),
              CommonMapper.convertToOrderHistProductData(orderDetails.getProducts()),
              CommonMapper.convertToOrderHistProductData(orderDetails.getAlongWith()),
              orderDetails.getRequestedForTimeStamp(),
              convertToPartnerData(orderDetails.getPartnerDetails()), orderDetails.getAutoDispatch(),
              CommonMapper.convertToAddressData(orderDetails.getDeliveryAddress()),
              orderDetails.getCustomerId(), orderDetails.getStoreName(),
              CommonMapper.convertCustomerData(orderDetails.getCustomerDetails()),
              orderDetails.getFreeDeliveryLimitPerStore(), orderDetails.getPayByWallet(),
              orderDetails.getStoreType(), orderDetails.getCartId(), orderDetails.getStoreId(),
              orderDetails.getDriverType(), orderDetails.getMasterOrderId(),
              orderDetails.getPackingDetails(), orderDetails.getStoreTypeMsg(),
              orderDetails.getCreatedDate(), orderDetails.getStoreOrderId(),
              orderDetails.getAutoAcceptOrders(),
              CommonMapper.convertToPickUpAddressData(orderDetails.getPickupAddress()),
              CommonMapper.convertToStoreLogoDataDetails(orderDetails.getStoreLogo()),
              orderDetails.getStoreEmail(), orderDetails.get_id(),
              CommonMapper.convertToAddressData(orderDetails.getBillingAddress()),
              CommonMapper.convertToPlanData(orderDetails.getStorePlan()),
              CommonMapper.convertToStatusData(orderDetails.getStatus()),
              convertToDelStatus(orderDetails.getDeliveryStatus()),
              convertToPackageData(orderDetails.getPackageBox()));
  }

  public MasterOrderData mapper(MasterOrderDetails details, int storeType) {
    MasterOrderData data;
    switch (storeType) {
      case PHARMACY_FLOW:
        data = new MasterOrderData(details.getRequestedFor(), details.getOrderType(),
                details.getPaymentTypeText(), details.getOrderId(), details.getCreatedTimeStamp(),
                details.getOrderTypeMsg(), CommonMapper.convertToAccountingData(details.getAccounting()),
                details.getPaymentType(), details.getRequestedForTimeStamp(),
                CommonMapper.convertToAddressData(details.getDeliveryAddress()),
                details.getCustomerId(), details.getPayByWallet(),
                details.getStoreType(), details.getCoupon(), details.getCartId(),
                convertToOrderData(details.getStoreOrders()), null,
                details.getStoreTypeMsg(), details.getCreatedDate(), details.get_id(),
                CommonMapper.convertToAddressData(details.getBillingAddress()),
                CommonMapper.convertToStatusData(details.getStatus()),
                CommonMapper.convertToOrderHistProductData(details.getProducts()), details.getStoreName(),
                convertToOrderDelStatus(details.getOrderStatus()), details.getPrescriptions(), details.getTip(),
                details.getContactLessDeliveryReason(), details.isContactLessDelivery());
        break;

      default:
        data = new MasterOrderData(details.getRequestedFor(), details.getOrderType(),
                details.getPaymentTypeText(), details.getOrderId(), details.getCreatedTimeStamp(),
                details.getOrderTypeMsg(), CommonMapper.convertToAccountingData(details.getAccounting()),
                details.getPaymentType(), details.getRequestedForTimeStamp(),
                CommonMapper.convertToAddressData(details.getDeliveryAddress()),
                details.getCustomerId(), details.getPayByWallet(),
                details.getStoreType(), details.getCoupon(), details.getCartId(),
                convertToOrderData(details.getStoreOrders()), convertToPackingDetailsData(details.getPackingDetails()),
                details.getStoreTypeMsg(), details.getCreatedDate(), details.get_id(),
                CommonMapper.convertToAddressData(details.getBillingAddress()),
                CommonMapper.convertToStatusData(details.getStatus()),
                CommonMapper.convertToOrderHistProductData(details.getProducts()), details.getStoreName(),
                convertToOrderDelStatus(details.getOrderStatus()));
        break;
    }
    return data;
  }

  private ArrayList<DeliveryStatusData> convertToDelStatus(
      ArrayList<DeliveryStatusDetails> statusDetails) {
    ArrayList<DeliveryStatusData> deliveryStatusData = new ArrayList<>();
    if (!isEmptyArray(statusDetails)) {
      for (DeliveryStatusDetails details : statusDetails) {
        DeliveryStatusData data = new DeliveryStatusData(details.getStatusText(),
            details.getTime(), details.getFormatedDate(), details.getStatus());
        deliveryStatusData.add(data);
      }
    }
    return deliveryStatusData;
  }

  private ArrayList<TrackingItemData> convertToOrderDelStatus(
      ArrayList<TrackingItemDetails> statusDetails) {
    ArrayList<TrackingItemData> deliveryStatusData = new ArrayList<>();
    if (!isEmptyArray(statusDetails)) {
      for (TrackingItemDetails details : statusDetails) {
        TrackingItemData data = new TrackingItemData(details.getStatusText(),
            details.getTime(), details.getFormatedDate(), details.getStatus());
        deliveryStatusData.add(data);
      }
    }
    return deliveryStatusData;
  }

  private PackageBoxData convertToPackageData(PackageBoxDetails details) {
    PackageBoxData packageBoxData = null;
    if (details != null) {
      packageBoxData = new PackageBoxData(details.getImage(),
          details.getVolumeCapacity(), details.getVoulumeCapacityUnit(),
          details.getWidthCapacityUnitName(), details.getWeight(),
          details.getHeightCapacityUnitName(), details.getVoulumeCapacityUnitName(),
          details.getWeightCapacityUnitName(), details.getWidthCapacityUnit(),
          details.getHeightCapacity(), details.getWeightCapacityUnit(),
          details.getLengthCapacityUnitName(), details.getName().getEn(),
          details.getHeightCapacityUnit(), details.getLengthCapacityUnit(),
          details.getWidthCapacity(), details.getLengthCapacity(),
          details.getBoxId());
    }
    return packageBoxData;
  }

  private PartnerData convertToPartnerData(PartnerDetails details) {
    PartnerData partnerData = null;
    if (details != null) {
      partnerData = new PartnerData(details.getId(), details.getName(), details.getTrackingId());
    }
    return partnerData;
  }

  public MasterOrdersDetails mapper(MasterOrdersDetail masterOrderDetails) {
    return new MasterOrdersDetails(convertToOrdersData(masterOrderDetails),
            null);
  }

  public StoreOrdersItemData mapper(StoreOrdersItem ordersItem) {
    return new StoreOrdersItemData(ordersItem.getRequestedFor(),
            ordersItem.getDeliverySlotId(), ordersItem.getOrderType(), ordersItem.getRececiptURL(),
            ordersItem.getOrderTypeMsg(), convertToAccountingData(ordersItem.getAccounting()),
            ordersItem.getCustomerPaymentTypeMsg(), ordersItem.getRequestedForPickupTimeStamp(),
            ordersItem.getRequestedForPickup(), ordersItem.getStoreTaxId(),
            convertToDelAddress(ordersItem.getDeliveryAddress()),
            ordersItem.getPoInvoiceLink(), ordersItem.getBookingType(), ordersItem.getStorePhone(),
            ordersItem.getReminderPreference(),
            convertToDelSlotDetails(ordersItem.getDeliverySlotDetails()), ordersItem.getCartId(),
            ordersItem.getPickupSlotId(), ordersItem.getBookingTypeText(), ordersItem.getStoreTypeMsg(),
            ordersItem.getStoreOrderId(), ordersItem.getStoreCategoryId(), ordersItem.isAutoAcceptOrders(),
            convertToPickupAddress(ordersItem.getPickupAddress()), ordersItem.getStoreEmail(),
            ordersItem.getId(), ordersItem.getSellerType(), convertToStatus(ordersItem.getStatus()),
            convertToWalletDetail(ordersItem.getWalletDetails()), ordersItem.getPaymentTypeText(),
            ordersItem.getDriverTypeId(), ordersItem.getBags(), convertToTimeStamps(ordersItem.getTimestamps()),
            ordersItem.getExtraNote(), ordersItem.getCreatedTimeStamp(), ordersItem.isContactLessDelivery(),
            ordersItem.getCustomerPaymentType(), ordersItem.getPaymentType(),
            convertToProductItems(ordersItem.getProducts()), ordersItem.getRequestedForTimeStamp(),
            ordersItem.getOrderImages(), ordersItem.getAutoDispatch(),
            convertToCardDetails(ordersItem.getCardDetails()), ordersItem.getCustomerId(),
            ordersItem.getStoreName(), convertToCustomerDetails(ordersItem.getCustomerDetails()),
            ordersItem.getFreeDeliveryLimitPerStore(), ordersItem.isPayByWallet(), ordersItem.getStoreType(),
            ordersItem.getCoupon(), ordersItem.getSellerTypeMsg(), ordersItem.getStoreCategory(), ordersItem.getStoreId(),
            ordersItem.getDriverType(), ordersItem.getMasterOrderId(), ordersItem.getCreatedDate(), ordersItem.getContactLessDeliveryReason(),
            convertToStoreLogo(ordersItem.getStoreLogo()), convertToBillingAddress(ordersItem.getBillingAddress()),
            convertToPickupSlotDetails(ordersItem.getPickupSlotDetails()), convertToStorePlan(ordersItem.getStorePlan()),
            convertToPackingDetailsData(ordersItem.getPackingDetails()),new PickingData(ordersItem.getPickerDetaisl().getType(),
           ordersItem.getPickerDetaisl().getName(),ordersItem.getPickerDetaisl().getPhone(),ordersItem.getPickerDetaisl().getEmail(),
           ordersItem.getPickerDetaisl().getFcmTopic(),ordersItem.getPickerDetaisl().getMqttTopic(),ordersItem.getPickerDetaisl().getProfilePic()));
  }

  private OrdersData convertToOrdersData(MasterOrdersDetail data) {
    return new OrdersData(data.getRequestedFor(), data.getOrderType(), convertToWalletDetails(data.getWalletDetails()),
            data.getPaymentTypeText(), data.getOrderId(), data.getExtraNote(), convertToTimeStamps(data.getTimestamps()),
            data.getCreatedTimeStamp(), data.isContactLessDelivery(), data.getOrderTypeMsg(),
            convertToAccountingData(data.getAccounting()), data.getCustomerPaymentType(),
            data.getCustomerPaymentTypeMsg(), data.getPaymentType(), data.getRequestedForPickupTimeStamp(),
            data.getRequestedForPickup(), data.getOrderImages(),
            data.getRequestedForTimeStamp(), convertToDelAddress(data.getDeliveryAddress()),
            data.getBookingType(), convertToCardDetails(data.getCardDetails()),data.getCustomerId(),
            convertToCustomerDetails(data.getCustomerDetails()),
            data.getReminderPreference(), data.isPayByWallet(),
            data.getStoreType(), data.getCoupon(), data.getCartId(), data.getBookingTypeText(),
            convertToCount(data.getCount()), convertToStoreOrders(data.getStoreOrders()),
            data.getStoreCategory(), data.getPackingDetails(),
            data.getStoreTypeMsg(), data.getCreatedDate(), data.getStoreCategoryId(), data.getContactLessDeliveryReason(),
            convertToOrders(data.getOrders()), data.getId(), convertToBillingAddress(data.getBillingAddress()),
            convertToSellers(data.getSellers()), convertToStatus(data.getStatus()),data.getStoreName());
  }

  private StatusData convertToStatus(Status status) {
    return new StatusData(status.getStatusText(), status.getUpdatedOnTimeStamp(),
            status.getUpdatedOn(), status.getStatus(), status.getExpiryTimeForConfirmation(),
            status.isCustomerConfirm(),status.isRejectedForPrescription());
  }

  private ArrayList<SellersItemData> convertToSellers(List<SellersItem> sellers) {
    ArrayList<SellersItemData> sellersItemData = new ArrayList<>();
    if (!isEmptyArray(sellers)) {
      for (SellersItem sellersItem: sellers) {
        SellersItemData data = new SellersItemData(sellersItem.getTargetAmtForFreeDelivery(),
                sellersItem.getDriverTypeId(), sellersItem.getStoreTypeId(), sellersItem.getStoreFrontType(),
                sellersItem.getCityId(), sellersItem.getMinOrder(), sellersItem.getFullfilledBy(),
                sellersItem.getStoreTax(), sellersItem.getCityName(), sellersItem.getAreaName(),
                sellersItem.getAutoDispatch(),
                 sellersItem.getPoInvoiceLink(), convertToLogo(sellersItem.getLogo()),
                sellersItem.getSellerTypeId(), sellersItem.getStoreType(), sellersItem.getStoreFrontTypeId(),
                convertToPlanData(sellersItem.getPlanData()),
                sellersItem.getFullFillMentCenterId(),
                sellersItem.getDriverType(), sellersItem.getContactPersonName(), sellersItem.getPhone(),
                convertToPickUpAddress(sellersItem.getPickupAddress()), sellersItem.isAutoAcceptOrders(),
                sellersItem.isIsInventoryCheck(), sellersItem.getName(), sellersItem.getSellerType(),
                sellersItem.getContactPersonEmail());
        sellersItemData.add(data);
      }
    }
    return null;
  }

  private LogoData convertToLogo(Logo logo) {
    return new LogoData(logo.getLogoImageThumb(), logo.getLogoImageMobile(), logo.getLogoImageweb());
  }

  private PickupAddressData convertToPickUpAddress(PickupAddress pickupAddress) {
    return new PickupAddressData(pickupAddress.getCountry(), pickupAddress.getAddress(),
            pickupAddress.getGooglePlaceId(), pickupAddress.getCity(), pickupAddress.getLocality(),
            pickupAddress.getAreaOrDistrict(), pickupAddress.getGooglePlaceName(), pickupAddress.getCityId(),
            pickupAddress.getJsonMemberLong(), pickupAddress.getRoute(), pickupAddress.getCityName(),
            pickupAddress.getAddressLine1(), pickupAddress.getPostCode(), pickupAddress.getAddressLine2(),
            pickupAddress.getState(), pickupAddress.getAddressArea(), pickupAddress.getLat());
  }

  private PlansData convertToPlanData(PlanData planData) {
    return new PlansData(planData.getAppCommissionTypeText(), planData.getAppCommission(),
            planData.getName(), planData.getAppCommissionType());
  }

  private BillingAddressData convertToBillingAddress(BillingAddress billingAddress) {
    return new BillingAddressData(billingAddress.getCity(),
            billingAddress.getMobileNumber(), billingAddress.getLatitude(), billingAddress.getAlternatePhoneCode(),
            billingAddress.getPlaceId(), billingAddress.getCreatedTimeStamp(), billingAddress.getCityId(),
            billingAddress.getCountryId(), billingAddress.isJsonMemberDefault(), billingAddress.getCityName(),
            billingAddress.getTagged(), billingAddress.getZoneId(), billingAddress.getAddLine1(),
            billingAddress.getAddLine2(), billingAddress.getState(), billingAddress.getLandmark(),
            billingAddress.getTaggedAs(), billingAddress.getLongitude(), billingAddress.getMobileNumberCode(),
            billingAddress.getPincode(), billingAddress.getFlatNumber(), billingAddress.getAlternatePhone(),
            billingAddress.getLocality(), billingAddress.getTimeZone(), billingAddress.getUserId(),
            billingAddress.getCreatedIsoDate(), billingAddress.getName(), billingAddress.getFullAddress(), billingAddress.getId(),
            billingAddress.getUserType(), billingAddress.getCountryName(), billingAddress.getCountry());
  }

  private ArrayList<OrdersItemData> convertToOrders(List<OrdersItem> orders) {
    ArrayList<OrdersItemData> ordersItemData = new ArrayList<>();
    if (!isEmptyArray(orders)) {
      for (OrdersItem itemData : orders) {
        OrdersItemData data = new OrdersItemData(itemData.getStoreOrderId(),
                itemData.getProductOrderId());
        ordersItemData.add(data);
      }
    }
    return ordersItemData;
  }

  private ArrayList<StoreOrdersItemData> convertToStoreOrders(List<StoreOrdersItem> storeOrders) {
    ArrayList<StoreOrdersItemData> storeOrdersItems = new ArrayList<>();
    if (!isEmptyArray(storeOrders)) {
      for (StoreOrdersItem ordersItem : storeOrders) {
        StoreOrdersItemData data = new StoreOrdersItemData(ordersItem.getRequestedFor(),
                ordersItem.getDeliverySlotId(), ordersItem.getOrderType(), ordersItem.getRececiptURL(),
                ordersItem.getOrderTypeMsg(), convertToAccountingData(ordersItem.getAccounting()),
                ordersItem.getCustomerPaymentTypeMsg(), ordersItem.getRequestedForPickupTimeStamp(),
                ordersItem.getRequestedForPickup(), ordersItem.getStoreTaxId(),
                convertToDelAddress(ordersItem.getDeliveryAddress()),
                ordersItem.getPoInvoiceLink(), ordersItem.getBookingType(), ordersItem.getStorePhone(),
                ordersItem.getReminderPreference(),
                convertToDelSlotDetails(ordersItem.getDeliverySlotDetails()), ordersItem.getCartId(),
                ordersItem.getPickupSlotId(), ordersItem.getBookingTypeText(), ordersItem.getStoreTypeMsg(),
                ordersItem.getStoreOrderId(), ordersItem.getStoreCategoryId(), ordersItem.isAutoAcceptOrders(),
                convertToPickupAddress(ordersItem.getPickupAddress()), ordersItem.getStoreEmail(),
                ordersItem.getId(), ordersItem.getSellerType(), convertToStatus(ordersItem.getStatus()),
                convertToWalletDetail(ordersItem.getWalletDetails()), ordersItem.getPaymentTypeText(),
                ordersItem.getDriverTypeId(), ordersItem.getBags(), convertToTimeStamps(ordersItem.getTimestamps()),
                ordersItem.getExtraNote(), ordersItem.getCreatedTimeStamp(), ordersItem.isContactLessDelivery(),
                ordersItem.getCustomerPaymentType(), ordersItem.getPaymentType(),
                convertToProductItems(ordersItem.getProducts()), ordersItem.getRequestedForTimeStamp(),
                ordersItem.getOrderImages(), ordersItem.getAutoDispatch(),
                convertToCardDetails(ordersItem.getCardDetails()), ordersItem.getCustomerId(),
                ordersItem.getStoreName(), convertToCustomerDetails(ordersItem.getCustomerDetails()),
                ordersItem.getFreeDeliveryLimitPerStore(), ordersItem.isPayByWallet(), ordersItem.getStoreType(),
                ordersItem.getCoupon(), ordersItem.getSellerTypeMsg(), ordersItem.getStoreCategory(), ordersItem.getStoreId(),
                ordersItem.getDriverType(), ordersItem.getMasterOrderId(), ordersItem.getCreatedDate(), ordersItem.getContactLessDeliveryReason(),
                convertToStoreLogo(ordersItem.getStoreLogo()), convertToBillingAddress(ordersItem.getBillingAddress()),
                convertToPickupSlotDetails(ordersItem.getPickupSlotDetails()), convertToStorePlan(ordersItem.getStorePlan()),
                convertToPackingDetailsData(ordersItem.getPackingDetails()),new com.kotlintestgradle.model.orderhistory.PickingData(ordersItem.getPickerDetaisl().getType(),
            ordersItem.getPickerDetaisl().getName(),ordersItem.getPickerDetaisl().getPhone(),ordersItem.getPickerDetaisl().getEmail(),
            ordersItem.getPickerDetaisl().getFcmTopic(),ordersItem.getPickerDetaisl().getMqttTopic(),ordersItem.getPickerDetaisl().getProfilePic()));
        storeOrdersItems.add(data);
      }
    }
    return storeOrdersItems;
  }

  private PickupSlotDetailsData convertToPickupSlotDetails(PickupSlotDetails pickupSlotDetails) {
    if (pickupSlotDetails != null) {
      return new PickupSlotDetailsData(pickupSlotDetails.getDate(), pickupSlotDetails.getShiftName(),
              pickupSlotDetails.getStartDateTime(), pickupSlotDetails.getStartTime(),
              pickupSlotDetails.getEndTime(), pickupSlotDetails.getEndDateTime());
    } else {
      return new PickupSlotDetailsData();
    }
  }

  private StorePlanData convertToStorePlan(StorePlan storePlan) {
    return new StorePlanData(storePlan.getAppCommissionTypeText(), storePlan.getAppCommission(),
            storePlan.getName(), storePlan.getAppCommissionType());
  }

  private StoreLogoData convertToStoreLogo(StoreLogo storeLogo) {
    return new StoreLogoData(storeLogo.getLogoImageThumb(), storeLogo.getLogoImageMobile(), storeLogo.getLogoImageweb());
  }

  private ArrayList<ProductsItemData> convertToProductItems(List<ProductsItem> products) {
    ArrayList<ProductsItemData> productsItemData = new ArrayList<>();
    if (!isEmptyArray(products)) {
      for (ProductsItem productsItem : products) {
        ProductsItemData data = new ProductsItemData(productsItem.getUpcNumber(),
                productsItem.getColor(), productsItem.getForcePickImage(),
                convertToTimeStamps(productsItem.getTimestamps()),
                convertToSingleUnitPrice(productsItem.getSingleUnitPrice()),
                convertToOfferDetails(productsItem.getOfferDetails()),
                productsItem.getNoofunits(), productsItem.getPreparationTime(), productsItem.getProductOrderId(),
                convertToAccountingData(productsItem.getAccounting()), productsItem.getAisle(),
                productsItem.getPackageType(), productsItem.getCentralProductId(), productsItem.isAllowOrderOutOfStock(),
                convertToShippingDetails(productsItem.getShippingDetails()),
                productsItem.getUnitId(), productsItem.getInvoiceLink(), productsItem.getBarcode(),
                productsItem.getProductDeliveryFee(), convertToMouData(productsItem.getMouData()),
                convertToImages(productsItem.getImages()), productsItem.getBrandName(),
                convertToQuantity(productsItem.getQuantity()), productsItem.getProductId(),
                productsItem.getCoupon(), productsItem.getAddOns(), productsItem.getPackageId(),
                productsItem.getShippingLabel(), productsItem.isIsSplitProduct(), productsItem.getCurrencySymbol(),
                convertToPackaging(productsItem.getPackaging()), productsItem.getShelf(), productsItem.isIsParentProduct(),
                productsItem.isPrescriptionRequired(), productsItem.isIsCentral(), productsItem.getDirections(),
                productsItem.getManufactureName(), productsItem.getName(),
                convertToRattingData(productsItem.getRattingData()),
                convertToAttributesItem(productsItem.getAttributes()), productsItem.getCurrencyCode(),
                convertToStatus(productsItem.getStatus()), productsItem.getReason(), productsItem.hasSubstitute(),
                convertToSubstituteWith(productsItem.getSubstitutesWith(), productsItem.hasSubstitute()));
        productsItemData.add(data);
      }
    }
    return productsItemData;
  }

  private ArrayList<PackingDetailItem> convertToPackingDetailsData(ArrayList<PackingDetailsItem> packingDetails) {
    ArrayList<PackingDetailItem> packingDetailItems = new ArrayList<>();
    if (!isEmptyArray(packingDetails)) {
      for (PackingDetailsItem packing: packingDetails) {
        PackingDetailItem item = new PackingDetailItem(packing.getRequestedFor(), packing.getDeliverySlotId(),
                packing.getOrderType(), packing.getRececiptURL(), packing.getOrderTypeMsg(),
                convertToPartnerData(packing.getPartnerDetails()),
                convertToAccountingData(packing.getAccounting()),
                packing.getRequestedForPickupTimeStamp(), packing.getRequestedForPickup(),
                convertToDelAddress(packing.getDeliveryAddress()), packing.getBookingType(),
                packing.getStorePhone(), convertToPackageData(packing.getPackageBox()),
                packing.getBookingTypeMsg(),
                convertToDelSlotDetails(packing.getDeliverySlotDetails()),
                packing.getPickupSlotId(), packing.getShippingLabel(),
                convertToDriverDetails(packing.getDriverDetails()), packing.getStoreTypeMsg(),
                packing.getStoreOrderId(), packing.getStoreCategoryId(),
                convertToPickupAddress(packing.getPickupAddress()), packing.getStoreEmail(),
                packing.getId(), packing.getSellerType(), convertToStatus(packing.getStatus()),
                packing.getPaymentTypeText(), packing.getDriverTypeId(),
                convertToBags(packing.getBags()), convertToTimeStamps(packing.getTimestamps()),
                packing.getProductOrderIds(), packing.getCreatedTimeStamp(), packing.isContactLessDelivery(),
                packing.getPaymentType(), packing.getRequestedForTimeStamp(), packing.isAutoDispatch(),
                packing.getCustomerId(), packing.getStoreName(), convertToCustomerDetails(packing.getCustomerDetails()),
                packing.getInvoiceLink(), packing.isPayByWallet(), packing.getStoreType(),
                convertToVechileDetails(packing.getVehicleDetails()), packing.getSellerTypeMsg(),
                packing.getPackageId(), packing.getStoreCategory(), packing.getStoreId(),
                packing.getDriverType(), packing.getMasterOrderId(), packing.getCreatedDate(),
                packing.getContactLessDeliveryReason(), convertToStoreLogo(packing.getStoreLogo()),
                convertToBillingAddress(packing.getBillingAddress()),
                convertToPickupSlotDetails(packing.getPickupSlotDetails()),
                convertToStorePlan(packing.getStorePlan()));
        packingDetailItems.add(item);
      }
    }
    return packingDetailItems;
  }

  private VehicleDetailsData convertToVechileDetails(VehicleDetails vehicleDetails) {
    return new VehicleDetailsData(vehicleDetails.isTowing(), vehicleDetails.getVehicleImage(),
            vehicleDetails.getPlateNo(), new ArrayList<String>(), vehicleDetails.getTypeName(),
            vehicleDetails.getVehicleMake(), vehicleDetails.getColour(), vehicleDetails.getVehicleYear(),
            vehicleDetails.getVehicleModel(), vehicleDetails.getTypeId(), vehicleDetails.getId(),
            vehicleDetails.getVehicleId(), vehicleDetails.getVehicleMapIcon());
  }

  private ArrayList<BagsItemData> convertToBags(ArrayList<BagsItem> bags) {
    ArrayList<BagsItemData> bagsItemData = new ArrayList<>();
    if (!isEmptyArray(bags)) {
      for (BagsItem item: bags) {
        BagsItemData currItem = new BagsItemData(item.getBagId(), item.getLable());
        bagsItemData.add(currItem);
      }
    }
    return bagsItemData;
  }

  private DriverDetailsData convertToDriverDetails(DriverDetails driverDetails) {
    return new DriverDetailsData(driverDetails.getLastName(), driverDetails.getFirstName(),
            driverDetails.getDriverId(), driverDetails.getCountryCode(), driverDetails.getProfilePic(),
            driverDetails.getMobile(), convertToLocation(driverDetails.getLocation()), driverDetails.getMqttTopic(),
            driverDetails.getFcmTopic(), driverDetails.getStoreId(), driverDetails.getEmail(), driverDetails.getDriverType());
  }

  private com.kotlintestgradle.model.ecom.location.LocationData convertToLocation(Location location) {
    return new com.kotlintestgradle.model.ecom.location.LocationData(location.getLon(), location.getLat());
  }

  private SubstituteWith convertToSubstituteWith(SubstitutesWith substitutesWith, boolean isSubstituteAvailable) {
    if (isSubstituteAvailable && substitutesWith != null) {
      return new SubstituteWith(substitutesWith.getCentralProductId(),
              convertToImages(substitutesWith.getImages()), substitutesWith.getBrandName(),
              convertToQuantity(substitutesWith.getQuantity()), substitutesWith.getColor(),
              substitutesWith.getOriginalPrice(), substitutesWith.getName(), substitutesWith.getUnitId(),
              substitutesWith.getId());
    }
    return null;
  }

  private ArrayList<AttributesItemData> convertToAttributesItem(List<AttributesItem> listAttributes) {
    ArrayList<AttributesItemData> attributesItemData = new ArrayList<>();
    if (!isEmptyArray(listAttributes)) {
      for (AttributesItem item : listAttributes) {
        AttributesItemData data = new AttributesItemData(item.isIsAddOn(), item.getAttrname(),
                item.getValue());
        attributesItemData.add(data);
      }
    }
    return attributesItemData;
  }

  private RattingsData convertToRattingData(RattingData rattingData) {
    return new RattingsData(rattingData.getReviewDescription(), rattingData.isIsRated(), rattingData.getRating(),
            rattingData.getReviewTitle());
  }

  private PackagingData convertToPackaging(Packaging packaging) {
    return new PackagingData(packaging.getUnitType(), packaging.getPackingType(), packaging.getUnitValue());
  }

  private QuantityData convertToQuantity(Quantity quantity) {
    return new QuantityData(quantity.getUnit(), quantity.getValue());
  }

  private ImagesData convertToImages(Images images) {
    return new ImagesData(images.getSmall(), images.getLarge(), images.getExtraLarge(),
            images.getAltText(), images.getMedium());
  }

  private MousData convertToMouData(MouData mouData) {
    return new MousData();
  }

  private ShippingDetailsData convertToShippingDetails(ShippingDetails shippingDetails) {
    return new ShippingDetailsData(shippingDetails.getName(), shippingDetails.getId(), shippingDetails.getTrackingId());
  }

  private OfferDetailsData convertToOfferDetails(OfferDetails offerDetails) {
    return new OfferDetailsData(offerDetails.getOfferType(), offerDetails.getOfferId(), offerDetails.getOfferValue(),
            offerDetails.getOfferTitle());
  }

  private SingleUnitPriceData convertToSingleUnitPrice(SingleUnitPrice singleUnitPrice) {
    return new SingleUnitPriceData(singleUnitPrice.getUnitPrice(), singleUnitPrice.getSubTotalAmount(),
            singleUnitPrice.getTaxableAmount(), singleUnitPrice.getFinalUnitPrice(), singleUnitPrice.getResellerCommission(),
            singleUnitPrice.getAddOnsAmount(), singleUnitPrice.getTax(), singleUnitPrice.getSubTotal(),
            singleUnitPrice.getResellerCommissionType(), singleUnitPrice.getOfferDiscount(), singleUnitPrice.getNormalUnitPrice(),
            singleUnitPrice.getExpressUnitPrice(), singleUnitPrice.getTaxAmount(), singleUnitPrice.getReason());
  }

  private WalletDetailsData convertToWalletDetail(WalletDetails walletDetails) {
    return new WalletDetailsData(walletDetails.getCharges());
  }

  private PickupAddressData convertToPickupAddress(PickupAddress pickupAddress) {
    return new PickupAddressData(pickupAddress.getCountry(), pickupAddress.getAddress(),
            pickupAddress.getGooglePlaceId(), pickupAddress.getCity(), pickupAddress.getLocality(),
            pickupAddress.getAreaOrDistrict(), pickupAddress.getGooglePlaceName(),
            pickupAddress.getCityId(), pickupAddress.getJsonMemberLong(), pickupAddress.getRoute(),
            pickupAddress.getCityName(), pickupAddress.getAddressLine1(), pickupAddress.getPostCode(), pickupAddress.getAddressLine2(),
            pickupAddress.getState(), pickupAddress.getAddressArea(), pickupAddress.getLat());
  }

  private DeliverySlotDetailsData convertToDelSlotDetails(DeliverySlotDetails deliverySlotDetails) {
    if (deliverySlotDetails != null) {
      return new DeliverySlotDetailsData(deliverySlotDetails.getDate(), deliverySlotDetails.getShiftName(),
              deliverySlotDetails.getStartDateTime(), deliverySlotDetails.getStartTime(), deliverySlotDetails.getEndTime(),
              deliverySlotDetails.getEndDateTime());
    } else {
      return new DeliverySlotDetailsData();
    }
  }

  private AccountingData convertToAccountingData(Accounting accounting) {
    return new AccountingData(accounting.getUnitPrice(), accounting.getTaxableAmount(), accounting.getFinalUnitPrice(),
            accounting.getAppEarningWithTax(), accounting.getPgEarning(), convertToPayBy(accounting.getPayBy()),
            accounting.getDriverEarning(), accounting.getCurrencySymbol(), accounting.getAddOnsAmount(), accounting.getTax(),
            accounting.getSubTotal(), accounting.getStoreEarning(), accounting.getOfferDiscount(), accounting.getDeliveryFee(),
            convertToDeliveryDetails(accounting.getDeliveryDetails()), accounting.getTaxAmount(), accounting.getPromoDiscount(), accounting.getCurrencyCode(),
            accounting.getFinalTotal(), accounting.getAppEarning(), accounting.getTip());
  }

  private PayByData convertToPayBy(PayBy payBy) {
    return new PayByData(payBy.getWallet(), payBy.getCash(), payBy.getCard());
  }

  private DeliveryDetailsData convertToDeliveryDetails(DeliveryDetails deliveryDetails) {
    return (deliveryDetails == null) ? new DeliveryDetailsData() :
            new DeliveryDetailsData(deliveryDetails.getDeliveryFee(),
            deliveryDetails.isDeliveryByDeliveryPartner(),
            deliveryDetails.isDeliveryByFleetDriver(),
            deliveryDetails.getTime());
  }

  private CountsData convertToCount(Count count) {
    return new CountsData(count.getUnavailable(), count.getPicked(), count.getSubstitutes(),
            count.getPending(), count.getPacked());
  }

  private CustomerDetailsData convertToCustomerDetails(CustomerDetails customerDetails) {
    return new CustomerDetailsData(customerDetails.getFirstName(), customerDetails.getLastName(),
            customerDetails.getCountryCode(), customerDetails.getUserTypeText(), customerDetails.getMobile(),
            customerDetails.getId(), customerDetails.getUserType(), customerDetails.getMqttTopic(), customerDetails.getFcmTopic(),
            customerDetails.getEmail());
  }

  private CardDetailsData convertToCardDetails(CardDetails cardDetails) {
    return new CardDetailsData(cardDetails.getLast4(), convertToGetCharges(cardDetails.getCharges()),
            cardDetails.getChargeId(), cardDetails.getCardId());
  }

  private ArrayList<ChargesItems> convertToGetCharges(List<ChargesItem> charges) {
    ArrayList<ChargesItems> chargesItems = new ArrayList<>();
    if(!isEmptyArray(charges)) {
      for (ChargesItem item: charges) {
        ChargesItems currItems = new ChargesItems(item.getAmount(),
                item.getChargeId());
        chargesItems.add(currItems);
      }
    }
    return chargesItems;
  }

  private DeliveryAddressData convertToDelAddress(DeliveryAddress deliveryAddress) {
    return new DeliveryAddressData(deliveryAddress.getCountry(), deliveryAddress.getCity(),
            deliveryAddress.getMobileNumber(), deliveryAddress.getLatitude(), deliveryAddress.getAlternatePhoneCode(),
            deliveryAddress.getPlaceId(), deliveryAddress.getCreatedTimeStamp(), deliveryAddress.getCityId(),
            deliveryAddress.getCountryId(), deliveryAddress.isJsonMemberDefault(), deliveryAddress.getCityName(),
            deliveryAddress.getTagged(), deliveryAddress.getZoneId(), deliveryAddress.getAddLine1(), deliveryAddress.getAddLine2(),
            deliveryAddress.getState(), deliveryAddress.getLandmark(), deliveryAddress.getTaggedAs(), deliveryAddress.getLongitude(),
            deliveryAddress.getMobileNumberCode(), deliveryAddress.getPincode(), deliveryAddress.getFlatNumber(), deliveryAddress.getAlternatePhone(),
            deliveryAddress.getLocality(), deliveryAddress.getTimeZone(), deliveryAddress.getUserId(), deliveryAddress.getCreatedIsoDate(),
            deliveryAddress.getName(), deliveryAddress.getFullAddress(), deliveryAddress.getId(), deliveryAddress.getUserType(), deliveryAddress.getCountryName());
  }

  private TimestampsData convertToTimeStamps(Timestamps timestamps) {
    return new TimestampsData(timestamps.getPicked(), timestamps.getInDispatch(), timestamps.getCreated(),
            timestamps.getReadyForPickup(), timestamps.getAccepted(), timestamps.getCancelled(),
            timestamps.getCompleted(), timestamps.getPacked());
  }

  private WalletDetailsData convertToWalletDetails(WalletDetails walletDetails) {
    return new WalletDetailsData(walletDetails.getCharges());
  }
}
