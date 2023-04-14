package com.kotlintestgradle.data.mapper;

import android.util.Log;
import com.kotlintestgradle.cache.entity.UserCart;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.common.FinalPriceListData;
import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.common.OffersListData;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import com.kotlintestgradle.model.ecom.common.SellerData;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.getcart.CartPickupAddressData;
import com.kotlintestgradle.model.ecom.getcart.CartTaxData;
import com.kotlintestgradle.model.ecom.getcart.QuantityData;
import com.kotlintestgradle.model.ecom.home.CategoryData;
import com.kotlintestgradle.model.ecom.home.CategoryOfferData;
import com.kotlintestgradle.model.ecom.pdp.ColorsData;
import com.kotlintestgradle.model.ecom.pdp.ReviewParameterData;
import com.kotlintestgradle.model.ecom.pdp.SizeData;
import com.kotlintestgradle.model.ecom.pdp.SupplierData;
import com.kotlintestgradle.model.ecom.similarproducts.SimilarProductsData;
import com.kotlintestgradle.model.orderhistory.OrderHistAttributeData;
import com.kotlintestgradle.model.orderhistory.OrderHistOfferData;
import com.kotlintestgradle.model.orderhistory.OrderHistPackagingData;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;
import com.kotlintestgradle.model.orderhistory.OrderHistRatingData;
import com.kotlintestgradle.model.orderhistory.OrderHistShippingData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryAccountingData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryCustomerData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryPlanData;
import com.kotlintestgradle.model.orderhistory.OrderStatusData;
import com.kotlintestgradle.model.orderhistory.OrderTimeStampData;
import com.kotlintestgradle.model.orderhistory.PayByData;
import com.kotlintestgradle.model.orderhistory.SingleUnitPriceData;
import com.kotlintestgradle.model.orderhistory.StoreLogoData;
import com.kotlintestgradle.remote.model.response.ecom.common.BasicSizeDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.FinalPriceListDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.OffersDataDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.ProductsDetails;
import com.kotlintestgradle.remote.model.response.ecom.getaddress.AddressListItemDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartPickupAddressDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartTaxDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.QuantityDetails;
import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.ProductOfferDetails;
import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.SubCategoryProductDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ColorsDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ReviewParametersDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.Supplier;
import com.kotlintestgradle.remote.model.response.ecom.pdp.SupplierDetails;
import com.kotlintestgradle.remote.model.response.ecom.similarproduct.GetSimilarProducts;
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.StoreLogo;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistAttributeDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistOfferDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistPackagingDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistProductDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistRatingDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistShippingDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryAccounting;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryCustomerDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryPlanDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderStatusDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.OrderTimeStampDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.PayByDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.SingleUnitPriceDetails;
import com.kotlintestgradle.remote.model.response.orderhistory.StoreLogoDetails;

import java.util.ArrayList;
import java.util.List;

