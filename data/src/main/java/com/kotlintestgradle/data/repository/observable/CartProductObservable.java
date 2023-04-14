package com.kotlintestgradle.data.repository.observable;

import android.annotation.SuppressLint;
import com.kotlintestgradle.model.ecom.CartProductData;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observables.ConnectableObservable;

public class CartProductObservable {
  private static CartProductObservable rxConnectable;
  private static ConnectableObservable<CartProductData> connectableObservable;
  private ObservableEmitter<CartProductData> emitor;

  @SuppressLint("CheckResult")
  public CartProductObservable() {
    Observable<CartProductData> observable = Observable.create(
            new ObservableOnSubscribe<CartProductData>() {
              @Override
              public void subscribe(ObservableEmitter<CartProductData> e) throws Exception {
                emitor = e;
              }
            });
    connectableObservable = observable.publish();
    connectableObservable.share();
    connectableObservable.replay();
    connectableObservable.connect();
  }

  public static CartProductObservable getInstance() {
    if (rxConnectable == null) {
      rxConnectable = new CartProductObservable();
      return rxConnectable;
    } else {
      return rxConnectable;
    }
  }

  public static ConnectableObservable<CartProductData> getObservable() {
    return connectableObservable;
  }

  public void postData(CartProductData userData) {
    if (emitor != null && userData != null) {
      emitor.onNext(userData);
    }
  }
}
