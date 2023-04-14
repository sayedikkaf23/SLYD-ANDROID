package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.model.ecom.CartProductData;
import com.kotlintestgradle.repository.CartHandlerRepository;
import io.reactivex.observables.ConnectableObservable;
import javax.inject.Inject;

public class CartHandlerImpl implements CartHandler {
  private CartHandlerRepository mRepository;

  @Inject
  public CartHandlerImpl(CartHandlerRepository cartHandlerRepository) {
    this.mRepository = cartHandlerRepository;
  }

  @Override
  public boolean isCartEmpty() {
    return mRepository.isCartEmpty();
  }

  @Override
  public boolean isProductExistInCart(String productId) {
    return mRepository.isProductExistInCart(productId);
  }

  @Override
  public int getQuantity(String productId) {
    return mRepository.getQuantity(productId);
  }

  @Override
  public int totalCartItems() {
    if (mRepository.getTotalCartItems() > 0) {
      return mRepository.getTotalCartItems();
    } else {
      return 0;
    }
  }

  @Override
  public int totalCartItems(int storeType) {
    if (mRepository.getTotalCartItems(storeType) > 0) {
      return mRepository.getTotalCartItems(storeType);
    } else {
      return 0;
    }
  }

  @Override
  public int totalCartItems(int storeType, String productId) {
    if (mRepository.getTotalCartItems(storeType, productId) > 0) {
      return mRepository.getTotalCartItems(storeType, productId);
    } else {
      return 0;
    }
  }

  @Override
  public ConnectableObservable<CartProductData> getCartObservable() {
    return mRepository.getCartObservable();
  }
}
