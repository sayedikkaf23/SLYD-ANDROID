package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.cache.entity.UserAddress;
import com.kotlintestgradle.data.repository.observable.DefaultAddressObservable;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.getaddress.AddressData;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.remote.model.response.ecom.getaddress.AddressDetails;
import com.kotlintestgradle.remote.model.response.ecom.getaddress.AddressListItemDetails;
import java.util.ArrayList;

public class GetAddressMapper {
  public AddressData mapper(AddressDetails details) {
    return new AddressData(convertToAddressListData(details.getData()), details.getMessage());
  }

  private ArrayList<AddressListItemData> convertToAddressListData(
      ArrayList<AddressListItemDetails> detailsList) {
    ArrayList<AddressListItemData> listData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(detailsList)) {
      for (AddressListItemDetails details : detailsList) {
        listData.add(CommonMapper.convertToAddressData(details));
      }
    }
    return listData;
  }

  public void insertToDatabase(AddressDetails details, DatabaseManager databaseManager) {
    if (details != null) {
      databaseManager.address().insertAddresses(dbMapper(details.getData()));
    }
  }

  private ArrayList<UserAddress> dbMapper(
      ArrayList<AddressListItemDetails> detailsList) {
    ArrayList<UserAddress> listData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(detailsList)) {
      for (AddressListItemDetails details : detailsList) {
        if (details.isDefaultAd()) {
          DefaultAddressObservable.getInstance().postData(
              new AddressListItemData(details.get_id()));
        }
        UserAddress itemData = new UserAddress(details.get_id(), details.getMobileNumberCode(),
            details.getCountry(), details.getPincode(), details.getCity(),
            details.getMobileNumber(),
            details.getFlatNumber(), details.getLatitude(), details.getAlternatePhoneCode(),
            details.getAlternatePhone(),
            details.getLocality(), details.getPlaceId(), details.getCreatedTimeStamp(),
            details.getUserId(), details.getCreatedIsoDate(),
            details.getName(), details.getAddLine1(), details.getAddLine2(), details.getState(),
            details.getUserType(),
            details.getLandmark(), details.getTaggedAs(), details.getTagged(),
            details.isDefaultAd() ? "1" : "0", details.getLongitude(),details.getCityId(),details.getCountryId());
        listData.add(itemData);
      }
    }
    return listData;
  }
}
