package com.kotlintestgradle.data.repository.observable;

import com.kotlintestgradle.model.ecom.common.ProductsData;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class WishListObservable extends Observable<ProductsData> {
  private static WishListObservable wishListItemEmitter;
  private Observer<? super ProductsData> observer;

  public static WishListObservable getInstance() {
    if (wishListItemEmitter == null) {
      wishListItemEmitter = new WishListObservable();
      return wishListItemEmitter;
    } else {
      return wishListItemEmitter;
    }
  }

  @Override
  protected void subscribeActual(Observer<? super ProductsData> observer) {
    this.observer = observer;
  }

  public void emit(ProductsData wishListProduct) {
    if (observer != null) {
      observer.onNext(wishListProduct);
    }
  }
}
