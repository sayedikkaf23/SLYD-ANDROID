package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.remote.RemoteConstants.ECOM_FLOW;

import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.CartMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.GetCartProductsUseCase;
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartDetails;
import com.kotlintestgradle.repository.GetCartProductsRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class GetCartProductsRepositoryImpl extends BaseRepository implements
    GetCartProductsRepository {
  private DataSource mDataSource;
  private CartMapper mCartMapper = new CartMapper();
  private DatabaseManager mDatabaseManager;
  private PreferenceManager mPreferenceManager;

  @Inject
  public GetCartProductsRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    mDatabaseManager = dataSource.db();
    mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<GetCartProductsUseCase.ResponseValues> getCartProducts(final boolean isDine,
      String userId,
      String currencySymb, String currencyCode) {
    return mDataSource.api().handler().getCartProducts(isDine, mPreferenceManager.getAuthToken(),
        userId, currencySymb, currencyCode).flatMap(
        new Function<CartDetails, SingleSource<? extends GetCartProductsUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends GetCartProductsUseCase.ResponseValues> apply(
              final CartDetails cartDetails) throws Exception {
            Thread t = new Thread() {
              public void run() {
                mDataSource.preference().storeTotalAmount(
                    mCartMapper.mapper(cartDetails, mDatabaseManager,
                        ECOM_FLOW).getSubTotal());
                mDataSource.preference().storeTotalProductCount(
                    Integer.parseInt(mCartMapper.mapper(cartDetails, mDatabaseManager,
                        ECOM_FLOW).getTotalCartquantity()));
              }
            };
            return Single.just(
                new GetCartProductsUseCase.ResponseValues(
                    mCartMapper.mapper(cartDetails, mDatabaseManager, ECOM_FLOW)));
          }
        });
  }
}
