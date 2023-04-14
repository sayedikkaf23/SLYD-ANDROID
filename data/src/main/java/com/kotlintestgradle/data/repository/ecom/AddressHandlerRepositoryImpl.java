package com.kotlintestgradle.data.repository.ecom;

import android.util.Log;
import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.cache.entity.UserAddress;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.AddressMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.location.LocationData;
import com.kotlintestgradle.repository.AddressHandlerRepository;
import javax.inject.Inject;
import kotlin.Triple;

public class AddressHandlerRepositoryImpl extends BaseRepository implements
    AddressHandlerRepository {
  private DatabaseManager mDatabaseManager;
  private PreferenceManager mPreferenceManager;
  private AddressMapper mMapper = new AddressMapper();

  @Inject
  public AddressHandlerRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mPreferenceManager = dataSource.preference();
    mDatabaseManager = dataSource.db();
  }

  @Override
  public boolean isAddressListEmpty() {
    if (mDatabaseManager != null) {
      final boolean[] isEmpty = {true};
      isEmpty[0] = mDatabaseManager.address().getRowCount() <= 0;
      Log.d("exe", "isEmpty[0]" + isEmpty[0] + " " + mDatabaseManager.address().getRowCount());
      return isEmpty[0];
    }
    return true;
  }

  @Override
  public AddressListItemData getDefaultAddress() {
    if (mDatabaseManager != null) {
      final UserAddress[] userAddress = new UserAddress[1];
      /*Thread t = new Thread() {
        public void run() {*/
      userAddress[0] = mDatabaseManager.address().getDefaultAddress();
      /* }
      };
      t.start();*/
      return mMapper.mapper(userAddress[0]);
    }
    return null;
  }

  @Override
  public boolean isCurrentAddressEmpty() {
    return mPreferenceManager.isCurrentLocationEmpty();
  }

  @Override
  public Triple<Double, Double, String> getCurrentLocation() {
    LocationData data = mPreferenceManager.getCurrentAddress();
    return new Triple<>(data.getLatitude(), data.getLongitude(), data.getCityName());
  }

  @Override
  public void storeCurrentLocation(double latitude, double longitude, String cityName) {
    mPreferenceManager.setCurrentAddress(new LocationData(latitude, longitude, cityName));
  }

  @Override
  public void storeDineCurrentLocation(double latitude, double longitude, String address) {
  }

  @Override
  public Triple<Double, Double, String> getDineCurrentLocation() {
    return new Triple<Double, Double, String>(0.0,0.0,"");
  }
}
