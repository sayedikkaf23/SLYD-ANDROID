package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import kotlin.Triple;

public interface AddressHandler {
  boolean isAddressListEmpty();

  AddressListItemData getDefaultAddress();

  boolean isCurrentAddressEmpty();

  void storeCurrentLocation(double latitude, double longitude, String cityName);

  void storeDineCurrentLocation(double latitude, double longitude, String address);

  Triple<Double, Double, String>  getDineCurrentLocation();

  Triple<Double, Double, String> getCurrentLocation();
}
