package com.kotlintestgradle.repository;

import com.kotlintestgradle.model.ecom.CartProductData;
import io.reactivex.observables.ConnectableObservable;

public interface CartHandlerRepository {
  boolean isCartEmpty();

  boolean isProductExistInCart(String productId);

  int getQuantity(String productId);

  int getTotalCartItems();

  int getTotalCartItems(int storeType);

  int getTotalCartItems(int storeType, String productId);

  ConnectableObservable<CartProductData> getCartObservable();
}
