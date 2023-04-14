package com.kotlintestgradle.data.mapper;

import static com.kotlintestgradle.remote.RemoteConstants.LOWER_BOUND;

import android.text.TextUtils;
import android.util.Log;
import com.customer.domain.model.getcart.SelectedDataRecord;
import com.customer.remote.http.model.response.getcart.SelectedAddOns;
import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.cache.entity.UserCart;
import com.kotlintestgradle.data.repository.observable.CartDataObservable;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.LogoImageData;
import com.kotlintestgradle.model.ecom.getcart.CartAccoutingData;
import com.kotlintestgradle.model.ecom.getcart.CartAttributesData;
import com.kotlintestgradle.model.ecom.getcart.CartData;
import com.kotlintestgradle.model.ecom.getcart.CartDeliveryData;
import com.kotlintestgradle.model.ecom.getcart.CartLogsData;
import com.kotlintestgradle.model.ecom.getcart.CartOfferData;
import com.kotlintestgradle.model.ecom.getcart.CartPickupAddressData;
import com.kotlintestgradle.model.ecom.getcart.CartProductItemData;
import com.kotlintestgradle.model.ecom.getcart.CartSellerData;
import com.kotlintestgradle.model.ecom.getcart.CartUserData;
import com.kotlintestgradle.model.ecom.getcart.Logo;
import com.kotlintestgradle.model.ecom.getcart.QuantityData;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartAccountingDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartAttributesDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartDeliveryDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartLogsDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartOfferDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartPickupAddressDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartProductItemDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartSellerDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartUserDetails;
import com.kotlintestgradle.remote.model.response.ecom.getcart.LogoImages;
import com.kotlintestgradle.remote.model.response.ecom.getcart.Logos;
import com.kotlintestgradle.remote.model.response.ecom.getcart.QuantityDetails;
import java.util.ArrayList;
import java.util.HashSet;

public class CartMapper {
  DatabaseManager mDatabaseManager;
  int storeTypeId=-1;

  public CartData mapper(CartDetails cartDetails, DatabaseManager databaseManager, int storeType) {
    mDatabaseManager = databaseManager;
    return new CartData(cartDetails.getNotes(), cartDetails.getCartTotalTax(),
        cartDetails.getStoreTypeId(), cartDetails.getSubTotal(),
        convertToCartLogData(cartDetails.getCartLogs()), cartDetails.getCartType(),
        cartDetails.getSeqId(), CommonMapper.convertCartTaxDataList(cartDetails.getTaxData()),
        cartDetails.getStoreType(), cartDetails.getGrandTotal(), cartDetails.getCurrencySymbol(),
        cartDetails.getStoreCategory(),
        CommonMapper.convertCartTaxDataList(cartDetails.getTaxList()),
        cartDetails.getDeliveryFee(), cartDetails.getUserTypeMsg(),
        cartDetails.getStoreCategoryId(), cartDetails.getSubtotal(),
        cartDetails.getTotalCartquantity(), cartDetails.getCartTypeInTxt(),
        cartDetails.getTotalDiscount(), cartDetails.get_id(), cartDetails.getUserType(),
        cartDetails.getTotalAppliedTaxOnCart(), convertToCartUserData(cartDetails.getUser()),
        cartDetails.getCurrencyCode(), convertToCartSellerData(cartDetails.getSellers(), storeType),
        convertToAccountAddressData(cartDetails.getAccounting()));
  }

  private CartLogsData convertToCartLogData(CartLogsDetails cartLogsDetails) {
    CartLogsData cartLogsData = null;
    if (cartLogsDetails != null) {
      cartLogsData = new CartLogsData(cartLogsDetails.getTimeStampIso(),
          cartLogsDetails.getStatusMsg(), cartLogsDetails.getUserType(),
          cartLogsDetails.getUserId(), cartLogsDetails.getStatus(), cartLogsDetails.getTimestamp());
    }
    return cartLogsData;
  }

  private CartUserData convertToCartUserData(CartUserDetails cartUserDetails) {
    CartUserData cartUserData = null;
    if (cartUserDetails != null) {
      cartUserData = new CartUserData(cartUserDetails.getFirstName(), cartUserDetails.getCity(),
          cartUserDetails.getCountryCode(), cartUserDetails.getMobile(),
          cartUserDetails.getUserIp(), cartUserDetails.getUserPostCode(),
          cartUserDetails.getUserId(), cartUserDetails.getUserLong(),
          cartUserDetails.getUserLat(), cartUserDetails.getEmail());
    }
    return cartUserData;
  }

