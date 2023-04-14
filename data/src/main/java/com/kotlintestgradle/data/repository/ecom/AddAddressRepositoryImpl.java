package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.cache.entity.RecentAddress;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.address.AddAddressUseCase;
import com.kotlintestgradle.remote.model.request.ecom.AddAddressRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.AddAddressRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class AddAddressRepositoryImpl extends BaseRepository implements AddAddressRepository {

  private DataSource dataSource;
  private SuccessMapper mapper = new SuccessMapper();
  private PreferenceManager preference;
  private DatabaseManager mDatabaseManager;

  @Inject
  public AddAddressRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    preference = dataSource.preference();
    mDatabaseManager = dataSource.db();
  }

  @Override
  public Single<AddAddressUseCase.ResponseValues> addAddress(boolean isApiCall,String name,
      String mobileNumber, String mobileNumberCode, String locality,final String addLine1,
      String addLine2, String landmark, final String city,
      final String country, final String placeId, String pincode, final String latitude, final String longitude,
      String taggedAs, String state, int tagged) {

    final boolean[] isDefault = new boolean[1];
    Thread t = new Thread() {
      public void run() {
        isDefault[0] = mDatabaseManager.address().getRowCount() <= 0;
      }
    };
    t.start();

    if (isApiCall){
      return dataSource.api()
          .handler().addAddress(preference.getAuthToken(),
              new AddAddressRequest(preference.getUserId(), name, mobileNumber, mobileNumberCode,
                  locality, addLine1, addLine2, landmark, city, country, placeId, pincode, latitude,
                  longitude, taggedAs, state, tagged, isDefault[0])).flatMap(
              new Function<CommonModel, SingleSource<? extends AddAddressUseCase.ResponseValues>>() {
                @Override
                public SingleSource<? extends AddAddressUseCase.ResponseValues> apply(
                    CommonModel addAddressSuccess) throws Exception {
                  return Single.just(
                      new AddAddressUseCase.ResponseValues(mapper.mapper(addAddressSuccess)));
                }
              });
    }else {


      Thread t1 = new Thread() {
        public void run() {
          dataSource.db().recentAddress().insertAddress(new RecentAddress(placeId,addLine1,Double.parseDouble(latitude),
              Double.parseDouble(longitude),city,country));
        }
      };
      t1.start();
      return Single.just(new AddAddressUseCase.ResponseValues(mapper.mapper(new CommonModel(""))));
    }

  }
}
