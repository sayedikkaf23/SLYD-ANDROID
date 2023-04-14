package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.data.repository.observable.DefaultAddressObservable;
import com.kotlintestgradle.interactor.ecom.address.MakeAddressDefaultUseCase;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.remote.model.request.ecom.MakeAddressDefaultRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.MakeAddressDefaultRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class MakeAddressDefaultRepositoryImpl extends BaseRepository implements
    MakeAddressDefaultRepository {
  private DataSource mDataSource;
  private SuccessMapper mSuccessMapper = new SuccessMapper();
  private DatabaseManager mDatabaseManager;
  private PreferenceManager mPreferenceManager;

  @Inject
  public MakeAddressDefaultRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    mDatabaseManager = dataSource.db();
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<MakeAddressDefaultUseCase.ResponseValues> makeAddressDefault(
      final String addressId) {
    return mDataSource.api().handler().makeAddressDefault(mPreferenceManager.getAuthToken(),
        new MakeAddressDefaultRequest(addressId)).flatMap(
        new Function<CommonModel,
            SingleSource<? extends MakeAddressDefaultUseCase.ResponseValues>>() {
            @Override
          public SingleSource<? extends MakeAddressDefaultUseCase.ResponseValues> apply(
                CommonModel model) throws Exception {
              mDatabaseManager.address().makeAddressAsNonDefault();
              mDatabaseManager.address().makeAddressDefault(addressId);
              DefaultAddressObservable.getInstance().postData(new AddressListItemData(addressId));
              return Single.just(
                  new MakeAddressDefaultUseCase.ResponseValues(mSuccessMapper.mapper(model)));
            }
          });
  }
}