  private ArrayList<CartSellerData> convertToCartSellerData(
      ArrayList<CartSellerDetails> cartSellerDetails, int storeType) {
    ArrayList<CartSellerData> cartSellerData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(cartSellerDetails)) {
      for (CartSellerDetails details : cartSellerDetails) {
        CartSellerData data = new CartSellerData(details.getTargetAmtForFreeDelivery(),
                    details.getSellerCartValue(), details.getFreeDelivery(),
                    details.getFullFillMentCenterId(), details.getMinOrder(), details.getDriverType(),
                    convertToCartProductData(details.getProducts(), storeType), details.getFullfilledBy(),
                    CommonMapper.convertToPickUpAddressData(details.getPickupAddress()),
                    details.getAutoDispatch(), details.getAutoAcceptOrders(),
                    details.getIsInventoryCheck(), details.getName(),details.getAreaName(),
                    details.getCityName(),convertLogoImageToData(details.getLogo()),
                details.getSupportedOrderTypes(),details.isStoreIsOpen(),details.isMinOrderSatisfy(),details.isFreeDeliverySatisfy());
        cartSellerData.add(data);
      }
    }
    return cartSellerData;
  }

  private Logo convertToLogo(Logos logo) {
    return new Logo(logo.getLogoImageMobile(), logo.getLogoImageThumb(), logo.getLogoImageWeb());
  }

  private CartPickupAddressData convertToPickUpAddressData(
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

  private CartAccoutingData convertToAccountAddressData(
      CartAccountingDetails cartAccountingDetails) {
    CartAccoutingData cartAccoutingData = null;
    if (cartAccountingDetails != null) {
      cartAccoutingData = new CartAccoutingData(cartAccountingDetails.getUnitPrice(),
          cartAccountingDetails.getOfferDiscount(), cartAccountingDetails.getTaxableAmount(),
          cartAccountingDetails.getDeliveryFee(),
          cartAccountingDetails.getFinalUnitPrice(), cartAccountingDetails.getAddOnsAmount(),
          CommonMapper.convertCartTaxDataList(cartAccountingDetails.getTax()),
          cartAccountingDetails.getSubTotal(),
          cartAccountingDetails.getTaxAmount(), cartAccountingDetails.getPromoDiscount(),
          cartAccountingDetails.getFinalTotal(),cartAccountingDetails.getTotalQuantity());
    }
    return cartAccoutingData;
  }

  private CartOfferData convertToOfferData(
      CartOfferDetails cartOfferDetails) {
    CartOfferData cartOfferData = null;
    if (cartOfferDetails != null) {
      cartOfferData = new CartOfferData(cartOfferDetails.getOfferType(),
          cartOfferDetails.getOfferId(), cartOfferDetails.getOfferValue(),
          cartOfferDetails.getOfferTitle());
    }
    return cartOfferData;
  }

  private ArrayList<CartProductItemData> convertToCartProductData(
      ArrayList<CartProductItemDetails> cartProductItemDetails, int storeType) {
    ArrayList<CartProductItemData> cartProductItemData = new ArrayList<>();
    ArrayList<UserCart> cartList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(cartProductItemDetails) && cartProductItemDetails.size() > 0) {
      for (CartProductItemDetails details : cartProductItemDetails) {
        int quantity = details.getQuantity() != null && TextUtils.isDigitsOnly(
            details.getQuantity().getValue()) ? Integer.parseInt(details.getQuantity().getValue())
            : LOWER_BOUND;
        UserCart cart = new UserCart(details.get_id(), details.getStoreId(), details.getName(),
            quantity, storeType);
        cartList.add(cart);
        CartProductItemData data;
            data = new CartProductItemData(details.getUpcNumber(),
                    details.getColor(), details.getOriginalPrice(),
                    details.getDiscount(), details.getFinalPrice(),
                    details.getAisle(), details.getPackageType(), details.getCentralProductId(),
                    details.getOfferType(), details.getOfferValue(),
                    convertToCartDelData(details.getDeliveryDetails()),
                    details.getUnitId(), details.getStoreName(), details.getInStock(),
                    details.getOfferTitle(), CommonMapper.convertToImage(details.getImages()),
                    details.getBrandName(), convertToQuantityData(details.getQuantity()),
                    details.getOriginalPriceTotal(), /*details.getAddOns(),*/ details.getCurrencySymbol(),
                    CommonMapper.convertCartTaxDataList(details.getTax()), details.getStoreId(),
                    CommonMapper.convertCartTaxDataList(details.getProductTaxArray()), details.getShelf(),
                    details.getOfferDiscount(), details.getIsCentral(),
                    details.getTotalAppliedTaxOnProduct(),
                    details.getDiscountedPrice(), details.getDirections(), details.getName(),
                    details.getOfferId(), convertToCartAttributeData(details.getAttributes()),
                    details.get_id(), details.getCurrencyCode(),
                    convertToAccountAddressData(details.getAccounting()),
                    convertToOfferData(details.getOfferDetails()) /*details.getAvailableQuantity()*/,
                    details.getAddToCartOnId(),details.getAddOnAvailable(),details.isContainsMeat(),
                    convertSelectedAddOnsData(details.getSelectedAddOns()), details.getAvailableQuantity());
               cartProductItemData.add(data);
      }
    }
    if (storeTypeId==1){
      for (int i =0;i<cartProductItemData.size();i++){
        int quantityNet=0;
        Double amount=0.0;
        if (cartProductItemData.get(i).getAccounting().getUnitPrice()!=null && cartProductItemData.get(i).getAccounting().getAddOnsAmount()!=null &&
                !cartProductItemData.get(i).getAccounting().getUnitPrice().trim().isEmpty() && !cartProductItemData.get(i).getAccounting().getAddOnsAmount().trim().isEmpty()){
          amount= (Double.parseDouble(cartProductItemData.get(i).getAccounting().getFinalUnitPrice())+Double.parseDouble(
                  cartProductItemData.get(i).getAccounting().getAddOnsAmount()))/Integer.parseInt(cartProductItemData.get(i).getQuantity().getValue());

        }
        if (cartProductItemData.get(i).getQuantity().getValue()!=null && !cartProductItemData.get(i).getQuantity().getValue().isEmpty()){
          quantityNet=Integer
                  .parseInt(cartProductItemData.get(i).getQuantity().getValue());
        }
        HashSet<String> addOns=new HashSet<String>();
        if (!DataUtils.isEmptyArray(cartProductItemData.get(i).getSelectedAddOns()  )) {
          for (int j = 0; j < cartProductItemData.get(i).getSelectedAddOns().size(); j++) {
            addOns.add(cartProductItemData.get(i).getSelectedAddOns().get(j).getName());
          }
        }
        String prodId="";

          prodId=cartProductItemData.get(i).getId();

      }
    }else {
      mDatabaseManager.cart().insertCartItems(cartList);
    }
    CartDataObservable.getInstance().postData(new CartData(6, "", 0));
    return cartProductItemData;
  }

  private CartDeliveryData convertToCartDelData(CartDeliveryDetails cartDeliveryDetails) {
    CartDeliveryData cartDeliveryData = null;
    if (cartDeliveryDetails != null) {
      cartDeliveryData = new CartDeliveryData(cartDeliveryDetails.getDeliveryFee(),
          cartDeliveryDetails.getDeliveryByDeliveryPartner(),
          cartDeliveryDetails.getDeliveryByFleetDriver(), cartDeliveryDetails.getTime());
    }
    return cartDeliveryData;
  }

  private QuantityData convertToQuantityData(QuantityDetails quantityDetails) {
    QuantityData quantityData = null;
    if (quantityDetails != null) {
      quantityData = new QuantityData(quantityDetails.getUnit(), quantityDetails.getValue());
    }
    return quantityData;
  }

  private ArrayList<CartAttributesData> convertToCartAttributeData(
      ArrayList<CartAttributesDetails> cartAttributesDetails) {
    ArrayList<CartAttributesData> cartAttributesData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(cartAttributesDetails)) {
      for (CartAttributesDetails details : cartAttributesDetails) {
        CartAttributesData data = new CartAttributesData(details.getMeasurementUnitName(),
                details.getCustomizable(), details.getRateable(), details.getLinkedtounit(),
                details.getAttrname(), details.getSearchable(), details.getAttributeDataType(),
                details.getMeasurementUnit(), details.getDropdownSelectType(), details.getAttributeId(),
                details.getAttriubteType(), details.isIsAddOn(), details.getValue());
        cartAttributesData.add(data);
      }
    }
    return cartAttributesData;
  }

  private ArrayList<SelectedDataRecord> convertSelectedAddOnsData(
      ArrayList<SelectedAddOns> cartAttributesDetails) {
    ArrayList<SelectedDataRecord> selectedDataRecords = new ArrayList<>();
    if (!DataUtils.isEmptyArray(cartAttributesDetails)) {
      for (SelectedAddOns details : cartAttributesDetails) {
        SelectedDataRecord data = new SelectedDataRecord(details.getName(), details.getId(),details.getAddOnId(),details.getAddOnName(),details.getPrice());
        selectedDataRecords.add(data);
      }
    }
    return selectedDataRecords;
  }

  LogoImageData convertLogoImageToData(LogoImages logoImages)  {

    return new LogoImageData(
        logoImages.getLogoImageMobile(), logoImages.getLogoImageThumb(), logoImages.getLogoImageweb()
    );
  }
}
