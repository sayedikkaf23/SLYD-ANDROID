package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.GetAddressMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.address.GetAddressUseCase;
import com.kotlintestgradle.repository.GetAddressRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetAddressRepositoryImpl extends BaseRepository implements GetAddressRepository {
  private DataSource dataSource;
  private GetAddressMapper mapper = new GetAddressMapper();
  private PreferenceManager preference;
  private DatabaseManager mDatabaseManager;

  @Inject
  public GetAddressRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    preference = dataSource.preference();
    mDatabaseManager = dataSource.db();
  }

  @Override
  public Single<GetAddressUseCase.ResponseValues> getFilteredProductList() {
    return dataSource.api().handler().getAddress(preference.getAuthToken(),
        preference.getUserId()).flatMap(details -> {
          mapper.insertToDatabase(details, mDatabaseManager);
          return Single.just(new GetAddressUseCase.ResponseValues(mapper.mapper(details)));
        });
  }
}
