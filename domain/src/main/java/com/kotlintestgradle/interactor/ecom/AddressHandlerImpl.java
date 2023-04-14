package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.repository.AddressHandlerRepository;
import javax.inject.Inject;
import kotlin.Triple;

public class AddressHandlerImpl implements AddressHandler {
  private AddressHandlerRepository mRepository;

  @Inject
  public AddressHandlerImpl(AddressHandlerRepository permissionRepository) {
    this.mRepository = permissionRepository;
  }

  @Override
  public boolean isAddressListEmpty() {
    return mRepository.isAddressListEmpty();
  }

  @Override
  public AddressListItemData getDefaultAddress() {
    return mRepository.getDefaultAddress();
  }

  @Override
  public boolean isCurrentAddressEmpty() {
    return mRepository.isCurrentAddressEmpty();
  }

  @Override
  public void storeCurrentLocation(double latitude, double longitude, String cityName) {
    mRepository.storeCurrentLocation(latitude, longitude, cityName);
  }

  @Override
  public void storeDineCurrentLocation(double latitude, double longitude, String address) {
    mRepository.storeDineCurrentLocation(latitude,longitude,address);
  }

  @Override
  public Triple<Double, Double, String> getDineCurrentLocation() {
    return mRepository.getDineCurrentLocation();
  }

  @Override
  public Triple<Double, Double, String> getCurrentLocation() {
    return mRepository.getCurrentLocation();
  }
}
