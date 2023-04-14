package com.kotlintestgradle.data.repository.observable;

import android.annotation.SuppressLint;
import com.kotlintestgradle.model.ecom.getcart.CartData;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observables.ConnectableObservable;

public class CartDataObservable {
  private static CartDataObservable rxConnectable;
  private static ConnectableObservable<CartData> connectableObservable;
  private ObservableEmitter<CartData> emitor;

  @SuppressLint("CheckResult")
  public CartDataObservable() {
    Observable<CartData> observable = Observable.create(new ObservableOnSubscribe<CartData>() {
      @Override
      public void subscribe(ObservableEmitter<CartData> e) throws Exception {
        emitor = e;
      }
    });
    connectableObservable = observable.publish();
    connectableObservable.share();
    connectableObservable.replay();
    connectableObservable.connect();
  }

  public static CartDataObservable getInstance() {
    if (rxConnectable == null) {
      rxConnectable = new CartDataObservable();
      return rxConnectable;
    } else {
      return rxConnectable;
    }
  }

  public static ConnectableObservable<CartData> getObservable() {
    return connectableObservable;
  }

  public void postData(CartData userData) {
    if (emitor != null && userData != null) {
      emitor.onNext(userData);
    }
  }
}
