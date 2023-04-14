package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.data.repository.observable.WishListObservable;
import com.kotlintestgradle.interactor.ecom.DeleteWishListProductUseCase;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import com.kotlintestgradle.remote.model.request.ecom.AddToWishListRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.DeleteWishListProductRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class DeleteWishListProductRepositoryImpl extends BaseRepository
    implements DeleteWishListProductRepository {
  private DataSource dataSource;
  private SuccessMapper mapper = new SuccessMapper();
  private PreferenceManager preference;

  @Inject
  public DeleteWishListProductRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    preference = dataSource.preference();
  }

  @Override
  public Single<DeleteWishListProductUseCase.ResponseValues> deleteWishListProduct(String productId,
      String ipAddress, double latitude, double longitude,String cityId,String countryId) {

    WishListObservable.getInstance().emit(new ProductsData(productId, true));
    return dataSource.api().handler().deleteWishListProduct(preference.getAuthToken(),
        new AddToWishListRequest(preference.getUserId(), productId, ipAddress, latitude,
            longitude, cityId, countryId)).flatMap(
        new Function<CommonModel, SingleSource<?
            extends DeleteWishListProductUseCase.ResponseValues>>() {
                @Override
                public SingleSource<? extends DeleteWishListProductUseCase.ResponseValues> apply(
                    CommonModel model) throws Exception {
                  return Single.just(
                      new DeleteWishListProductUseCase.ResponseValues(mapper.mapper(model)));
                }
              });
  }
}
