package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.model.ecom.location.IpAddressToLocationData;
import com.kotlintestgradle.model.ecom.location.LocationData;
import com.kotlintestgradle.remote.model.response.ecom.location.IpAddressToLocationDetails;

public class IpAddressToLocationMapper {

  public IpAddressToLocationData mapper(IpAddressToLocationDetails details) {
    return new IpAddressToLocationData(details.getIp(), details.getCity(), details.getRegion(),
        details.getCountry(), details.getPostal(), details.getTimezone(),
        details.getLocationData() != null ? new LocationData(
            details.getLocationData().getLatitude(),
            details.getLocationData().getLongitude(), "") : null);
  }
}