public class CommonMapper {
  static ArrayList<ImageData> convertToImageDataList(ArrayList<ImagesDetails> imageDetList) {
    ArrayList<ImageData> imagesData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(imageDetList)) {
      for (ImagesDetails det : imageDetList) {
        ImageData data = new ImageData(det.getImageText(), det.getImage(), det.getThumbnail(),
            det.getMobile(), det.getDescription(), det.getTitle(), det.getKeyword());
        imagesData.add(data);
      }
    }
    return imagesData;
  }

  static OrderTimeStampData convertToTimeStampData(OrderTimeStampDetails details) {
    OrderTimeStampData timeStampData = null;
    if (details != null) {
      timeStampData = new OrderTimeStampData(details.getInDispatch(),
              details.getCreated(), details.getReadyForPickup(), details.getAccepted(),
              details.getCancelled(), details.getCompleted(), details.getPacked());
    }
    return timeStampData;
  }

  static OrderStatusData convertToStatusData(OrderStatusDetails statusDetails) {
    OrderStatusData orderStatusData = null;
    if (statusDetails != null) {
      orderStatusData = new OrderStatusData(statusDetails.getStatusText(),
              statusDetails.getUpdatedOnTimeStamp(), statusDetails.getUpdatedOn(),
              statusDetails.getStatus(),statusDetails.getUpdatedBy());
    }
    return orderStatusData;
  }

  static ArrayList<OrderHistProductData> convertToOrderHistProductData(
          ArrayList<OrderHistProductDetails> productDetails) {
    ArrayList<OrderHistProductData> productData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(productDetails)) {
      for (OrderHistProductDetails details : productDetails) {
        OrderHistProductData histProductData = new OrderHistProductData(
                details.getUpcNumber(), details.getColor(),
                CommonMapper.convertToTimeStampData(details.getTimestamps()),
                convertToUnitPriceData(details.getSingleUnitPrice()),
                convertToOfferData(details.getOfferDetails()),
                details.getProductOrderId(),
                CommonMapper.convertToAccountingData(details.getAccounting()),
                details.getAisle(), details.getPackageType(), details.getCentralProductId(),
                details.getAllowOrderOutOfStock(),
                convertToOrderShippingData(details.getShippingDetails()),
                details.getUnitId(), details.getProductDeliveryFee(), null,
                CommonMapper.convertToImage(details.getImages()), details.getBrandName(),
                convertToQuantityData(details.getQuantity()), details.getProductId(),
                details.getAddOns(), details.getPackageId(), details.getCurrencySymbol(),
                convertToPackageData(details.getPackaging()), details.getShelf(),
                details.getIsCentral(), details.getDirections(), details.getName(),
                convertToRatingData(details.getRattingData()),
                convertToAttributeDataList(details.getAttributes()),
                details.getCurrencyCode(), convertToStatusData(details.getStatus()),
                details.getInvoiceLink(), details.getReason(), details.getSplitProduct());
        productData.add(histProductData);
      }
    }
    return productData;
  }


  private static ArrayList<OrderHistAttributeData> convertToAttributeDataList(
          ArrayList<OrderHistAttributeDetails> attributeDetails) {
    ArrayList<OrderHistAttributeData> orderHistAttributeData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(attributeDetails)) {
      for (OrderHistAttributeDetails details : attributeDetails) {
        OrderHistAttributeData attributeData = new OrderHistAttributeData(
                details.getRateable(), details.getSearchable(),
                details.getAttriubteType(), details.getLinkedtounit(),
                details.getAttributeDataType(), details.getDropdownSelectType(),
                details.getAttributeId(),
                details.getValue(), details.getMeasurementUnit(), details.getAttrname());
        orderHistAttributeData.add(attributeData);
      }
    }
    return orderHistAttributeData;
  }

  private static OrderHistPackagingData convertToPackageData(
          OrderHistPackagingDetails packagingDetails) {
    OrderHistPackagingData orderHistPackagingData = null;
    if (packagingDetails != null) {
      orderHistPackagingData = new OrderHistPackagingData(packagingDetails.getUnitType(),
              packagingDetails.getPackingType());
    }
    return orderHistPackagingData;
  }

  static StoreLogoData convertToStoreLogoData(StoreLogoDetails logoDetails) {
    StoreLogoData storeLogoData = null;
    if (logoDetails != null) {
      storeLogoData = new StoreLogoData(logoDetails.getLogoImageThumb(),
              logoDetails.getLogoImageMobile());
    }
    return storeLogoData;
  }

  static com.kotlintestgradle.model.orderdetails.pharmacy.StoreLogoData convertToStoreLogoDataDetails(StoreLogo logoDetails) {
    com.kotlintestgradle.model.orderdetails.pharmacy.StoreLogoData storeLogoData = null;
    if (logoDetails != null) {
      storeLogoData = new com.kotlintestgradle.model.orderdetails.pharmacy.StoreLogoData(logoDetails.getLogoImageThumb(),
              logoDetails.getLogoImageMobile(), logoDetails.getLogoImageweb());
    }
    return storeLogoData;
  }

  private static OrderHistRatingData convertToRatingData(OrderHistRatingDetails ratingDetails) {
    OrderHistRatingData orderHistRatingData = null;
    if (ratingDetails != null) {
      orderHistRatingData = new OrderHistRatingData(ratingDetails.getReviewDescription(),
              ratingDetails.isRated(), ratingDetails.getRating(), ratingDetails.getReviewTitle());
    }
    return orderHistRatingData;
  }


  private static QuantityData convertToQuantityData(QuantityDetails quantityDetails) {
    Log.d("exe", "quantityDetails" + quantityDetails.getUnit());
    QuantityData quantityData = null;
    if (quantityDetails != null) {
      quantityData = new QuantityData(quantityDetails.getUnit(), quantityDetails.getValue());
    }
    return quantityData;
  }

  private static SingleUnitPriceData convertToUnitPriceData(SingleUnitPriceDetails details) {
    SingleUnitPriceData singleUnitPriceData = null;
    if (details != null) {
      singleUnitPriceData = new SingleUnitPriceData(details.getUnitPrice(),
              details.getOfferDiscount(), details.getTaxableAmount(), details.getFinalUnitPrice(),
              details.getAddOnsAmount(), CommonMapper.convertCartTaxDataList(details.getTax()),
              details.getSubTotal(),
              details.getTaxAmount());
    }
    return singleUnitPriceData;
  }

  static OrderHistoryPlanData convertToPlanData(OrderHistoryPlanDetails planDetails) {
    OrderHistoryPlanData historyPlanData = null;
    if (planDetails != null) {
      historyPlanData = new OrderHistoryPlanData(planDetails.getAppCommissionTypeText(),
              planDetails.getAppCommission(), planDetails.getName(),
              planDetails.getAppCommissionType());
    }
    return historyPlanData;
  }

  private static OrderHistOfferData convertToOfferData(OrderHistOfferDetails offerDetails) {
    OrderHistOfferData orderHistOfferData = null;
    if (offerDetails != null) {
      orderHistOfferData = new OrderHistOfferData(offerDetails.getOfferTitle(),
              offerDetails.getOfferId(), offerDetails.getOfferType(),
              offerDetails.getOfferValue());
    }
    return orderHistOfferData;
  }

  private static OrderHistShippingData convertToOrderShippingData(
          OrderHistShippingDetails shippingDetails) {
    OrderHistShippingData shippingData = null;
    if (shippingDetails != null) {
      shippingData = new OrderHistShippingData(shippingDetails.getName(),
              shippingDetails.getId(), shippingDetails.getTrackingId());
    }
    return shippingData;
  }

  static OrderHistoryAccountingData convertToAccountingData(OrderHistoryAccounting accounting) {
    OrderHistoryAccountingData accountingData = null;
    if (accounting != null) {
      accountingData = new OrderHistoryAccountingData(accounting.getUnitPrice(),
              accounting.getTaxableAmount(), accounting.getFinalUnitPrice(),
              accounting.getAppEarningWithTax(), convertToPaidByData(accounting.getPayBy()),
              accounting.getCurrencySymbol(), accounting.getAddOnsAmount(),
              CommonMapper.convertCartTaxDataList(accounting.getTax()),
              accounting.getSubTotal(), accounting.getStoreEarning(),
              accounting.getOfferDiscount(), accounting.getDeliveryFee(), accounting.getTaxAmount(),
              accounting.getPromoDiscount(), accounting.getCurrencyCode(), accounting.getFinalTotal(),
              accounting.getAppEarning());
    }
    return accountingData;
  }

  private static PayByData convertToPaidByData(PayByDetails payByDetails) {
    PayByData payByData = null;
    if (payByDetails != null) {
      payByData = new PayByData(payByDetails.getWallet(), payByDetails.getCash(),
              payByDetails.getCard());
    }
    return payByData;
  }


  static OrderHistoryCustomerData convertCustomerData(
          OrderHistoryCustomerDetails details) {
    OrderHistoryCustomerData customerData = null;
    if (details != null) {
      customerData = new OrderHistoryCustomerData(details.getFirstName(),
              details.getLastName(), details.getCountryCode(),
              details.getUserTypeText(), details.getMobile(), details.getId(), details.getUserType(),
              details.getMqttTopic(), details.getFcmTopic(), details.getEmail());
    }
    return customerData;
  }


  static ImageData convertToImagesData(ImagesDetails det) {
    if (det != null) {
      return new ImageData(det.getImageText(), det.getImage(), det.getThumbnail(),
          det.getMobile(), det.getDescription(), det.getTitle(), det.getKeyword());
    } else {
      return new ImageData();
    }
  }

  static SupplierData convertSupplierData(Supplier supplier) {
    SupplierData supplierData = null;
    if (supplier != null) {
      supplierData = new SupplierData(supplier.getProductId(),
          supplier.getRetailerQty(), supplier.getRetailerPrice(), supplier.getCurrencySymbol(),
          supplier.getDistributorPrice(), supplier.getCurrency(),
          supplier.getId(), supplier.getDistributorQty(), supplier.getSupplierName(),
          supplier.getRating(), supplier.getTotalRating(), getSellerData(supplier.getSupplier()),
          getFinalPriceListData(supplier.getFinalPriceList()),
          getReviewParamDataList(supplier.getReviewParameter()), supplier.getSellerSince());
    }
    return supplierData;
  }

  static FinalPriceListData convertToFinalPriceData(FinalPriceListDetails details) {
    FinalPriceListData priceListData = null;
    if (details != null) {
      priceListData = new FinalPriceListData(details.getDiscountPrice(),
          details.getFinalPrice(), details.getBasePrice(),
          details.getDiscountPercentage(), details.getMou());
    }
    return priceListData;
  }

  private static ArrayList<ColorsData> convertToColorData(ArrayList<ColorsDetails> colorsDetList) {
    ArrayList<ColorsData> colorsData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(colorsDetList)) {
      for (ColorsDetails details : colorsDetList) {
        ColorsData data = new ColorsData(details.getImage(), details.getIsPrimary(),
            details.getChildProductId(), details.getName(), details.getRgb());
        colorsData.add(data);
      }
    }
    return colorsData;
  }

  private static FinalPriceListData getFinalPriceListData(
      FinalPriceListDetails finalPriceListDetails) {
    FinalPriceListData finalPriceListData = null;
    if (finalPriceListDetails != null) {
      finalPriceListData = new FinalPriceListData(finalPriceListDetails.getDiscountPrice(),
          finalPriceListDetails.getFinalPrice(), finalPriceListDetails.getBasePrice(),
          finalPriceListDetails.getDiscountPrice(), finalPriceListDetails.getMou());
    }
    return finalPriceListData;
  }

  private static ArrayList<ReviewParameterData> getReviewParamDataList(
      ArrayList<ReviewParametersDetails> reviewParametersDetails) {
    ArrayList<ReviewParameterData> reviewParameterData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(reviewParametersDetails)) {
      for (ReviewParametersDetails details : reviewParametersDetails) {
        ReviewParameterData data = new ReviewParameterData(details.getAttributeId(),
            details.getName(),
            details.getRating(), details.getTotalStarRating());
        reviewParameterData.add(data);
      }
    }
    return reviewParameterData;
  }

  private static SellerData getSellerData(SupplierDetails supplierDetails) {
    SellerData sellerData = null;
    if (supplierDetails != null) {
      sellerData = new SellerData(supplierDetails.getRetailerPrice(),
          supplierDetails.getSupplierName(), supplierDetails.getRating(),
          supplierDetails.getCurrency(), supplierDetails.getProductId());
    }
    return sellerData;
  }

  private static ArrayList<SizeData> convertToMiniSizeData(ArrayList<BasicSizeDetails> sizeList) {
    ArrayList<SizeData> sizeData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(sizeList)) {
      for (BasicSizeDetails details : sizeList) {
        sizeData.add(
            new SizeData(details.getSize(), details.isPrimary(), details.getChildProductId()));
      }
    }
    return sizeData;
  }

  static ArrayList<ImageData> convertToImageData(ArrayList<ImagesDetails> imageDetList) {
    ArrayList<ImageData> imagesData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(imageDetList)) {
      for (ImagesDetails det : imageDetList) {
        imagesData.add(convertToImage(det));
      }
    }
    return imagesData;
  }

  public static ImageData convertToImage(ImagesDetails details) {
    ImageData data = null;
    if (details != null) {
      data = new ImageData(details.getLarge(), details.getExtraLarge(), details.getSmall(),
          details.getAltText(), details.getMedium());
    }
    return data;
  }

  public static ArrayList<CategoryData> convertToSecApiCategoryData(
      ArrayList<SubCategoryProductDetails> productDetails, List<UserCart> cartList) {

    ArrayList<CategoryData> categoryDataList = new ArrayList<>();

    if (!DataUtils.isEmptyArray(productDetails)) {

      for (SubCategoryProductDetails details : productDetails) {
        CategoryOfferData offer = null;
        if (details.getOffers() != null) {
          ProductOfferDetails offerDetails = details.getOffers();
          offer = new CategoryOfferData(offerDetails.getStartDateTimeISO(),
              offerDetails.getApplicableOnStatus(),
              CommonMapper.convertToImage(offerDetails.getImages()),
              offerDetails.getOfferName() != null ? offerDetails.getOfferName().getEn() : "",
              offerDetails.getDiscountValue() + "",
              offerDetails.getEndDateTimeISO(), offerDetails.getDiscountType(),
              offerDetails.getEndDateTime(),
              offerDetails.getGlobalClaimCount(),
              offerDetails.getStartDateTime(), offerDetails.getMinimumPurchaseQty(),
              offerDetails.getPerUserLimit(),
              offerDetails.getStatusString(), offerDetails.getOfferId(), offerDetails.getOfferFor(),
              offerDetails.getApplicableOn(), offerDetails.getStatus());
        }
        CategoryData categoryData;
        categoryData = new CategoryData(offer,
            details.getAvailableQuantity(),
            details.getBrandName(),
            CommonMapper.convertToImageData(details.getImages()),
            details.getParentProductId(), details.getChildProductId(),
            details.getCurrencySymbol(), details.getTotalStarRating(),
            details.getProductName(), CommonMapper.convertSupplierData(details.getSupplier()),
            details.getOutOfStock(), details.getUnitId(),
            details.getCurrency(), details.getDiscountType(),
            CommonMapper.convertToFinalPriceData(details.getFinalPriceList()));
        categoryDataList.add(categoryData);
      }
    }
    if (!DataUtils.isEmptyArray(cartList)) {
      for (UserCart cart : cartList) {
        SubCategoryProductDetails categoryData = new SubCategoryProductDetails(cart.productId);
        if (productDetails.contains(categoryData)) {
          int index = productDetails.indexOf(categoryData);
          categoryDataList.get(index).setCartQuantity(cart.quantity);
        }
      }
    }
    return categoryDataList;
  }

  public static AddressListItemData convertToAddressData(AddressListItemDetails details) {
    if (details != null) {
      AddressListItemData itemData = new AddressListItemData(details.getMobileNumberCode(),
          details.getCountry(), details.getPincode(), details.getCity(),
          details.getMobileNumber(),
          details.getFlatNumber(), details.getLatitude(), details.getAlternatePhoneCode(),
          details.getAlternatePhone(), details.getLocality(),
          details.getPlaceId(), details.getCreatedTimeStamp(),
          details.getUserId(), details.getCreatedIsoDate(), details.getName(),
          details.getAddLine1(), details.get_id(), details.getAddLine2(),
          details.getState(), details.getUserType(), details.getLandmark(), details.getTaggedAs(),
          details.getLongitude(), details.isDefaultAd(), details.getTagged(),details.getCityId(),details.getCountryId());
      return itemData;
    } else {
      return null;
    }
  }

  static ArrayList<CartTaxData> convertCartTaxDataList(
      ArrayList<CartTaxDetails> cartTaxDetails) {
    ArrayList<CartTaxData> cartTaxData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(cartTaxDetails)) {
      for (CartTaxDetails details : cartTaxDetails) {
        Log.d("exe", "details" + details.getTaxName());
        cartTaxData.add(convertToTaxData(details));
      }
    }
    return cartTaxData;
  }

  private static CartTaxData convertToTaxData(CartTaxDetails cartTaxDetails) {
    CartTaxData cartTaxData = null;
    if (cartTaxDetails != null) {
      cartTaxData = new CartTaxData(cartTaxDetails.getTotalValue(),
          cartTaxDetails.getTaxValue(), cartTaxDetails.getTaxId(), cartTaxDetails.getTaxName());
    }
    return cartTaxData;
  }

  public static CartPickupAddressData convertToPickUpAddressData(
      CartPickupAddressDetails addressDetails) {
    CartPickupAddressData cartPickupAddressData = null;
    if (addressDetails != null) {
      cartPickupAddressData = new CartPickupAddressData(addressDetails.getCountry(),
          addressDetails.getAddress(), addressDetails.getLocality(),
          addressDetails.getCityId(),
          addressDetails.getSublocality_level_2(), addressDetails.getSublocality_level_1(),
          addressDetails.getRoute(), addressDetails.getAdministrative_area_level_2(),
          addressDetails.getCityName(), addressDetails.getAdministrative_area_level_1(),
          addressDetails.getPostal_code(),addressDetails.getAddressArea());
    }
    return cartPickupAddressData;
  }

  public static CartPickupAddressData convertToPickUpAddressDataEcom(
          CartPickupAddressDetails addressDetails) {
    CartPickupAddressData cartPickupAddressData = null;
    if (addressDetails != null) {
      cartPickupAddressData = new CartPickupAddressData(addressDetails.getCountry(),
              addressDetails.getAddress(), addressDetails.getLocality(),
              addressDetails.getCityId(),
              addressDetails.getSublocality_level_2(), addressDetails.getSublocality_level_1(),
              addressDetails.getRoute(), addressDetails.getAdministrative_area_level_2(),
              addressDetails.getCityName(), addressDetails.getAdministrative_area_level_1(),
              addressDetails.getPostal_code(),addressDetails.getAddressArea());
    }
    return cartPickupAddressData;
  }

  public static ArrayList<SupplierData> convertToSupplierDataList(ArrayList<Supplier> suppliers) {
    ArrayList<SupplierData> supplierData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(suppliers)) {
      for (Supplier supplier : suppliers) {
        supplierData.add(convertSupplierData(supplier));
      }
    }
    return supplierData;
  }

  public SimilarProductsData map(GetSimilarProducts products, int storeType) {
    return new SimilarProductsData(null, products.getPenCount(), convertToProductData(products.getProducts(), storeType));
  }

  static ArrayList<ProductsData> convertToProductData(ArrayList<ProductsDetails> detailsList,
      int storeType) {
    ArrayList<ProductsData> dataList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(detailsList)) {
      for (ProductsDetails details : detailsList) {
        OffersListData offersData = null;
        if (details.getOffers() != null) {
          OffersDataDetails offersDetails = details.getOffers();
          ImageData imageData = null;
          if (offersDetails.getImagesDetails() != null) {
            imageData = new ImageData(offersDetails.getImagesDetails().getImageText(),
                offersDetails.getImagesDetails().getImage(),
                offersDetails.getImagesDetails().getThumbnail(),
                offersDetails.getImagesDetails().getMobile(),
                offersDetails.getImagesDetails().getDescription(),
                offersDetails.getImagesDetails().getTitle(),
                offersDetails.getImagesDetails().getKeyword());
          }
          offersData = new OffersListData(imageData, details.getOffers().getOffername(),
              details.getOffers().getOfferId(), details.getOffers().getDiscountValue());
        }
        Supplier supplier = details.getSuppliers();
        SupplierData supplierData = null;
        if (supplier != null) {
          supplierData = new SupplierData(supplier.getProductId(), supplier.getRetailerQty(),
              supplier.getRetailerPrice(), supplier.getCurrencySymbol(),
              supplier.getDistributorPrice(),
              supplier.getCurrency(), supplier.getId(), supplier.getDistributorQty(),
              supplier.getSupplierName(), supplier.getRating(), supplier.getTotalRating(),
              getSellerData(supplier.getSupplier()),
              getFinalPriceListData(supplier.getFinalPriceList()),
              getReviewParamDataList(supplier.getReviewParameter()), supplier.getSellerSince());
        }
        FinalPriceListData priceListData = null;
        if (details.getFinalPriceList() != null) {
          priceListData = new FinalPriceListData(
              details.getFinalPriceList().getDiscountPrice(),
              details.getFinalPriceList().getFinalPrice(),
              details.getFinalPriceList().getBasePrice(),
              details.getFinalPriceList().getDiscountPercentage(),
              details.getFinalPriceList().getMou());
        }
        ProductsData data;
        switch (storeType) {
          default:
            data = new ProductsData(offersData, details.getAvailableQuantity(),
                details.getBrandName(), convertToImageData(details.getImages()),
                details.getParentProductId(),
                supplierData, details.getStores(), details.getChildProductId(), details.getStoreCount(),
                details.getDiscountPrice(), details.getCurrencySymbol(), details.getTotalStarRating(),
                convertToColorData(details.getColors()), details.getProductName(), details.getMou(),
                convertToMiniSizeData(details.getSizes()), details.getMinimumPurchaseUnit(),
                details.getOutOfStock(),
                details.getUnitId(), details.getDiscountType(), details.getCurrency(),
                details.getMouUnit(),
                priceListData, details.getAvgRating());
            break;
        }
        dataList.add(data);
      }
    }
    return dataList;
  }


}
