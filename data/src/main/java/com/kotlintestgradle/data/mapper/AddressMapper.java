package com.kotlintestgradle.data.mapper;

import static com.kotlintestgradle.data.utils.DataConstants.DEFAULT_TYPE;

import com.kotlintestgradle.cache.entity.RecentAddress;
import com.kotlintestgradle.cache.entity.UserAddress;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.location.PlacesData;
import com.kotlintestgradle.remote.model.response.ecom.placesuggestion.PlacesDetails;
import java.util.ArrayList;
import java.util.List;

public class AddressMapper {
  public AddressListItemData mapper(UserAddress userAddress) {
    if (userAddress != null) {
      return new AddressListItemData(userAddress.mobileNumberCode,
          userAddress.country, userAddress.pinCode, userAddress.city, userAddress.mobileNumber,
          userAddress.flatNumber, userAddress.latitude, userAddress.alternatePhoneCode,
          userAddress.alternatePhone, userAddress.locality, userAddress.placeId,
          userAddress.createdTimeStamp, userAddress.userId, userAddress.createdIsoDate,
          userAddress.name, userAddress.addLine1, userAddress.id, userAddress.addLine2,
          userAddress.state, userAddress.userType, userAddress.landmark,
          userAddress.taggedAs, userAddress.longitude, userAddress.defaultAd.equals(DEFAULT_TYPE),
          userAddress.tagged,userAddress.cityId,userAddress.countryId);
    }
    return new AddressListItemData();
  }

  public ArrayList<PlacesData> mapper(List<RecentAddress> recentAddresses) {
    ArrayList<PlacesData> placesData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(recentAddresses)) {
      for (RecentAddress address : recentAddresses) {
        PlacesData data = new PlacesData(address.address, address.addressId, address.latitude,
            address.longitude);
        placesData.add(data);
      }
    }
    return placesData;
  }

  public ArrayList<PlacesData> mapSuggestion(ArrayList<PlacesDetails> placesDetails) {
    ArrayList<PlacesData> placesData = new ArrayList<>();
    if (placesDetails != null) {
      for (PlacesDetails details : placesDetails) {
        PlacesData data = new PlacesData(details.getPlaceId(), details.getFullAddress(),
            details.getLat(), details.getLang());
        placesData.add(data);
      }
    }
    return placesData;
  }
}
