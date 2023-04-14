package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.address.DeleteAddressUseCase;
import com.kotlintestgradle.repository.DeleteAddressRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class DeleteAddressRepositoryImpl extends BaseRepository implements DeleteAddressRepository {

  private DataSource dataSource;
  private SuccessMapper mapper = new SuccessMapper();
  private DatabaseManager mDatabaseManager;
  private PreferenceManager mPreferenceManager;

  @Inject
  public DeleteAddressRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    this.mDatabaseManager = dataSource.db();
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<DeleteAddressUseCase.ResponseValues> deleteAddress(final String addressId) {
    return dataSource.api().handler().deleteAddress(mPreferenceManager.getAuthToken(),
        addressId).flatMap(
        deleteAddressSuccess -> {
          mDatabaseManager.address().deleteByAddressId(addressId);
          return Single.just(
              new DeleteAddressUseCase.ResponseValues(mapper.mapper(deleteAddressSuccess)));
        });
  }
}
