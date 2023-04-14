package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.data.repository.observable.WishListObservable;
import com.kotlintestgradle.interactor.ecom.AddProductToWishListUseCase;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import com.kotlintestgradle.remote.model.request.ecom.AddToWishListRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.AddProductToWishListRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class AddProductToWishListRepositoryImpl extends BaseRepository implements
    AddProductToWishListRepository {

  private DataSource dataSource;
  private PreferenceManager mPreferenceManager;
  private SuccessMapper mSuccessMapper = new SuccessMapper();

  @Inject
  public AddProductToWishListRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<AddProductToWishListUseCase.ResponseValues> addProductWishList(String productId,
      String ipAddress, double latitude, double longitude,String cityId, String countryId,String userId) {

    WishListObservable.getInstance().emit(new ProductsData(productId, false));

    return dataSource.api().handler().addProductToWishList(mPreferenceManager.getAuthToken(),
        new AddToWishListRequest(userId, productId, ipAddress, latitude, longitude,cityId,
            countryId)).flatMap(
        new Function<CommonModel, SingleSource<?
            extends AddProductToWishListUseCase.ResponseValues>>() {
                @Override
                public SingleSource<? extends AddProductToWishListUseCase.ResponseValues> apply(
                    CommonModel model) throws Exception {
                  return Single.just(
                      new AddProductToWishListUseCase.ResponseValues(mSuccessMapper.mapper(model)));
                }
              });
  }
}
