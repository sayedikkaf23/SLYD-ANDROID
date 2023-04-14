package com.kotlintestgradle.repository;

import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import kotlin.Triple;

public interface AddressHandlerRepository {
  boolean isAddressListEmpty();

  AddressListItemData getDefaultAddress();

  boolean isCurrentAddressEmpty();

  Triple<Double,Double,String> getCurrentLocation();

  void storeCurrentLocation(double latitude, double longitude, String cityName);

  void storeDineCurrentLocation(double latitude, double longitude, String address);

  Triple<Double, Double, String>  getDineCurrentLocation();
}
