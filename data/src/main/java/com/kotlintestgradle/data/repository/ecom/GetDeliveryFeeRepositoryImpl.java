package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.DeliveryFeeMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.address.GetDeliveryFeeUseCase;
import com.kotlintestgradle.remote.model.response.ecom.deliveryfee.DeliveryData;
import com.kotlintestgradle.repository.GetDeliveryFeeRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class GetDeliveryFeeRepositoryImpl extends BaseRepository implements
    GetDeliveryFeeRepository {
  private DataSource dataSource;
  private PreferenceManager mPreferenceManager;
  private DeliveryFeeMapper mapper = new DeliveryFeeMapper();

  @Inject
  public GetDeliveryFeeRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<GetDeliveryFeeUseCase.ResponseValues> calculateDeliveryFee(
          String cartId, String deliveryAddressId) {
    return dataSource.api().handler().calculateDeliveryFee(mPreferenceManager.getAuthToken(), cartId,
            deliveryAddressId).flatMap(new Function<DeliveryData, SingleSource<? extends GetDeliveryFeeUseCase.ResponseValues>>() {
      @Override
      public SingleSource<? extends GetDeliveryFeeUseCase.ResponseValues> apply(DeliveryData data) throws Exception {
        return Single.just(new GetDeliveryFeeUseCase.ResponseValues(mapper.mapper(data)));
      }
    });
  }
}

