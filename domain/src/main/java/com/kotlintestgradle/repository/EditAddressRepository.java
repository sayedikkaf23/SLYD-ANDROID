package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.address.EditAddressUseCase;
import io.reactivex.Single;

public interface EditAddressRepository {

  Single<EditAddressUseCase.ResponseValues> editAddress(String name,
      String mobileNumber,
      String mobileNumberCode, String locality, String addLine1, String addLine2,
      String flatNumber, String landmark, String city, String state, String country,
      String placeId, String pincode, String latitude, String longitude, String taggedAs,
      String addressId, int tagged, boolean isDefault, String cityId, String countryId);

}
