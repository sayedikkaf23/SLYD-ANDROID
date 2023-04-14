package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.ClearWishListUseCase;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.repository.ClearWishListRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class ClearWishListRepositoryImpl extends BaseRepository implements ClearWishListRepository {

  private DataSource mDataSource;
  private PreferenceManager mPreferenceManager;
  private SuccessMapper mMapper = new SuccessMapper();

  @Inject
  public ClearWishListRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mDataSource = dataSource;
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<ClearWishListUseCase.ResponseValues> clearWishList() {
    return mDataSource.api().handler().clearAllWishListItems(mPreferenceManager.getAuthToken()).flatMap(
        new Function<CommonModel, SingleSource<? extends ClearWishListUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends ClearWishListUseCase.ResponseValues> apply(
              CommonModel model) throws Exception {
            return Single.just(new ClearWishListUseCase.ResponseValues(mMapper.mapper(model)));
          }
        });
  }
}
