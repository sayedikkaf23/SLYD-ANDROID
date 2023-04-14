package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.data.utils.DataConstants.LOWER_BOUND;

import android.util.Log;
import com.kotlintestgradle.cache.DatabaseManager;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.data.repository.observable.CartProductObservable;
import com.kotlintestgradle.model.ecom.CartProductData;
import com.kotlintestgradle.repository.CartHandlerRepository;
import io.reactivex.observables.ConnectableObservable;
import javax.inject.Inject;

public class CartHandlerRepositoryImpl extends BaseRepository implements CartHandlerRepository {
  private static final int NUMBER_OF_THREADS = 4;
  private DatabaseManager mDatabaseManager;

  @Inject
  public CartHandlerRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    CartProductObservable.getInstance();
    mDatabaseManager = dataSource.db();
  }

  @Override
  public boolean isCartEmpty() {
    final boolean isCartEmpty;
    isCartEmpty = mDatabaseManager.cart().getRowCount() <= LOWER_BOUND;
    return isCartEmpty;
  }

  @Override
  public boolean isProductExistInCart(final String productId) {
    final boolean isProductExist;
    isProductExist = mDatabaseManager.cart().getProductCount(productId) > LOWER_BOUND;
    Log.d("ItemExistInCart", "" + isProductExist);
    return isProductExist;
  }

  @Override
  public int getQuantity(String productId) {

    return mDatabaseManager.cart().getProductById(productId);
  }

  @Override
  public int getTotalCartItems() {
    return mDatabaseManager.cart().getRowCount();
  }

  @Override
  public int getTotalCartItems(int storeType) {
    return mDatabaseManager.cart().getRowCount(storeType);
  }

  @Override
  public int getTotalCartItems(int storeType, String productId) {
    return mDatabaseManager.cart().getTotalProductCount(storeType, productId);
  }

  @Override
  public ConnectableObservable<CartProductData> getCartObservable() {
    return CartProductObservable.getObservable();
  }
}
