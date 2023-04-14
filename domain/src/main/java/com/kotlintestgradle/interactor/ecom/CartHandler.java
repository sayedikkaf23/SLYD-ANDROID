package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.model.ecom.CartProductData;
import io.reactivex.observables.ConnectableObservable;

public interface CartHandler {

  boolean isCartEmpty();
  boolean isProductExistInCart(String productId);
  int getQuantity(String productId);
  int totalCartItems();
  int totalCartItems(int storeType);
  int totalCartItems(int storeType, String productId);
  ConnectableObservable<CartProductData> getCartObservable();
}
